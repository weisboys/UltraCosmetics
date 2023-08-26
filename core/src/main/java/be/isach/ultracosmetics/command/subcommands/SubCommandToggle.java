package be.isach.ultracosmetics.command.subcommands;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.command.SubCommand;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.player.UltraPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Clear {@link be.isach.ultracosmetics.command.SubCommand SubCommand}.
 *
 * @author iSach
 * @author RadBuilder
 * @since 12-21-2015
 */
public class SubCommandToggle extends SubCommand {
    private static final String ERROR_PREFIX = " " + ChatColor.RED + ChatColor.BOLD;

    public SubCommandToggle(UltraCosmetics ultraCosmetics) {
        super("toggle", "Toggles a cosmetic.", "<type> <cosmetic> [player]", ultraCosmetics, true);
    }

    @Override
    protected void onExePlayer(Player sender, String[] args) {
        if (args.length < 3 || args.length > 4) {
            badUsage(sender);
            return;
        }

        Player target;
        if (args.length > 3) {
            // null check later
            target = Bukkit.getPlayer(args[3]);
        } else {
            target = sender;
        }

        toggle(sender, target, args[1].toLowerCase(), args[2].toLowerCase());
    }

    @Override
    protected void onExeAnyone(CommandSender sender, String[] args) {
        if (args.length != 4) {
            badUsage(sender, "/uc toggle <type> <cosmetic> <player>");
            return;
        }

        toggle(sender, Bukkit.getPlayer(args[3]), args[1].toLowerCase(), args[2].toLowerCase());
    }

    private void toggle(CommandSender sender, Player targetPlayer, String type, String cosm) {
        if (sender != targetPlayer && !sender.hasPermission(getPermission().getName() + ".others")) {
            error(sender, "You do not have permission to toggle cosmetics for others.");
            return;
        }

        UltraPlayer target = ultraCosmetics.getPlayerManager().getUltraPlayer(targetPlayer);
        if (target == null) {
            error(sender, "Invalid player.");
            return;
        }

        if (!SettingsManager.isAllowedWorld(target.getBukkitPlayer().getWorld())) {
            MessageManager.send(sender, "World-Disabled");
            return;
        }

        Optional<Category> categories = Arrays.stream(Category.values()).filter(category -> category.isEnabled() && category.toString().toLowerCase().startsWith(type)).findFirst();
        if (!categories.isPresent()) {
            error(sender, "Invalid category.");
            return;
        }
        Category category = categories.get();
        CosmeticType<?> matchingType = findCosmetic(category, cosm);
        if (matchingType == null) {
            error(sender, "Invalid cosmetic.");
            return;
        }
        if (target.getCosmetic(category) != null && matchingType == target.getCosmetic(category).getType()) {
            target.removeCosmetic(category);
        } else {
            matchingType.equip(target, ultraCosmetics);
        }
    }

    private CosmeticType<?> findCosmetic(Category category, String partialName) {
        for (CosmeticType<?> type : category.getEnabled()) {
            if (type.toString().equalsIgnoreCase(partialName)) {
                return type;
            }
        }
        for (CosmeticType<?> type : category.getEnabled()) {
            if (type.toString().startsWith(partialName.toUpperCase())) {
                return type;
            }
        }
        return null;
    }

    @Override
    protected void tabComplete(CommandSender sender, String[] args, List<String> options) {
        if (args.length == 2) {
            addCategories(options);
        } else if (args.length == 3) {
            Category cat = Category.fromString(args[1]);

            if (cat == null || !cat.isEnabled()) return;

            for (CosmeticType<?> cosm : cat.getEnabled()) {
                options.add(cosm.toString());
            }
        } else if (args.length == 4) {
            addPlayers(options);
        }
    }
}
