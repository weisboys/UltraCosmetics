package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class MountHeldItem extends Mount {
    private static final NamespacedKey ITEM_KEY = new NamespacedKey(UltraCosmeticsData.get().getPlugin(), "UC_HELD_ITEM");

    public MountHeldItem(UltraPlayer ultraPlayer, MountType type, UltraCosmetics ultraCosmetics) {
        super(ultraPlayer, type, ultraCosmetics);
    }

    @Override
    public void clear() {
        super.clear();
        getPlayer().getInventory().setItem(SettingsManager.getConfig().getInt("Gadget-Slot"), null);
    }

    @Override
    public boolean tryEquip() {
        getOwner().removeCosmetic(Category.GADGETS);
        getOwner().removeCosmetic(Category.MOUNTS);
        int slot = SettingsManager.getConfig().getInt("Gadget-Slot");
        if (getPlayer().getInventory().getItem(slot) != null) {
            getPlayer().sendMessage(MessageManager.getMessage("Must-Remove.Mounts").replace("%slot%", String.valueOf(slot + 1)));
            return false;
        }
        getPlayer().getInventory().setItem(slot, getHeldItem());
        getPlayer().getInventory().setHeldItemSlot(slot);
        return true;
    }

    @Override
    public void onUpdate() {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer() == getPlayer() && itemMatches(event.getItemDrop().getItemStack())) {
            if (SettingsManager.getConfig().getBoolean("Remove-Gadget-With-Drop")) {
                clear();
                event.getItemDrop().remove();
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player != getPlayer()) return;
        if (itemMatches(event.getCurrentItem()) || itemMatches(event.getCursor()) || (event.getClick() == ClickType.NUMBER_KEY
                && itemMatches(player.getInventory().getItem(event.getHotbarButton())))) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void cancelMove(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player == getPlayer() && (itemMatches(event.getCurrentItem())) || itemMatches(event.getCursor())) {
            event.setCancelled(true);
            player.closeInventory(); // Close the inventory because clicking again results in the event being handled
                                     // client side
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer() == getPlayer() && event.getRightClicked() instanceof ItemFrame
                && itemMatches(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }

    public boolean itemMatches(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return false;
        return stack.getItemMeta().getPersistentDataContainer().has(ITEM_KEY, PersistentDataType.INTEGER);
    }

    public ItemStack getHeldItem() {
        ItemStack stack = new ItemStack(getHeldItemMaterial());
        ItemMeta meta = stack.getItemMeta();
        meta.getPersistentDataContainer().set(ITEM_KEY, PersistentDataType.INTEGER, 1);
        stack.setItemMeta(meta);
        return stack;
    }

    public abstract Material getHeldItemMaterial();
}
