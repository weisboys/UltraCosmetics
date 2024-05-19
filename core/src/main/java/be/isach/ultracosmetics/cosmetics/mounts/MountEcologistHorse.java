package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import org.bukkit.entity.Horse;

/**
 * Represents an instance of an ecologist mount.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class MountEcologistHorse extends MountAbstractHorse {
    public MountEcologistHorse(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        super.setupEntity();
        ((Horse) entity).setColor(Horse.Color.CHESTNUT);
    }

    @Override
    public void onUpdate() {
        Particles.HAPPY_VILLAGER.display(0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
    }
}
