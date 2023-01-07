package be.isach.ultracosmetics.menu.menus;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.SuitCategory;
import be.isach.ultracosmetics.menu.Menu;
import be.isach.ultracosmetics.permissions.PermissionManager;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.treasurechests.TreasureChestManager;
import be.isach.ultracosmetics.treasurechests.TreasureRandomizer;
import be.isach.ultracosmetics.util.ItemFactory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import java.util.ArrayList;
import java.util.List;

/**
 * Main {@link be.isach.ultracosmetics.menu.Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuMain extends Menu {

    private int[] layout;

    public MenuMain(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics);

        switch (Category.enabledSize()) {
        case 10:
            layout = new int[] { 9, 11, 13, 15, 17, 27, 29, 31, 33, 35 };
            break;
        case 9:
            layout = new int[] { 9, 11, 13, 15, 17, 28, 30, 32, 34 };
            break;
        case 8:
            layout = new int[] { 10, 12, 14, 16, 28, 30, 32, 34 };
            break;
        case 7:
            layout = new int[] { 10, 13, 16, 28, 30, 32, 34 };
            break;
        case 6:
            layout = new int[] { 10, 13, 16, 28, 31, 34 };
            break;
        case 5:
            layout = new int[] { 10, 16, 22, 29, 33 };
            break;
        case 4:
            layout = new int[] { 19, 21, 23, 25 };
            break;
        case 3:
            layout = new int[] { 20, 22, 24 };
            break;
        case 2:
            layout = new int[] { 21, 23 };
            break;
        case 1:
            layout = new int[] { 22 };
            break;
        }

        if (UltraCosmeticsData.get().areTreasureChestsEnabled() && layout != null) {
            for (int i = 0; i < layout.length; i++) {
                layout[i] += 9;
            }
        }
    }

    @Override
    public void open(UltraPlayer player) {
        if (!UltraCosmeticsData.get().areTreasureChestsEnabled() && Category.enabledSize() == 1) {
            getUltraCosmetics().getMenus().getCategoryMenu(Category.enabled().get(0)).open(player);
            return;
        }
        super.open(player);
    }

    @Override
    protected void putItems(Inventory inventory, UltraPlayer player) {
        if (Category.enabledSize() > 0) {
            int i = 0;
            boolean foundSuits = false;
            PermissionManager pm = UltraCosmeticsData.get().getPlugin().getPermissionManager();
            Player bukkitPlayer = player.getBukkitPlayer();
            for (Category category : Category.enabled()) {
                // Avoid counting suits categories as different menu items
                if (category.isSuits()) {
                    if (foundSuits) continue;
                    foundSuits = true;
                }
                ItemStack stack = category.getItemStack();
                String lore = MessageManager.getMessage("Menu." + category.getConfigPath() + ".Button.Lore");
                if (lore.contains("%unlocked%")) {
                    lore = lore.replace("%unlocked%", calculateUnlocked(bukkitPlayer, category, pm));
                }
                List<String> loreList = new ArrayList<>();
                loreList.add("");
                for (String line : lore.split("\n")) {
                    loreList.add(line);
                }
                ItemMeta meta = stack.getItemMeta();
                meta.setLore(loreList);
                stack.setItemMeta(meta);
                putItem(inventory, layout[i++], stack, data -> {
                    getUltraCosmetics().getMenus().getCategoryMenu(category).open(player);
                });
            }
        }

        // Clear cosmetics item.
        String message = MessageManager.getMessage("Clear.Cosmetics");
        ItemStack itemStack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Clear-Cosmetic-Item"), message);
        putItem(inventory, inventory.getSize() - 5, itemStack, data -> {
            player.clear();
            open(player);
        });

        if (UltraCosmeticsData.get().areTreasureChestsEnabled()) {
            String msgChests = MessageManager.getMessage("Treasure-Chests");
            final boolean usingEconomy = getUltraCosmetics().getEconomyHandler().isUsingEconomy();
            boolean canBuyKeys = usingEconomy && SettingsManager.getConfig().getInt("TreasureChests.Key-Price") > 0;
            String buyKeyMessage = "";
            if (canBuyKeys) {
                buyKeyMessage = "\n" + MessageManager.getMessage("Click-Buy-Key") + "\n";
            }
            String[] chestLore;
            if (player.getKeys() < 1) {
                chestLore = new String[] { "", MessageManager.getMessage("Dont-Have-Key"), buyKeyMessage };
            } else {
                if (SettingsManager.getConfig().getString("TreasureChests.Mode", "").equalsIgnoreCase("both")) {
                    chestLore = new String[] { "", MessageManager.getMessage("Left-Click-Open-Chest"), MessageManager.getMessage("Right-Click-Simple"), "" };
                } else {
                    chestLore = new String[] { "", MessageManager.getMessage("Click-Open-Chest"), "" };
                }
            }
            ItemStack keys = ItemFactory.create(XMaterial.TRIPWIRE_HOOK, MessageManager.getMessage("Treasure-Keys"), "",
                    MessageManager.getMessage("Your-Keys").replace("%keys%", String.valueOf(player.getKeys())), buyKeyMessage);

            putItem(inventory, 5, keys, (data) -> {
                if (!canBuyKeys) {
                    XSound.BLOCK_ANVIL_LAND.play(player.getBukkitPlayer().getLocation(), 0.2f, 1.2f);
                    return;
                }
                player.getBukkitPlayer().closeInventory();
                player.openKeyPurchaseMenu();
            });

            ItemStack chest = ItemFactory.create(XMaterial.CHEST, msgChests, chestLore);
            putItem(inventory, 3, chest, (data) -> {
                Player p = player.getBukkitPlayer();
                if (!canBuyKeys && player.getKeys() < 1) {
                    XSound.BLOCK_ANVIL_LAND.play(p.getLocation(), 0.2f, 1.2f);
                    return;
                }
                String mode = SettingsManager.getConfig().getString("TreasureChests.Mode", "structure");
                if (mode.equalsIgnoreCase("both")) {
                    if (data.getClick().isRightClick()) {
                        mode = "simple";
                    } else {
                        mode = "structure";
                    }
                }
                if (player.getKeys() > 0 && mode.equalsIgnoreCase("simple")) {
                    player.removeKey();
                    int count = SettingsManager.getConfig().getInt("TreasureChests.Count", 4);
                    TreasureRandomizer tr = new TreasureRandomizer(p, p.getLocation().subtract(1, 0, 1), true);
                    for (int i = 0; i < count; i++) {
                        tr.giveRandomThing();
                    }
                    // Refresh with new key count
                    open(player);
                } else {
                    // Opens the buy-a-key menu if the player doesn't have enough keys
                    TreasureChestManager.tryOpenChest(p);
                }
            });

        }
    }

    private String calculateUnlocked(Player player, Category category, PermissionManager pm) {
        int unlocked = 0;
        int total = 0;
        if (category.isSuits()) {
            for (Category cat : Category.enabled()) {
                if (!cat.isSuits()) continue;
                unlocked += pm.getEnabledUnlocked(player, cat).size();
            }
            total = SuitCategory.enabled().size() * 4;
        } else {
            unlocked = pm.getEnabledUnlocked(player, category).size();
            total = category.getEnabled().size();
        }
        return unlocked + "/" + total;
    }

    @Override
    protected String getName() {
        return MessageManager.getMessage("Menu.Main.Title");
    }

    @Override
    protected int getSize() {
        return UltraCosmeticsData.get().areTreasureChestsEnabled() ? 54 : 45;
    }

    /**
     * Opens UC's main menu OR runs the custom main menu command specified in config.yml
     *
     * @param ultraPlayer The player to show the menu to
     */
    public static void openMainMenu(UltraPlayer ultraPlayer) {
        UltraCosmetics ultraCosmetics = UltraCosmeticsData.get().getPlugin();
        if (ultraCosmetics.getConfig().getBoolean("Categories.Back-To-Main-Menu-Custom-Command.Enabled")) {
            String command = ultraCosmetics.getConfig().getString("Categories.Back-To-Main-Menu-Custom-Command.Command").replace("/", "").replace("{player}", ultraPlayer.getBukkitPlayer().getName()).replace("{playeruuid}", ultraPlayer.getUUID().toString());
            Bukkit.dispatchCommand(ultraCosmetics.getServer().getConsoleSender(), command);
        } else {
            ultraCosmetics.getMenus().getMainMenu().open(ultraPlayer);
        }
    }
}
