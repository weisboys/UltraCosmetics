package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.events.UCAmmoRewardEvent;
import be.isach.ultracosmetics.permissions.PermissionManager;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.treasurechests.TreasureChest;
import be.isach.ultracosmetics.util.WeightedSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AmmoLoot implements Loot {
    private final WeightedSet<CosmeticType<?>> types = new WeightedSet<>();

    public AmmoLoot(Player player) {
        for (CosmeticType<?> type : CosmeticType.enabledOf(Category.GADGETS)) {
            PermissionManager pm = UltraCosmeticsData.get().getPlugin().getPermissionManager();
            if (type.isEnabled() && ((GadgetType) type).requiresAmmo() && type.canBeFound() && pm.hasPermission(player, type)) {
                types.add(type, type.getChestWeight());
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return types.size() == 0;
    }

    public void add(GadgetType type) {
        types.add(type, type.getChestWeight());
    }

    @Override
    public LootReward giveToPlayer(UltraPlayer player, TreasureChest chest) {
        GadgetType g = (GadgetType) types.getRandom();
        int ammoMin = SettingsManager.getConfig().getInt("TreasureChests.Loots.Gadgets-Ammo.Min");
        int ammoMax = SettingsManager.getConfig().getInt("TreasureChests.Loots.Gadgets-Ammo.Max");
        int ammo = randomInRange(ammoMin, ammoMax);

        UCAmmoRewardEvent event = new UCAmmoRewardEvent(player, chest, this, g, ammo);
        Bukkit.getPluginManager().callEvent(event);
        ammo = event.getAmmo();

        String[] name = MessageManager.getMessage("Treasure-Chests-Loot.Ammo").replace("%cosmetic%", g.getName()).replace("%ammo%", String.valueOf(ammo)).split("\n");

        player.addAmmo(g, ammo);
        // if the player received more than half of what they could have, send a firework
        boolean firework = ammo > (ammoMax - ammoMin) / 2 + ammoMin;
        boolean toOthers = SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Gadgets-Ammo.Message.enabled");
        String message = getConfigMessage("TreasureChests.Loots.Gadgets-Ammo.Message.message").replace("%ammo%", String.valueOf(ammo))
                .replace("%cosmetic%", (UltraCosmeticsData.get().arePlaceholdersColored()) ? g.getName() : ChatColor.stripColor(g.getName()));
        return new LootReward(name, g.getItemStack(), message, toOthers, firework, g);
    }

}
