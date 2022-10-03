package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.Strider;

/**
 * Represents an instance of a strider pet summoned by a player.
 *
 * @author Chris6ix
 * @since 07-09-2022
 */
public class PetStrider extends Pet {
    private boolean shivering = false;

    public PetStrider(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        ((Strider) entity).setShivering(shivering);
    }

    @Override
    public boolean customize(String customization) {
        shivering = customization.equalsIgnoreCase("true");
        return true;
    }
}
