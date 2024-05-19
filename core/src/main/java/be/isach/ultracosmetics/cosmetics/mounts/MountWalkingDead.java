package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

/**
 * Created by sacha on 1/03/17.
 */
public class MountWalkingDead extends MountAbstractHorse {

    public MountWalkingDead(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        Particles.ENCHANTED_HIT.display(0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
        Particles.ENTITY_EFFECT_AMBIENT.display(0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
    }
}
