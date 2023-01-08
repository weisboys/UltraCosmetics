package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.VersionManager;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.cryptomorin.xseries.XMaterial;

/**
 * Represents an instance of crushed candy cane particles summoned by a player.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class ParticleEffectCrushedCandyCane extends ParticleEffect {

    private int step;

    public ParticleEffectCrushedCandyCane(UltraPlayer owner, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);

        this.alternativeEffect = true;
    }

    @Override
    public void onUpdate() {
        if (step > 360) {
            step = 0;
        }
        Location center = getPlayer().getEyeLocation().add(0, 0.6, 0);
        double inc = (2 * Math.PI) / 20;
        double angle = step * inc;
        double x = Math.cos(angle) * 1.1f;
        double z = Math.sin(angle) * 1.1f;
        center.add(x, 0, z);
        showParticlesAt(center);
        step++;
    }

    private void showParticlesAt(Location loc) {
        if (VersionManager.IS_VERSION_1_13) {
            for (int i = 0; i < getModifiedAmount(15); i++) {
                getPlayer().getLocation().getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 1, 0.2, 0.2, 0.2, 0, ItemFactory.getRandomDye());
            }
        } else {
            for (int i = 0; i < getModifiedAmount(15); i++) {
                Particles.ITEM_CRACK.display(new Particles.ItemData(XMaterial.INK_SAC.parseMaterial(), getRandomColor()), 0.2f, 0.2f, 0.2f, 0, 1, loc, 128);
            }
        }
    }

    @Override
    public void showAlternativeEffect() {
        showParticlesAt(getPlayer().getLocation().add(0, 0.2, 0));
    }

    public static byte getRandomColor() {
        int random = RANDOM.nextInt(100);
        if (random > 98) {
            return (byte) 2;
        } else if (random > 49) {
            return (byte) 1;
        } else {
            return (byte) 15;
        }
    }
}
