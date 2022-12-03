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
                Particles.REDSTONE.display(255, 0, 0, projectile.getLocation());
                break;
            case 2:
                Particles.REDSTONE.display(255, 135, 0, projectile.getLocation());
                break;
            case 3:
                Particles.REDSTONE.display(255, 211, 0, projectile.getLocation());
                break;
            case 4:
                Particles.REDSTONE.display(222, 255, 10, projectile.getLocation());
                break;
            case 5:
                Particles.REDSTONE.display(161, 255, 10, projectile.getLocation());
                break;
            case 6:
                Particles.REDSTONE.display(10, 255, 153, projectile.getLocation());
                break;
            case 7:
                Particles.REDSTONE.display(10, 239, 255, projectile.getLocation());
                break;
            case 8:
                Particles.REDSTONE.display(20, 125, 245, projectile.getLocation());
                break;
            case 9:
                Particles.REDSTONE.display(88, 10, 255, projectile.getLocation());
                break;
            case 10:
                Particles.REDSTONE.display(190, 10, 255, projectile.getLocation());
                i = 0;
                break;
        }
    }
}
