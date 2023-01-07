package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.config.SettingsManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public interface Loot {
    public LootReward giveToPlayer(Player player);

    default int randomInRange(int min, int max) {
        if (min < max) {
            return ThreadLocalRandom.current().nextInt(max - min) + min;
        }
        return min;
    }

    default String getConfigMessage(String s) {
        String message = SettingsManager.getConfig().getString(s);
        if (message == null) {
            return ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Error";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public default boolean isEmpty() {
        return false;
    }
}
