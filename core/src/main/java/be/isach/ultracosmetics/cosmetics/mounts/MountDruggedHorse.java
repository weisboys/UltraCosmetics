package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sacha on 10/08/15.
 */
public class MountDruggedHorse extends MountAbstractHorse {
    private static final Particle ENTITY_EFFECT_PARTICLE = XParticle.ENTITY_EFFECT.get();
    private static final boolean SUPPORTS_ALPHA;
    private static final boolean SUPPORTS_COLOR_DATA;

    static {
        Color color = null;
        try {
            color = Color.fromARGB(0);
        } catch (NoSuchMethodError ignored) {
            // Handled below
        }
        SUPPORTS_ALPHA = color != null;
        SUPPORTS_COLOR_DATA = XParticle.ENTITY_EFFECT.get().getDataType() == Color.class;
    }

    private Player effectPlayer;

    public MountDruggedHorse(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        super.setupEntity();
        Horse horse = (Horse) getEntity();
        horse.setColor(Horse.Color.CHESTNUT);
        horse.setJumpStrength(1.3);

        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            getPlayer().addPotionEffect(new PotionEffect(XPotion.NAUSEA.getPotionEffectType(), 10000000, 1));
            effectPlayer = getPlayer();
        }, 1);
    }

    @Override
    public void onUpdate() {
        Location loc = entity.getLocation().add(0, 1, 0);
        Particles.FIREWORK.display(0.4f, 0.2f, 0.4f, loc, 5);
        Particles.EFFECT.display(0.4f, 0.2f, 0.4f, loc, 5);
        for (int i = 0; i < 5; i++) {
            coloredParticle(loc, null);
            coloredParticle(loc, Color.fromRGB(5, 255, 0));
        }
    }

    private void coloredParticle(Location loc, Color color) {
        if (color == null) {
            ThreadLocalRandom r = ThreadLocalRandom.current();
            if (SUPPORTS_ALPHA) {
                color = Color.fromARGB(Particles.AMBIENT_ALPHA, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            } else {
                color = Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
            }
        }
        if (SUPPORTS_COLOR_DATA) {
            entity.getWorld().spawnParticle(ENTITY_EFFECT_PARTICLE, loc, 0, 0.4, 0.2, 0.4, color);
        } else {
            entity.getWorld().spawnParticle(ENTITY_EFFECT_PARTICLE, loc, 0, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        }
    }

    @Override
    protected void onClear() {
        if (effectPlayer != null) {
            effectPlayer.removePotionEffect(XPotion.NAUSEA.getPotionEffectType());
        }
        super.onClear();
    }
}
