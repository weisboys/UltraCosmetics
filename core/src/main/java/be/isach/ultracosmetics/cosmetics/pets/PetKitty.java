package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;

/**
 * Represents an instance of a kitten pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetKitty extends Pet {

    public PetKitty(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public boolean customize(String customization) {
        Type type;
        try {
            type = Type.valueOf(customization.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        ((Cat) entity).setCatType(type);
        return true;
    }
}
