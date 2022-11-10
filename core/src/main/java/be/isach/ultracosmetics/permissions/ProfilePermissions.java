package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.player.UltraPlayerManager;

import org.bukkit.entity.Player;

import java.util.Set;

public class ProfilePermissions implements CosmeticPermissionGetter, CosmeticPermissionSetter {
    private final UltraPlayerManager pm;

    public ProfilePermissions(UltraCosmetics ultraCosmetics) {
        this.pm = ultraCosmetics.getPlayerManager();
    }

    @Override
    public void setPermissions(Player player, Set<CosmeticType<?>> types) {
        pm.getUltraPlayer(player).profileSetUnlocked(types);
    }

    @Override
    public void unsetPermissions(Player player, Set<CosmeticType<?>> types) {
        pm.getUltraPlayer(player).profileSetLocked(types);
    }

    @Override
    public boolean hasPermission(Player player, CosmeticType<?> type) {
        return pm.getUltraPlayer(player).profileHasUnlocked(type);
    }

    @Override
    public boolean isUnsetSupported() {
        return true;
    }
}
