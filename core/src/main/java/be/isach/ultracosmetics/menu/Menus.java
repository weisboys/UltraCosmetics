package be.isach.ultracosmetics.menu;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.menu.menus.MenuDeathEffects;
import be.isach.ultracosmetics.menu.menus.MenuEmotes;
import be.isach.ultracosmetics.menu.menus.MenuGadgets;
import be.isach.ultracosmetics.menu.menus.MenuHats;
import be.isach.ultracosmetics.menu.menus.MenuMain;
import be.isach.ultracosmetics.menu.menus.MenuMorphs;
import be.isach.ultracosmetics.menu.menus.MenuMounts;
import be.isach.ultracosmetics.menu.menus.MenuParticleEffects;
import be.isach.ultracosmetics.menu.menus.MenuPets;
import be.isach.ultracosmetics.menu.menus.MenuProjectileEffects;
import be.isach.ultracosmetics.menu.menus.MenuPurchase;
import be.isach.ultracosmetics.menu.menus.MenuSuits;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores menus.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class Menus {

    private final UltraCosmetics ultraCosmetics;
    private final Map<Category,CosmeticMenu<?>> categoryMenus = new HashMap<>();
    private final MenuMain mainMenu;

    public Menus(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
        categoryMenus.put(Category.EMOTES, new MenuEmotes(ultraCosmetics));
        categoryMenus.put(Category.GADGETS, new MenuGadgets(ultraCosmetics));
        categoryMenus.put(Category.EFFECTS, new MenuParticleEffects(ultraCosmetics));
        categoryMenus.put(Category.HATS, new MenuHats(ultraCosmetics));
        categoryMenus.put(Category.MORPHS, new MenuMorphs(ultraCosmetics));
        categoryMenus.put(Category.MOUNTS, new MenuMounts(ultraCosmetics));
        categoryMenus.put(Category.PETS, new MenuPets(ultraCosmetics));
        categoryMenus.put(Category.PROJECTILE_EFFECTS, new MenuProjectileEffects(ultraCosmetics));
        categoryMenus.put(Category.DEATH_EFFECTS, new MenuDeathEffects(ultraCosmetics));
        MenuSuits ms = new MenuSuits(ultraCosmetics);
        categoryMenus.put(Category.SUITS_HELMET, ms);
        categoryMenus.put(Category.SUITS_CHESTPLATE, ms);
        categoryMenus.put(Category.SUITS_LEGGINGS, ms);
        categoryMenus.put(Category.SUITS_BOOTS, ms);
        this.mainMenu = new MenuMain(ultraCosmetics);
    }

    public MenuMain getMainMenu() {
        return mainMenu;
    }

    public CosmeticMenu<?> getCategoryMenu(Category category) {
        return categoryMenus.get(category);
    }

    /**
     * Opens Ammo Purchase Menu.
     */
    public void openAmmoPurchaseMenu(GadgetType type, UltraPlayer player) {
        String itemName = MessageManager.getMessage("Buy-Ammo-Description");
        itemName = itemName.replace("%amount%", String.valueOf(type.getResultAmmoAmount()));
        itemName = itemName.replace("%price%", String.valueOf(type.getAmmoPrice()));
        itemName = itemName.replace("%gadgetname%", type.getName());
        ItemStack display = ItemFactory.create(type.getMaterial(), itemName);
        PurchaseData pd = new PurchaseData();
        MenuGadgets mg = (MenuGadgets) categoryMenus.get(Category.GADGETS);
        pd.setPrice(type.getAmmoPrice());
        pd.setShowcaseItem(display);
        pd.setOnPurchase(() -> {
            player.addAmmo(type, type.getResultAmmoAmount());
            mg.open(player, player.getGadgetsPage());
            player.setGadgetsPage(1);
        });
        pd.setOnCancel(() -> {
            mg.open(player, player.getGadgetsPage());
            player.setGadgetsPage(1);
        });
        MenuPurchase mp = new MenuPurchase(ultraCosmetics, MessageManager.getMessage("Menu.Buy-Ammo.Title"), pd);
        player.getBukkitPlayer().openInventory(mp.getInventory(player));
    }
}
