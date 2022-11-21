package be.isach.ultracosmetics.menu.menus;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.menu.ClickRunnable;
import be.isach.ultracosmetics.menu.CosmeticMenu;
import be.isach.ultracosmetics.menu.PurchaseData;
import be.isach.ultracosmetics.mysql.MySqlConnectionManager;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import net.wesjd.anvilgui.AnvilGUI;

/**
 * Pet {@link be.isach.ultracosmetics.menu.Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuPets extends CosmeticMenu<PetType> {

    public MenuPets(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.PETS);
    }

    @Override
    protected void putItems(Inventory inventory, UltraPlayer ultraPlayer, int page) {
        addPetRenameItem(inventory, ultraPlayer);
    }

    private void addPetRenameItem(Inventory inventory, UltraPlayer player) {
        if (SettingsManager.getConfig().getBoolean("Pets-Rename.Enabled")) {
            ItemStack stack;
            int slot = inventory.getSize() - (getCategory().hasGoBackArrow() ? 4 : 6);
            ClickRunnable run;
            if (SettingsManager.getConfig().getBoolean("Pets-Rename.Permission-Required")
                    && !player.getBukkitPlayer().hasPermission("ultracosmetics.pets.rename")) {
                return;
            }
            if (player.getCurrentPet() != null) {
                stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Menu.Rename-Pet.Button.Name").replace("%petname%", player.getCurrentPet().getType().getName()));
                run = data -> renamePet(player);
            } else {
                stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Active-Pet-Needed"));
                run = data -> {
                    player.getBukkitPlayer().sendMessage(MessageManager.getMessage("Active-Pet-Needed"));
                    player.getBukkitPlayer().closeInventory();
                };
            }
            putItem(inventory, slot, stack, run);
        }
    }

    public void renamePet(final UltraPlayer ultraPlayer) {
        new AnvilGUI.Builder().plugin(ultraCosmetics)
                .itemLeft(XMaterial.PAPER.parseItem())
                .text(MessageManager.getMessage("Menu.Rename-Pet.Placeholder"))
                .title(MessageManager.getMessage("Menu.Rename-Pet.Title"))
                .onComplete((Player player, String text) -> {
                    if (text.length() > MySqlConnectionManager.MAX_NAME_SIZE) {
                        return AnvilGUI.Response.text(MessageManager.getMessage("Too-Long"));
                    }
                    if (!text.isEmpty() && ultraCosmetics.getEconomyHandler().isUsingEconomy()
                            && SettingsManager.getConfig().getBoolean("Pets-Rename.Requires-Money.Enabled")) {
                        return AnvilGUI.Response.openInventory(buyRenamePet(ultraPlayer, text, this));
                    } else {
                        ultraPlayer.setPetName(ultraPlayer.getCurrentPet().getType(), text);
                        return AnvilGUI.Response.close();
                    }
                }).open(ultraPlayer.getBukkitPlayer());
    }

    @Override
    protected void filterItem(ItemStack itemStack, PetType cosmeticType, UltraPlayer player) {
        if (player.getPetName(cosmeticType) != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemMeta.getDisplayName() + ChatColor.GRAY + " (" + player.getPetName(cosmeticType) + ChatColor.GRAY + ")");
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static Inventory buyRenamePet(UltraPlayer ultraPlayer, final String name, MenuPets returnMenu) {
        final String formattedName = UltraPlayer.colorizePetName(name);
        int price = SettingsManager.getConfig().getInt("Pets-Rename.Requires-Money.Price");
        ItemStack showcaseItem = ItemFactory.create(XMaterial.NAME_TAG, MessageManager.getMessage("Menu.Purchase-Rename.Button.Showcase")
                .replace("%price%", String.valueOf(price)).replace("%name%", formattedName));

        PurchaseData purchaseData = new PurchaseData();
        purchaseData.setPrice(price);
        purchaseData.setShowcaseItem(showcaseItem);
        purchaseData.setOnPurchase(() -> {
            ultraPlayer.setPetName(ultraPlayer.getCurrentPet().getType(), name);
            if (returnMenu != null) {
                returnMenu.open(ultraPlayer);
            }
        });
        if (returnMenu != null) {
            purchaseData.setOnCancel(() -> returnMenu.open(ultraPlayer));
        }

        MenuPurchase menu = new MenuPurchase(UltraCosmeticsData.get().getPlugin(), MessageManager.getMessage("Menu.Purchase-Rename.Title"), purchaseData);
        return menu.getInventory(ultraPlayer);
    }
}
