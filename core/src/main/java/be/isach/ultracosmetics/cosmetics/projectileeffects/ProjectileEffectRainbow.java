package be.isach.ultracosmetics.cosmetics.projectileeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ProjectileEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ColorUtils;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.entity.Projectile;

import java.awt.Color;

public class ProjectileEffectRainbow extends ProjectileEffect {
    public ProjectileEffectRainbow(UltraPlayer owner, ProjectileEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void showParticles(Projectile projectile) {
        Color color = ColorUtils.getRainbowColor(0.5);
        Particles.DUST.display(color.getRed(), color.getGreen(), color.getBlue(), projectile.getLocation());
    }
}
