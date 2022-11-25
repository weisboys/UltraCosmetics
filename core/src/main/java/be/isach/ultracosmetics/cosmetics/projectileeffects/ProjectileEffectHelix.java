package be.isach.ultracosmetics.cosmetics.projectileeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ProjectileEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class ProjectileEffectHelix extends ProjectileEffect {
    private static final Vector Y_AXIS = new Vector(0, 1, 0);
    private static final double STEP = Math.PI / 8;
    private double angle = 0;

    public ProjectileEffectHelix(UltraPlayer owner, ProjectileEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void showParticles(Projectile projectile) {
        angle += STEP;
        if (angle >= Math.PI * 2) angle %= Math.PI * 2;
        Vector vel = projectile.getVelocity();
        Vector particle = vel.getCrossProduct(Y_AXIS).normalize().rotateAroundAxis(vel, angle);
        Particles.REDSTONE.display(projectile.getLocation().add(particle));
        Particles.REDSTONE.display(projectile.getLocation().subtract(particle));
    }
}
