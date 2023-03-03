package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.Cosmetic;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.util.PlayerUtils;
import be.isach.ultracosmetics.util.TextUtil;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Represents an instance of a Gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public abstract class Gadget extends Cosmetic<GadgetType> {

    private static final DecimalFormatSymbols OTHER_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        OTHER_SYMBOLS.setDecimalSeparator('.');
        OTHER_SYMBOLS.setGroupingSeparator('.');
        OTHER_SYMBOLS.setPatternSeparator('.');
        DECIMAL_FORMAT = new DecimalFormat("0.0", OTHER_SYMBOLS);
    }

    /**
     * Gadget ItemStack.
     */
    protected ItemStack itemStack;

    /**
     * If true, will display cooldown left when fail on use because cooldown active.
     */
    protected boolean displayCooldownMessage = true;

    /**
     * Last Clicked Block by the player.
     */
    protected Block lastClickedBlock;

    /**
     * If Gadget interaction should tick asynchronously.
     */
    private final boolean asynchronous;

    // Cache the actual material value so we don't have to keep calling parseMaterial
    private final Material material;

    private final int slot;

    public Gadget(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        this(owner, type, ultraCosmetics, false);
    }

    public Gadget(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics, boolean asynchronous) {
        super(owner, type, ultraCosmetics);
        material = type.getMaterial().parseMaterial();
        this.asynchronous = asynchronous;
        this.slot = SettingsManager.getConfig().getInt("Gadget-Slot");
    }

    @Override
    public boolean tryEquip() {
        getOwner().removeCosmetic(Category.GADGETS);
        if (getPlayer().getInventory().getItem(slot) != null) {
            getPlayer().sendMessage(MessageManager.getMessage("Must-Remove.Gadgets").replace("%slot%", String.valueOf(slot + 1)));
            return false;
        }

        updateItemStack();
        getPlayer().getInventory().setItem(slot, itemStack);
        return true;
    }

    @Override
    public void onEquip() {
    }

    @Override
    public void run() {
        if (getOwner() == null || getPlayer() == null) return;

        UltraPlayer owner = getOwner();
        if (owner.getCurrentGadget() == null || owner.getCurrentGadget().getType() != getType()) {
            clear();
            return;
        }
        // Only Updatable cosmetics schedule this task
        ((Updatable) this).onUpdate();
        if (UltraCosmeticsData.get().displaysCooldownInBar()) {
            @SuppressWarnings("deprecation")
            ItemStack hand = getPlayer().getItemInHand();
            if (itemMatches(hand) && !owner.canUse(getType())) {
                owner.sendCooldownBar(getType(), getType().getCountdown(), getType().getRunTime());
            }
        }

        double left = owner.getCooldown(getType());
        if (left > 0) {
            String leftRounded = DECIMAL_FORMAT.format(left);
            double decimalRoundedValue = Double.parseDouble(leftRounded);
            if (decimalRoundedValue == 0) {
                String message = MessageManager.getMessage("Gadgets.Gadget-Ready-ActionBar");
                message = message.replace("%gadgetname%",
                        TextUtil.filterPlaceHolder(getType().getName()));
                ActionBar.sendActionBar(getPlayer(), message);
                play(XSound.BLOCK_NOTE_BLOCK_HAT, getPlayer(), 1.4f, 1.5f);
            }
        }
    }

    @Override
    public void clear() {
        removeItem();
        super.clear();
    }

    /**
     * Removes the item from the player.
     * Does not assume item is in the correct slot,
     * or that there is only one item.
     */
    public void removeItem() {
        PlayerUtils.removeItems(getPlayer(), this::itemMatches);
    }

    /**
     * Gets the gadget current Item Stack.
     * If using ammo system, this may not be up-to-date.
     * Call {@link #updateItemStack()} to update itemstack immediately.
     *
     * @return current itemstack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void updateItemStack() {
        String ammo = "";
        if (UltraCosmeticsData.get().isAmmoEnabled() && getType().requiresAmmo()) {
            ammo = ChatColor.WHITE.toString() + ChatColor.BOLD + getOwner().getAmmo(getType()) + " ";
        }
        itemStack = ItemFactory.create(getType().getMaterial(), ammo + getType().getName(), MessageManager.getMessage("Gadgets.Lore"));
    }

    protected boolean checkRequirements(PlayerInteractEvent event) {
        return true;
    }

    protected void play(XSound sound, Entity entity, float volume, float pitch) {
        if (!SettingsManager.getConfig().getBoolean("Gadgets-Are-Silent")) {
            sound.play(entity, volume, pitch);
        }
    }

    protected void play(XSound sound, Location loc, float volume, float pitch) {
        if (!SettingsManager.getConfig().getBoolean("Gadgets-Are-Silent")) {
            sound.play(loc, volume, pitch);
        }
    }

    public boolean itemMatches(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta() || stack.getType() != getItemStack().getType() || !stack.getItemMeta().hasDisplayName()) {
            return false;
        }
        return stack.getItemMeta().getDisplayName().endsWith(getType().getName());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (getPlayer() == event.getPlayer() && event.getRightClicked() instanceof ItemFrame
                && itemMatches(event.getPlayer().getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) return;
        Player player = event.getPlayer();
        if (player != getPlayer()) return;
        @SuppressWarnings("deprecation")
        ItemStack itemStack = player.getItemInHand();
        if (itemStack.getType() != material) return;
        if (player.getInventory().getHeldItemSlot() != slot) return;
        if (UltraCosmeticsData.get().getServerVersion().offhandAvailable()) {
            if (event.getHand() != EquipmentSlot.HAND) return;
        }
        event.setCancelled(true);
        // player.updateInventory();
        UltraPlayer ultraPlayer = getUltraCosmetics().getPlayerManager().getUltraPlayer(event.getPlayer());

        if (ultraPlayer.getCurrentTreasureChest() != null) return;

        if (PlayerAffectingCosmetic.isVanished(player) && SettingsManager.getConfig().getBoolean("Prevent-Cosmetics-In-Vanish")) {
            getOwner().clear();
            getPlayer().sendMessage(MessageManager.getMessage("Not-Allowed-In-Vanish"));
            return;
        }

        if (!ultraPlayer.hasGadgetsEnabled()) {
            getPlayer().sendMessage(MessageManager.getMessage("Gadgets-Enabled-Needed"));
            return;
        }

        if (UltraCosmeticsData.get().isAmmoEnabled() && getType().requiresAmmo() && ultraPlayer.getAmmo(getType()) < 1) {
            if (UltraCosmeticsData.get().isAmmoPurchaseEnabled() && getUltraCosmetics().getEconomyHandler().isUsingEconomy()) {
                getUltraCosmetics().getMenus().openAmmoPurchaseMenu(getType(), getOwner());
            } else {
                player.sendMessage(MessageManager.getMessage("No-Ammo"));
            }
            return;
        }

        if (!checkRequirements(event)) return;

        double coolDown = ultraPlayer.getCooldown(getType());
        if (coolDown > 0) {
            String timeLeft = new DecimalFormat("#.#").format(coolDown);
            if (getType().getCountdown() > 1) {
                getPlayer().sendMessage(MessageManager.getMessage("Gadgets.Countdown-Message")
                        .replace("%gadgetname%", TextUtil.filterPlaceHolder(getType().getName()))
                        .replace("%time%", timeLeft));
            }
            return;
        }
        ultraPlayer.setCoolDown(getType(), getType().getCountdown(), getType().getRunTime());
        if (UltraCosmeticsData.get().isAmmoEnabled() && getType().requiresAmmo()) {
            ultraPlayer.removeAmmo(getType());
            updateItemStack();
            getPlayer().getInventory().setItem(slot, this.itemStack);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            lastClickedBlock = event.getClickedBlock();
        }
        boolean isLeft = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        Runnable callClick = isLeft ? this::onLeftClick : this::onRightClick;
        if (asynchronous) {
            Bukkit.getScheduler().runTaskAsynchronously(getUltraCosmetics(), callClick);
        } else {
            callClick.run();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(getItemStack())) {
            if (SettingsManager.getConfig().getBoolean("Remove-Gadget-With-Drop")) {
                clear();
                event.getItemDrop().remove();
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     */
    @EventHandler
    public void cancelMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player != getPlayer()) return;
        // If clicked item is the gadget
        if (itemMatches(event.getCurrentItem())) {
            // Item is not where it should be, clear it
            if (event.getSlot() != slot) {
                clear();
            }
            // If other item in hotbar swap is the gadget
        } else if (event.getClick() == ClickType.NUMBER_KEY && itemMatches(player.getInventory().getItem(event.getHotbarButton()))) {
            if (event.getHotbarButton() != slot) {
                clear();
            }
        } else {
            return;
        }
        event.setCancelled(true);
        player.updateInventory();
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     * Does this actually do anything?
     */
    @EventHandler
    public void cancelMove(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player != getPlayer()) return;
        for (ItemStack item : event.getNewItems().values()) {
            if (itemMatches(item)) {
                event.setCancelled(true);
                player.updateInventory();
                player.closeInventory();
                return;
            }
        }
    }

    /**
     * Cancel players from removing, picking the item in their inventory.
     */
    @EventHandler
    public void cancelMove(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item != null && player == getPlayer() && item.equals(itemStack)) {
            event.setCancelled(true);
            // Close the inventory because clicking again results in the event being handled client side
            player.closeInventory();
        }
    }

    /**
     * Called when a right-click is performed, and potentially when a left-click
     * is performed, depending on the implementation of onLeftClick()
     */
    protected abstract void onRightClick();

    /**
     * Called when a left click is done with gadget.
     */
    protected void onLeftClick() {
        onRightClick();
    }

}
