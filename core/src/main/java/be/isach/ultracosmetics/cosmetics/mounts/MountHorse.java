package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.Horse;

public class MountHorse extends MountAbstractHorse {

    public MountHorse(UltraPlayer ultraPlayer, MountType type, UltraCosmetics ultraCosmetics) {
        super(ultraPlayer, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public Horse.Color getColor() {
        return Horse.Color.GRAY;
    }
}
