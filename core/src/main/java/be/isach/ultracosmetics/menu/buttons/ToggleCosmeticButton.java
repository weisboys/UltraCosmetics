package be.isach.ultracosmetics.menu.buttons;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ToggleCosmeticButton extends CosmeticButton {
    private final boolean showPermissionInLore = SettingsManager.getConfig().getBoolean("No-Permission.Show-In-Lore");
    private final String permissionYes = SettingsManager.getConfig().getString("No-Permission.Lore-Message-Yes");
    private final String permissionNo = SettingsManager.getConfig().getString("No-Permission.Lore-Message-No");
    private final String permissionShowroom = SettingsManager.getConfig().getString("No-Permission.Lore-Message-Showroom");

    public ToggleCosmeticButton(UltraCosmetics ultraCosmetics, CosmeticType<?> cosmeticType) {
        super(ultraCosmetics, cosmeticType, false);
    }

    @Override
    protected ItemStack getBaseItem(UltraPlayer ultraPlayer) {
        Component toggle = cosmeticType.getCategory().getActivateTooltip();

        boolean deactivate = ultraPlayer.hasCosmetic(cosmeticType.getCategory()) && ultraPlayer.getCosmetic(cosmeticType.getCategory()).getType() == cosmeticType;

        if (deactivate) {
            toggle = cosmeticType.getCategory().getDeactivateTooltip();
        }
        ItemStack stack = ItemFactory.rename(cosmeticType.getItemStack(), toggle.appendSpace().append(cosmeticType.getName()));
        if (deactivate) {
            ItemFactory.addGlow(stack);
        }
        ItemMeta meta = stack.getItemMeta();
        List<String> loreList = new ArrayList<>();
        if (cosmeticType.showsDescription()) {
            loreList.add("");
            loreList.addAll(cosmeticType.getDescription());
        }

        if (showPermissionInLore) {
            loreList.add("");
            String permissionLore;
            if (ultraCosmetics.getPermissionManager().hasPermission(ultraPlayer, cosmeticType)) {
                permissionLore = permissionYes;
            } else if (ultraCosmetics.getWorldGuardManager().isInShowroom(ultraPlayer.getBukkitPlayer())) {
                permissionLore = permissionShowroom;
            } else {
                permissionLore = permissionNo;
            }
            loreList.add(ChatColor.translateAlternateColorCodes('&', permissionLore));
        }
        meta.setLore(loreList);
        stack.setItemMeta(meta);
        return stack;
    }
}
