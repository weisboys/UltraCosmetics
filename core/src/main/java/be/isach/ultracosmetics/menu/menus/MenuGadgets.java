package be.isach.ultracosmetics.menu.menus;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.menu.CosmeticMenu;
import be.isach.ultracosmetics.menu.buttons.ToggleGadgetsButton;
import be.isach.ultracosmetics.player.UltraPlayer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Gadget {@link be.isach.ultracosmetics.menu.Menu Menu}.
 *
 * @author iSach
 * @since 07-23-2016
 */
public class MenuGadgets extends CosmeticMenu<GadgetType> {

    public MenuGadgets(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.GADGETS);
    }

    @Override
    protected void putItems(Inventory inventory, UltraPlayer player, int page) {
        if (SettingsManager.getConfig().getBoolean("Categories.Gadgets.Allow-Disable-Gadgets", true)) {
            int slot = inventory.getSize() - (getCategory().hasGoBackArrow() ? 4 : 6);
            putItem(inventory, slot, new ToggleGadgetsButton(), player);
        }
    }

    @Override
    protected void filterItem(ItemStack itemStack, GadgetType gadgetType, UltraPlayer player) {
        if (!UltraCosmeticsData.get().isAmmoEnabled() || !gadgetType.requiresAmmo() || !player.canEquip(gadgetType)) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreList = itemMeta.getLore();

        loreList.add("");
        int ammo = player.getAmmo(gadgetType);
        loreList.add(MessageManager.getLegacyMessage("Ammo", Placeholder.unparsed("ammo", String.valueOf(ammo))));
        if (ultraCosmetics.getEconomyHandler().isUsingEconomy()) {
            loreList.add(MessageManager.getLegacyMessage("Right-Click-Buy-Ammo"));
        }

        if (SettingsManager.getConfig().getBoolean("Ammo-System-For-Gadgets.Show-Ammo-In-Menu-As-Item-Amount")
                && !(player.getCurrentGadget() != null
                && player.getCurrentGadget().getType() == gadgetType)) {
            itemStack.setAmount(Math.max(1, Math.min(64, ammo)));
        }
        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
    }


}
