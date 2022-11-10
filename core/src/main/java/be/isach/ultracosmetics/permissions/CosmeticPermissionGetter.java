package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.entity.Player;

public interface CosmeticPermissionGetter {
    public boolean hasPermission(Player player, CosmeticType<?> type);
}
