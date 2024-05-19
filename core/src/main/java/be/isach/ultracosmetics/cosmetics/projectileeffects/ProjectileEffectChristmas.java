package be.isach.ultracosmetics.cosmetics.projectileeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ProjectileEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.Location;
import org.bukkit.entity.Projectile;

public class ProjectileEffectChristmas extends ProjectileEffectHelix {

    public ProjectileEffectChristmas(UltraPlayer owner, ProjectileEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void showHelix(Projectile projectile, Location a, Location b) {
        Particles.CLOUD.display(projectile.getLocation());
        Particles.DUST.display(211, 47, 47, a);
        Particles.DUST.display(52, 168, 83, b);
    }
}
