package be.isach.ultracosmetics.treasurechests;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.treasurechests.loot.AmmoLoot;
import be.isach.ultracosmetics.treasurechests.loot.CommandLoot;
import be.isach.ultracosmetics.treasurechests.loot.CosmeticLoot;
import be.isach.ultracosmetics.treasurechests.loot.Loot;
import be.isach.ultracosmetics.treasurechests.loot.LootReward;
import be.isach.ultracosmetics.treasurechests.loot.MoneyLoot;
import be.isach.ultracosmetics.treasurechests.loot.NothingLoot;
import be.isach.ultracosmetics.util.EntitySpawner;
import be.isach.ultracosmetics.util.WeightedSet;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sacha on 19/08/15.
 */
public class TreasureRandomizer {
    private static final int MONEY_CHANCE = SettingsManager.getConfig().getInt("TreasureChests.Loots.Money.Chance");
    private static final int AMMO_CHANCE = SettingsManager.getConfig().getInt("TreasureChests.Loots.Gadgets-Ammo.Chance");
    private final WeightedSet<Loot> lootTypes = new WeightedSet<>();
    private Location loc;
    private final Player player;
    private final boolean forceMessageToOwner;
    private final MoneyLoot money = new MoneyLoot();
    private final NothingLoot nothing = new NothingLoot();
    private final AmmoLoot ammo;
    private final boolean canAddAmmo;

    public TreasureRandomizer(final Player player, Location location, boolean forceMessageToOwner) {
        this.loc = location.add(0.5, 0, 0.5);
        this.player = player;
        this.forceMessageToOwner = forceMessageToOwner;
        // add ammo.
        canAddAmmo = Category.GADGETS.isEnabled() && UltraCosmeticsData.get().isAmmoEnabled()
                && SettingsManager.getConfig().getBoolean("TreasureChests.Loots.Gadgets-Ammo.Enabled");
        ammo = new AmmoLoot(player);
        if (!ammo.isEmpty() && canAddAmmo) {
            lootTypes.add(ammo, AMMO_CHANCE);
        }
        CustomConfiguration config = UltraCosmeticsData.get().getPlugin().getConfig();
        for (String key : config.getConfigurationSection("TreasureChests.Loots.Commands").getKeys(false)) {
            String path = "TreasureChests.Loots.Commands." + key;
            if (config.getBoolean(path + ".Enabled")) {
                String cancelPermission = config.getString(path + ".Cancel-If-Permission");
                if (cancelPermission.equals("no") || !player.hasPermission(cancelPermission)) {
                    CommandReward reward = new CommandReward(path);
                    lootTypes.add(new CommandLoot(reward), reward.getChance());
                }
            }
        }

        for (Category cat : Category.values()) {
            setupChance(cat);
        }

        if (UltraCosmeticsData.get().useMoneyTreasureLoot()) {
            lootTypes.add(money, MONEY_CHANCE);
        }
    }

    public TreasureRandomizer(final Player player, Location location) {
        this(player, location, false);
    }

    private void setupChance(Category category) {
        String configPath = "TreasureChests.Loots." + category.getConfigPath();
        if (!SettingsManager.getConfig().getBoolean(configPath + ".Enabled")) return;
        if (!category.isEnabled()) return;
        int chance = SettingsManager.getConfig().getInt(configPath + ".Chance");
        if (chance <= 0) return;
        CosmeticLoot loot = new CosmeticLoot(category, player);
        if (!loot.isEmpty()) {
            lootTypes.add(loot, chance);
        }
    }

    private static Color randomColor() {
        Random r = ThreadLocalRandom.current();
        return Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }

    public LootReward giveRandomThing(TreasureChest chest) {
        UltraPlayer ultraPlayer = UltraCosmeticsData.get().getPlugin().getPlayerManager().getUltraPlayer(player);
        XSound.BLOCK_CHEST_OPEN.play(loc, 1.4f, 1.5f);
        if (lootTypes.size() == 0) {
            return giveFallback(ultraPlayer, chest);
        }
        Loot loot = lootTypes.getRandom();
        LootReward reward = loot.giveToPlayer(ultraPlayer, chest);
        broadcast(reward.getMessage(), reward.isBroadcast());
        if (reward.isFirework()) {
            spawnFirework();
        }
        if (loot.isEmpty()) {
            lootTypes.remove(loot);
        }
        if (loot instanceof CosmeticLoot && ((CosmeticLoot) loot).getCategory() == Category.GADGETS) {
            ammo.add((GadgetType) reward.getCosmetic());
            if (canAddAmmo && !lootTypes.contains(ammo)) {
                lootTypes.add(ammo, AMMO_CHANCE);
            }
        }

        return reward;
    }

    private LootReward giveFallback(UltraPlayer player, TreasureChest chest) {
        if (UltraCosmeticsData.get().getPlugin().getEconomyHandler().isUsingEconomy()) {
            return money.giveToPlayer(player, chest);
        }
        return nothing.giveToPlayer(player, chest);
    }

    public void spawnFirework() {
        EntitySpawner.spawnFireworks(loc.clone().add(0.5, 0, 0.5), randomColor(), randomColor(), FireworkEffect.Type.BALL);
    }

    private void broadcast(String message, boolean toOthers) {
        if (message == null || message.isEmpty()) return;
        UltraCosmetics ultraCosmetics = UltraCosmeticsData.get().getPlugin();
        message = message.replace("%name%", player.getName());
        if (ultraCosmetics.getDiscordHook() != null) {
            ultraCosmetics.getDiscordHook().sendLootMessage(player, message.replace("%prefix%", ""));
        }
        message = message.replace("%prefix%", MessageManager.getMessage("Prefix"));
        if (!toOthers) {
            if (forceMessageToOwner) {
                player.sendMessage(message);
            }
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == this.player || (SettingsManager.isAllowedWorld(player.getWorld())
                    && ultraCosmetics.getPlayerManager().getUltraPlayer(player).isTreasureNotifying())) {
                player.sendMessage(message);
            }
        }
    }

    public void setLocation(Location newLoc) {
        loc = newLoc;
    }
}
