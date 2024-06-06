package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sacha on 10/08/15.
 */
public class MountDruggedHorse extends MountAbstractHorse {

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
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10000000, 1));
            effectPlayer = getPlayer();
        }, 1);
    }

    @Override
    public void onUpdate() {
        Location loc = entity.getLocation().add(0, 1, 0);
        Particles.FIREWORK.display(0.4f, 0.2f, 0.4f, loc, 5);
        Particles.EFFECT.display(0.4f, 0.2f, 0.4f, loc, 5);
        Particle entityEffectParticle = XParticle.ENTITY_EFFECT.get();
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            Color color = Color.fromARGB(Particles.AMBIENT_ALPHA, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            entity.getWorld().spawnParticle(entityEffectParticle, loc, 1, 0.4f, 0.2f, 0.4f, color);
        }
        entity.getWorld().spawnParticle(entityEffectParticle, loc, 5, 0.4f, 0.2f, 0.4f, Color.fromRGB(5, 255, 0));
    }

    @Override
    protected void onClear() {
        if (effectPlayer != null) {
            effectPlayer.removePotionEffect(PotionEffectType.CONFUSION);
        }
        super.onClear();
    }
}
