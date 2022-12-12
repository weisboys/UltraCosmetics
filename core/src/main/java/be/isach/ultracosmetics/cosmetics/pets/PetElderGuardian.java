package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.potion.PotionEffectType;

/**
 * Represents an instance of a elder guardian pet summoned by a player.
 *
 * @author Chris6ix
 * @since 15-09-2022
 */
public class PetElderGuardian extends Pet {
    public PetElderGuardian(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @EventHandler
    public void onFatigue(EntityPotionEffectEvent event) {
        if (event.getEntity() == getPlayer() && event.getCause() == Cause.ATTACK && event.getNewEffect().getType().equals(PotionEffectType.SLOW_DIGGING)
                && SettingsManager.getConfig().getBoolean(getOptionPath("Block-Effect"))) {
            event.setCancelled(true);
        }
    }
}
