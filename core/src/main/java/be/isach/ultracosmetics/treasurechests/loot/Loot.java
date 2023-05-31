package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.treasurechests.TreasureChest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.ChatColor;

import java.util.concurrent.ThreadLocalRandom;

public interface Loot {
    public LootReward giveToPlayer(UltraPlayer player, TreasureChest chest);

    default int randomInRange(int min, int max) {
        if (min < max) {
            return ThreadLocalRandom.current().nextInt(max - min) + min;
        }
        return min;
    }

    default String getConfigMessage(String s) {
        String message = SettingsManager.getConfig().getString(s);
        if (message == null) {
            return ChatColor.RED.toString() + ChatColor.BOLD + "Error";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    default Component filterColors(Component component) {
        if (UltraCosmeticsData.get().arePlaceholdersColored()) {
            return component;
        }
        return component.style(Style.empty());
    }

    public default boolean isEmpty() {
        return false;
    }
}
