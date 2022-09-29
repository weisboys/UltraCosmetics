package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ServerVersion;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Axolotl.Variant;

/**
 * Represents an instance of an axolotl pet summoned by a player.
 *
 * @author Chris6ix
 * @since 14-01-2022
 */
public class PetAxolotl extends Pet {
    public PetAxolotl(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        // For some strange reason, an axolotl has a default movement speed of 1.0, which is higher
        // than the default speed of every other entity except dolphin.
        if (!SettingsManager.getConfig().getBoolean("Pets.Axolotl.Fast") && UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_9)) {
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6);
        }
    }

    @Override
    public boolean customize(String customization) {
        Variant variant;
        try {
            variant = Variant.valueOf(customization.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        ((Axolotl) entity).setVariant(variant);
        return true;
    }
}
