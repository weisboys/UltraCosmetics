package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.entity.Player;

public class MySQLPermissions implements CosmeticPermissionGetter, CosmeticPermissionSetter {

    public MySQLPermissions() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setPermission(Player player, CosmeticType<?> cosmeticType) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasPermission(Player player, CosmeticType<?> type) {
        // TODO Auto-generated method stub
        return false;
    }

}
