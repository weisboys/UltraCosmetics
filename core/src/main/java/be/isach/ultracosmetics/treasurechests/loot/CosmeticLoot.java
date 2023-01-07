package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.permissions.PermissionManager;
import be.isach.ultracosmetics.util.WeightedSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CosmeticLoot implements Loot {
    private final Category category;
    private final WeightedSet<CosmeticType<?>> types = new WeightedSet<>();
    private final PermissionManager pm = UltraCosmeticsData.get().getPlugin().getPermissionManager();

    public CosmeticLoot(Category category, Player player) {
        this.category = category;
        for (CosmeticType<?> type : category.getEnabled()) {
            if (!type.isEnabled() || type.getChestWeight() < 1 || pm.hasPermission(player, type)) continue;
            types.add(type, type.getChestWeight());
        }
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public LootReward giveToPlayer(Player player) {
        CosmeticType<?> cosmetic = types.removeRandom();
        String catName = category.getConfigPath();
        String[] name = MessageManager.getMessage("Treasure-Chests-Loot." + catName).replace("%cosmetic%", cosmetic.getName()).split("\n");
        pm.setPermission(player, cosmetic);
        boolean toOthers = SettingsManager.getConfig().getBoolean("TreasureChests.Loots." + catName + ".Message.enabled");
        String message = getConfigMessage("TreasureChests.Loots." + catName + ".Message.message")
                .replace("%cosmetic%", UltraCosmeticsData.get().arePlaceholdersColored() ? cosmetic.getName() : ChatColor.stripColor(cosmetic.getName()));
        return new LootReward(name, cosmetic.getItemStack(), message, toOthers, true, cosmetic);
    }

    @Override
    public boolean isEmpty() {
        return types.size() == 0;
    }
}
