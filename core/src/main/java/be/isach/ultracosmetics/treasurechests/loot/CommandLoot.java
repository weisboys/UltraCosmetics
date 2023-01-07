package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.treasurechests.CommandReward;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandLoot implements Loot {
    private final CommandReward reward;

    public CommandLoot(CommandReward reward) {
        this.reward = reward;
    }

    @Override
    public LootReward giveToPlayer(Player player) {
        for (String command : reward.getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%name%", player.getName()));
        }
        String[] name = new String[] { reward.getName().replace("%name%", player.getName()) };
        String message = ChatColor.translateAlternateColorCodes('&', reward.getMessage().replace("%prefix%", MessageManager.getMessage("Prefix")));

        return new LootReward(name, reward.getItemStack(), message, reward.getMessageEnabled(), true);
    }

}
