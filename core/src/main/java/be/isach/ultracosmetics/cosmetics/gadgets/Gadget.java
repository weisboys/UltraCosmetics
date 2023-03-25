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
import be.isach.ultracosmetics.util.UnmovableItemProvider;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
public abstract class Gadget extends Cosmetic<GadgetType> implements UnmovableItemProvider {

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

    private final int slot = SettingsManager.getConfig().getInt("Gadget-Slot");

    private final boolean removeWithDrop = SettingsManager.getConfig().getBoolean("Remove-Gadget-With-Drop");

    private final boolean showCooldownInBar = UltraCosmeticsData.get().displaysCooldownInBar();

    private boolean handledThisTick = false;

    public Gadget(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        this(owner, type, ultraCosmetics, false);
    }

    public Gadget(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics, boolean asynchronous) {
        super(owner, type, ultraCosmetics);
        material = type.getMaterial().parseMaterial();
        this.asynchronous = asynchronous;
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
        // All gadgets need a task running if we need to show the cooldown bar.
        // The base Cosmetic class schedules it anyway if the cosmetic is Updatable,
        // so we just need to handle the other case.
        // The base Cosmetic class attempts to cancel the task for Updatable and
        // non-Updatable cosmetics alike, so we don't need to worry about that.
        if (!(this instanceof Updatable)) {
            scheduleTask();
        }
        getUltraCosmetics().getUnmovableItemListener().addProvider(this);
    }

    @Override
    public void run() {
        // For some reason, the client sends two `use` packets for snowballs and
        // ender pearls, so we have to figure out how to ignore one of them.
        handledThisTick = false;
        if (getOwner() == null || getPlayer() == null) return;

        UltraPlayer owner = getOwner();
        if (owner.getCurrentGadget() == null || owner.getCurrentGadget().getType() != getType()) {
            clear();
            return;
        }
        if (this instanceof Updatable) {
            ((Updatable) this).onUpdate();
        }
        if (showCooldownInBar) {
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
        getUltraCosmetics().getUnmovableItemListener().removeProvider(this);
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
        if (stack == null || stack.getType() != getItemStack().getType() || !stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) {
            return false;
        }
        return stack.getItemMeta().getDisplayName().endsWith(getType().getName());
    }

    @Override
    public void handleInteract(PlayerInteractEvent event) {
        if (handledThisTick) return;
        handledThisTick = true;
        Player player = event.getPlayer();
        event.setCancelled(true);
        UltraPlayer ultraPlayer = getUltraCosmetics().getPlayerManager().getUltraPlayer(player);

        if (ultraPlayer.getCurrentTreasureChest() != null) return;

        if (PlayerAffectingCosmetic.isVanished(event.getPlayer()) && SettingsManager.getConfig().getBoolean("Prevent-Cosmetics-In-Vanish")) {
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

    @Override
    public void handleDrop(PlayerDropItemEvent event) {
        if (removeWithDrop) {
            clear();
            event.getItemDrop().remove();
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void moveItem(int slot, Player player) {
        clear();
    }
}
