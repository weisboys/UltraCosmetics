package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.entity.Player;

public interface CosmeticPermissionSetter {
    public void setPermission(Player player, CosmeticType<?> cosmeticType);
}
