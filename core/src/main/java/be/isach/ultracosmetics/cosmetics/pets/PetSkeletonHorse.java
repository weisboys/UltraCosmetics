package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

/**
 * Represents an instance of a skeleton horse pet summoned by a player.
 *
 * @author Chris6ix
 * @since 06-09-2022
 */
public class PetSkeletonHorse extends Pet {
    public PetSkeletonHorse(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }
}
