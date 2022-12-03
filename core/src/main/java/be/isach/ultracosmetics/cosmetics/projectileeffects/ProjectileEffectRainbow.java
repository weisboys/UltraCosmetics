package be.isach.ultracosmetics.cosmetics.projectileeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ProjectileEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.entity.Projectile;

public class ProjectileEffectRainbow extends ProjectileEffect {
    private int i = 0;

    public ProjectileEffectRainbow(UltraPlayer owner, ProjectileEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void showParticles(Projectile projectile) {
        i++;

        switch (i) {
            case 1:
                Particles.REDSTONE.display(222, 0, 0, projectile.getLocation());
                break;
            case 2:
                Particles.REDSTONE.display(254, 98, 44, projectile.getLocation());
                break;
            case 3:
                Particles.REDSTONE.display(254, 246, 0, projectile.getLocation());
                break;
            case 4:
                Particles.REDSTONE.display(0, 188, 0, projectile.getLocation());
                break;
            case 5:
                Particles.REDSTONE.display(0, 156, 254, projectile.getLocation());
                break;
            case 6:
                Particles.REDSTONE.display(0, 0, 132, projectile.getLocation());
                break;
            case 7:
                Particles.REDSTONE.display(44, 0, 156, projectile.getLocation());
                i = 0;
                break;
        }
    }
}
