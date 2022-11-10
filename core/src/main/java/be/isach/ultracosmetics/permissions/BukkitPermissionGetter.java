package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.entity.Player;

public class BukkitPermissionGetter implements CosmeticPermissionGetter, RawPermissionGetter {

    @Override
    public boolean hasRawPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Player player, CosmeticType<?> type) {
        return hasRawPermission(player, type.getPermission().getName());
    }

}
