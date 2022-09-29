package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

/**
 * Represents an instance of a shulker pet summoned by a player.
 *
 * @author Chris6ix
 * @since 28-09-2022
 */
public class PetShulker extends Pet {
    public PetShulker(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }
}
