package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.Horse;

public class MountMule extends MountAbstractHorse {

    public MountMule(UltraPlayer ultraPlayer, MountType type, UltraCosmetics ultraCosmetics) {
        super(ultraPlayer, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.MULE;
    }
}
