package be.isach.ultracosmetics.menu.buttons;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.menu.Button;
import be.isach.ultracosmetics.menu.ClickData;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandButton implements Button {
    private final ItemStack stack;
    private final String command;
    private final boolean closeAfterClick;

    public CommandButton(ItemStack stack, String command, boolean closeAfterClick) {
        this.stack = stack;
        this.command = command;
        this.closeAfterClick = closeAfterClick;
    }

    @Override
    public ItemStack getDisplayItem(UltraPlayer ultraPlayer) {
        return stack;
    }

    @Override
    public void onClick(ClickData clickData) {
        if (closeAfterClick) {
            clickData.getClicker().getBukkitPlayer().closeInventory();
        }
        if (command == null) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", clickData.getClicker().getBukkitPlayer().getName()));
    }

    public static CommandButton deserialize(ConfigurationSection section, UltraCosmetics ultraCosmetics) {
        XMaterial xmat = XMaterial.matchXMaterial(section.getString("Material")).orElse(null);
        if (xmat == null) {
            throw new IllegalArgumentException("Invalid item for button: '" + section.getString("Material") + "'");
        }
        ItemStack stack = xmat.parseItem();
        int amount = section.getInt("Amount", 1);
        if (amount < 1 || amount > 64) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
        stack.setAmount(amount);
        MiniMessage mm = MessageManager.getMiniMessage();
        if (section.isString("Name")) {
            String name = MessageManager.toLegacy(mm.deserialize(section.getString("Name")));
            ItemFactory.rename(stack, name);
        }
        ItemMeta meta = stack.getItemMeta();
        boolean loreIsString = section.isString("Lore");
        boolean loreIsList = section.isList("Lore");
        if (loreIsString || loreIsList) {
            List<String> lore = new ArrayList<>();
            if (loreIsString) {
                for (String line : section.getString("Lore").split("\n")) {
                    lore.add(MessageManager.toLegacy(mm.deserialize(line)));
                }
            } else {
                for (String line : section.getStringList("Lore")) {
                    lore.add(MessageManager.toLegacy(mm.deserialize(line)));
                }
            }
            meta.setLore(lore);
        }
        if (UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_14) && section.isInt("CustomModelData")) {
            meta.setCustomModelData(section.getInt("CustomModelData"));
        }
        stack.setItemMeta(meta);
        boolean closeAfterClick = section.getBoolean("CloseAfterClick");
        String command = section.getString("Command");
        return new CommandButton(stack, command, closeAfterClick);
    }
}
