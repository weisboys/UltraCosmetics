package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;

/**
 * Represents an instance of a dog pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetDog extends Pet {
    private boolean fixedColor = false;

    public PetDog(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!fixedColor) {
            ((Wolf) entity).setCollarColor(DyeColor.values()[RANDOM.nextInt(16)]);
        }
    }

    @Override
    public boolean customize(String customization) {
        fixedColor = enumCustomize(DyeColor.class, customization, ((Wolf) entity)::setCollarColor);
        return fixedColor;
    }
}
