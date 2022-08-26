package be.isach.ultracosmetics.listeners;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.command.SubCommand;
import be.isach.ultracosmetics.command.subcommands.SubCommandTroubleshoot;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PriorityListener implements Listener {
    private final UltraCosmetics ultraCosmetics;
    private final SubCommandTroubleshoot troubleshoot;

    public PriorityListener(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
        SubCommandTroubleshoot search = null;
        for (SubCommand command : ultraCosmetics.getCommandManager().getCommands()) {
            if (command instanceof SubCommandTroubleshoot) {
                search = (SubCommandTroubleshoot) command;
                break;
            }
        }
        troubleshoot = search;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (ultraCosmetics.getProblems().size() > 0 && event.getPlayer().hasPermission(troubleshoot.getPermission())) {
            event.getPlayer().sendMessage(ChatColor.RED + "UltraCosmetics currently has " + ultraCosmetics.getProblems().size() + " problems, please run '/uc troubleshoot' to learn more.");
        }
    }
}
