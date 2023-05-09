package be.isach.ultracosmetics.menu.buttons;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CosmeticNoPermissionButton extends CosmeticButton {
    private final String name = SettingsManager.getConfig().getString("No-Permission.Custom-Item.Name");
    private final List<String> lore = SettingsManager.getConfig().getStringList("No-Permission.Custom-Item.Lore");
    private final ItemStack stack = ItemFactory.getItemStackFromConfig("No-Permission.Custom-Item.Type");

    public CosmeticNoPermissionButton(UltraCosmetics ultraCosmetics, CosmeticType<?> cosmeticType) {
        super(ultraCosmetics, cosmeticType, true);
    }

    @Override
    public ItemStack getBaseItem(UltraPlayer ultraPlayer) {
        ItemStack stack = this.stack.clone();
        String name = ChatColor.translateAlternateColorCodes('&', this.name).replace("{cosmetic-name}", cosmeticType.getName());
        ItemFactory.rename(stack, name);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String line : this.lore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
