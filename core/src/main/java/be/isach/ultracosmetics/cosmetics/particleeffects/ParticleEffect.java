package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.Cosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.Bukkit;

/**
 * Represents an instance of a particle effect summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public abstract class ParticleEffect extends Cosmetic<ParticleEffectType> implements Updatable {

    /**
     * If false, no particle effects will appear when the player is moving.
     */
    protected boolean displayIfPlayerMoves = true;
    /**
     * If true, display an alternative effect when the player is moving.
     */
    protected boolean alternativeEffect = false;

    public ParticleEffect(UltraPlayer ultraPlayer, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(ultraPlayer, type, ultraCosmetics);
    }

    @Override
    protected void onEquip() {
    }

    @Override
    protected void scheduleTask() {
        runTaskTimerAsynchronously(getUltraCosmetics(), 0, getType().getRepeatDelay());
    }

    @Override
    public void run() {
        try {
            if (Bukkit.getPlayer(getOwnerUniqueId()) != null && getOwner().getCurrentParticleEffect() == this) {
                // If the player is not moving, display the default particle effect.
                if (!isMoving()) {
                    onUpdate();
                } else {
                    // If the player is moving:
                    if (displayIfPlayerMoves) {
                        if (!alternativeEffect) {
                            // Display default particle effect.
                            onUpdate();
                        } else {
                            // Display the alternative effect.
                            showAlternativeEffect();
                        }
                    }
                }
            } else {
                cancel();
            }
        } catch (NullPointerException exc) {
            exc.printStackTrace();
            clear();
            cancel();
        }
    }

    protected boolean isMoving() {
        return getOwner().isMoving();
    }

    protected int getModifiedAmount(int originalAmount) {
        // always return at least 1 so the particles work
        return Integer.max((int) (originalAmount * getType().getParticleMultiplier()), 1);
    }

    public void showAlternativeEffect() {
        getType().getEffect().display(0.4f, 0.3f, 0.4f, getPlayer().getLocation().add(0, 1, 0), getModifiedAmount(3));
    }
}
