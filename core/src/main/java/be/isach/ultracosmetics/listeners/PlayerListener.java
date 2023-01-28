package be.isach.ultracosmetics.listeners;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.menu.CosmeticsInventoryHolder;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.player.UltraPlayerManager;
import be.isach.ultracosmetics.run.FallDamageManager;
import be.isach.ultracosmetics.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Player listeners.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class PlayerListener implements Listener {

    private final UltraCosmetics ultraCosmetics;
    private final UltraPlayerManager pm;
    private final ItemStack menuItem;

    public PlayerListener(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
        this.pm = ultraCosmetics.getPlayerManager();
        this.menuItem = ItemFactory.createMenuItem();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {
        // Load UltraPlayer whether we use it or not so it's ready
        UltraPlayer up = pm.getUltraPlayer(event.getPlayer());
        if (SettingsManager.getConfig().getBoolean("Menu-Item.Enabled") && event.getPlayer().hasPermission("ultracosmetics.receivechest") && SettingsManager.isAllowedWorld(event.getPlayer().getWorld())) {
            if (up != null) {
                up.giveMenuItem();
            }
        }

        if (ultraCosmetics.getUpdateChecker() != null && ultraCosmetics.getUpdateChecker().isOutdated()) {
            if (event.getPlayer().hasPermission("ultracosmetics.updatenotify")) {
                event.getPlayer().sendMessage(MessageManager.getMessage("Prefix") + ChatColor.RED + ChatColor.BOLD + "An update is available: " + ultraCosmetics.getUpdateChecker().getLastVersion());
                event.getPlayer().sendMessage(MessageManager.getMessage("Prefix") + ChatColor.RED + ChatColor.BOLD + "Use " + ChatColor.YELLOW + "/uc update" + ChatColor.RED + ChatColor.BOLD + " to install the update.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        if (SettingsManager.isAllowedWorld(event.getPlayer().getWorld())) {
            UltraPlayer up = pm.getUltraPlayer(event.getPlayer());
            if (SettingsManager.getConfig().getBoolean("Menu-Item.Enabled") && event.getPlayer().hasPermission("ultracosmetics.receivechest")) {
                up.giveMenuItem();
            }
            // If the player joined an allowed world from a non-allowed world, re-equip their cosmetics.
            if (!SettingsManager.isAllowedWorld(event.getFrom())) {
                up.getProfile().equip();
            }
        }
    }

    // run this as early as possible for compatibility with MV-inventories
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldChangeEarly(final PlayerChangedWorldEvent event) {
        UltraPlayer ultraPlayer = pm.getUltraPlayer(event.getPlayer());
        if (!SettingsManager.isAllowedWorld(event.getPlayer().getWorld())) {
            // Disable cosmetics when joining a bad world.
            ultraPlayer.removeMenuItem();
            ultraPlayer.setPreserveEquipped(true);
            if (ultraPlayer.clear()) {
                ultraPlayer.getBukkitPlayer().sendMessage(MessageManager.getMessage("World-Disabled"));
            }
            ultraPlayer.setPreserveEquipped(false);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        if (isMenuItem(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getItemDrop().remove();
            ItemStack chest = event.getPlayer().getItemInHand().clone();
            chest.setAmount(1);
            event.getPlayer().setItemInHand(chest);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        UltraPlayer ultraPlayer = pm.getUltraPlayer(event.getPlayer());
        // apparently can happen if a player disconnected while on a pressure plate
        if (ultraPlayer == null) return;
        // Avoid triggering this when clicking in the inventory
        InventoryType t = event.getPlayer().getOpenInventory().getType();
        if (t != InventoryType.CRAFTING && t != InventoryType.CREATIVE) {
            return;
        }
        if (ultraPlayer.getCurrentTreasureChest() != null) {
            event.setCancelled(true);
            return;
        }
        if (isMenuItem(event.getItem())) {
            event.setCancelled(true);
            ultraCosmetics.getMenus().getMainMenu().open(ultraPlayer);
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!SettingsManager.isAllowedWorld(player.getWorld())) return;
        boolean isMenuItem = isMenuItem(event.getCurrentItem()) || isMenuItem(event.getCursor()) || (event.getClick() == ClickType.NUMBER_KEY && isMenuItem(player.getInventory().getItem(event.getHotbarButton())));
        // TODO: redundant check? see be.isach.ultracosmetics.menu.Menu#onClick
        if (event.getView().getTopInventory().getHolder() instanceof CosmeticsInventoryHolder
                || isMenuItem) {
            event.setCancelled(true);
            player.updateInventory();
            if (isMenuItem && SettingsManager.getConfig().getBoolean("Menu-Item.Open-Menu-On-Inventory-Click", false)) {
                // if it's not delayed by one tick, the client holds the item in cursor slot until they open their inventory again
                Bukkit.getScheduler().runTaskLater(ultraCosmetics, () -> ultraCosmetics.getMenus().getMainMenu().open(pm.getUltraPlayer(player)), 1);
            }
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!SettingsManager.isAllowedWorld(player.getWorld())) return;
        if (isMenuItem(event.getCurrentItem()) || isMenuItem(event.getCursor())) {
            event.setCancelled(true);
            player.closeInventory(); // Close the inventory because clicking again results in the event being handled client side
            if (SettingsManager.getConfig().getBoolean("Menu-Item.Open-Menu-On-Inventory-Click", false)) {
                Bukkit.getScheduler().runTaskLater(ultraCosmetics, () -> ultraCosmetics.getMenus().getMainMenu().open(pm.getUltraPlayer(player)), 1);
            }
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelMove(InventoryDragEvent event) {
        for (ItemStack item : event.getNewItems().values()) {
            if (isMenuItem(item)) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).updateInventory();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (SettingsManager.getConfig().getBoolean("Menu-Item.Enabled") && SettingsManager.isAllowedWorld(event.getPlayer().getWorld())) {
            int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
            if (event.getPlayer().getInventory().getItem(slot) != null) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), event.getPlayer().getInventory().getItem(slot));
                event.getPlayer().getInventory().setItem(slot, null);
            }
            String name = ChatColor.translateAlternateColorCodes('&', SettingsManager.getConfig().getString("Menu-Item.Displayname"));
            ItemStack stack = ItemFactory.getItemStackFromConfig("Menu-Item.Type");
            event.getPlayer().getInventory().setItem(slot, ItemFactory.rename(stack, name));
        }
        pm.getUltraPlayer(event.getPlayer()).getProfile().equip();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        UltraPlayer up = pm.getUltraPlayer(event.getPlayer());
        if (up.getCurrentTreasureChest() != null) {
            up.getCurrentTreasureChest().forceOpen(0);
        }
        up.setPreserveEquipped(true);
        up.saveCosmeticsProfile();
        up.clear();
        up.removeMenuItem();
        // workaround plugins calling events after player quit
        Bukkit.getScheduler().runTaskLater(ultraCosmetics, () -> pm.remove(event.getPlayer()), 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        // Ignore NPC deaths as per iSach#467
        if (Bukkit.getPlayer(event.getEntity().getUniqueId()) == null) return;
        int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
        if (isMenuItem(event.getEntity().getInventory().getItem(slot))) {
            event.getDrops().remove(event.getEntity().getInventory().getItem(slot));
            event.getEntity().getInventory().setItem(slot, null);
        }
        UltraPlayer ultraPlayer = pm.getUltraPlayer(event.getEntity());
        if (ultraPlayer.getCurrentGadget() != null) {
            event.getDrops().remove(ultraPlayer.getCurrentGadget().getItemStack());
        }
        if (ultraPlayer.getCurrentHat() != null) event.getDrops().remove(ultraPlayer.getCurrentHat().getItemStack());
        Arrays.asList(ArmorSlot.values()).forEach(armorSlot -> {
            if (ultraPlayer.getCurrentSuit(armorSlot) != null) {
                event.getDrops().remove(ultraPlayer.getCurrentSuit(armorSlot).getItemStack());
            }
        });
        if (ultraPlayer.getCurrentEmote() != null) {
            event.getDrops().remove(ultraPlayer.getCurrentEmote().getItemStack());
        }

        ultraPlayer.setPreserveEquipped(true);
        for (Category cat : Category.values()) {
            if (cat.isClearOnDeath()) {
                ultraPlayer.removeCosmetic(cat);
            }
        }
        ultraPlayer.setPreserveEquipped(false);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                && FallDamageManager.shouldBeProtected(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getDamager().hasMetadata("uc_firework")) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickUpItem(org.bukkit.event.player.PlayerPickupItemEvent event) {
        if (isMenuItem(event.getItem().getItemStack())) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractGhost(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().hasMetadata("C_AD_ArmorStand")) event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("ultracosmetics.bypass.disabledcommands")) return;
        String strippedCommand = event.getMessage().split(" ")[0].replace("/", "").toLowerCase();
        if (!SettingsManager.getConfig().getList("Disabled-Commands").contains(strippedCommand)) return;
        UltraPlayer player = pm.getUltraPlayer(event.getPlayer());
        if (player.hasCosmeticsEquipped()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageManager.getMessage("Disabled-Command-Message"));
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        UltraPlayer ultraPlayer = pm.getUltraPlayer(event.getPlayer());
        if (ultraPlayer.getCurrentGadget() != null && ultraPlayer.getCurrentGadget().getItemStack().equals(event.getItem())) {
            event.setCancelled(true);
            return;
        }
        for (ArmorSlot armorSlot : ArmorSlot.values()) {
            if (ultraPlayer.getCurrentSuit(armorSlot) != null) {
                if (event.getItem().equals(ultraPlayer.getCurrentSuit(armorSlot).getItemStack())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private boolean isMenuItem(ItemStack item) {
        return menuItem.equals(item);
    }
}
