package be.isach.ultracosmetics.permissions;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Problem;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PermissionManager {
    private CosmeticPermissionGetter cosmeticGetter;
    private RawPermissionGetter rawGetter;
    private CosmeticPermissionSetter cosmeticSetter;
    private RawPermissionSetter rawSetter;

    public PermissionManager(UltraCosmetics ultraCosmetics) {
        CustomConfiguration config = SettingsManager.getConfig();
        if (!UltraCosmeticsData.get().usingFileStorage() && config.getBoolean("MySQL.Unlocked-Cosmetics.Enabled")) {
            ProfilePermissions mysql = new ProfilePermissions(ultraCosmetics);
            cosmeticGetter = mysql;
            cosmeticSetter = mysql;
        }
        if (config.getString("TreasureChests.Permission-Add-Command", "").startsWith("!lp-api")) {
            if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
                LuckPermsHook lp = new LuckPermsHook(ultraCosmetics);
                rawSetter = lp;
                if (cosmeticSetter == null) {
                    cosmeticSetter = lp;
                }
            } else {
                ultraCosmetics.getSmartLogger().write(LogLevel.WARNING, "Permission-Add-Command was set to '!lp-api' but LuckPerms is not present. Please change it manually.");
                config.set("TreasureChests.Permission-Add-Command", "say Please set Permission-Add-Command in UC config.yml");
            }
        }
        if (config.getBoolean("TreasureChests.Enabled") && config.getString("TreasureChests.Permission-Add-Command", "say ").startsWith("say ")) {
            ultraCosmetics.addProblem(Problem.PERMISSION_COMMAND_NOT_SET);
        }

        BukkitPermissionGetter bukkit = new BukkitPermissionGetter();
        PermissionCommand cmd = new PermissionCommand();
        if (cosmeticGetter == null) {
            cosmeticGetter = bukkit;
        }
        if (rawGetter == null) {
            rawGetter = bukkit;
        }
        if (cosmeticSetter == null) {
            cosmeticSetter = cmd;
        }
        if (rawSetter == null) {
            rawSetter = cmd;
        }
    }

    public boolean hasPermission(Player player, CosmeticType<?> type) {
        return cosmeticGetter.hasPermission(player, type);
    }

    public boolean hasPermission(UltraPlayer player, CosmeticType<?> type) {
        return hasPermission(player.getBukkitPlayer(), type);
    }

    public boolean hasRawPermission(Player player, String permission) {
        return rawGetter.hasRawPermission(player, permission);
    }

    public void setPermissions(Player player, Set<CosmeticType<?>> types) {
        cosmeticSetter.setPermissions(player, types);
    }

    public void unsetPermissions(Player player, Set<CosmeticType<?>> types) {
        cosmeticSetter.unsetPermissions(player, types);
    }

    public void setPermission(Player player, CosmeticType<?> type) {
        Set<CosmeticType<?>> types = new HashSet<>();
        types.add(type);
        setPermissions(player, types);
    }

    public void unsetPermission(Player player, CosmeticType<?> type) {
        Set<CosmeticType<?>> types = new HashSet<>();
        types.add(type);
        unsetPermissions(player, types);
    }

    public void setPermission(UltraPlayer player, CosmeticType<?> type) {
        setPermission(player.getBukkitPlayer(), type);
    }

    public void setRawPermission(Player player, String permission) {
        rawSetter.setRawPermission(player, permission);
    }

    public boolean isUsingSQL() {
        return cosmeticGetter instanceof ProfilePermissions;
    }
}
