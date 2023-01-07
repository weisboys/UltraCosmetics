package be.isach.ultracosmetics.treasurechests.loot;

import be.isach.ultracosmetics.config.MessageManager;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

public class NothingLoot implements Loot {

    @Override
    public LootReward giveToPlayer(Player player) {
        String[] name = MessageManager.getMessage("Treasure-Chests-Loot.Nothing").split("\n");
        return new LootReward(name, XMaterial.BARREL.parseItem(), null, false, false);
    }

}
