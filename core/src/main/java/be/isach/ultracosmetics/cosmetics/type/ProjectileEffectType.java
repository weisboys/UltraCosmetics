package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffect;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectExplosion;
import be.isach.ultracosmetics.util.Particles;

import com.cryptomorin.xseries.XMaterial;

public class ProjectileEffectType extends CosmeticParticleType<ProjectileEffect> {

    public ProjectileEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ProjectileEffect> clazz, boolean supportsParticleMultiplier) {
        super(Category.PROJECTILE_EFFECTS, configName, repeatDelay, effect, material, clazz, supportsParticleMultiplier);
    }

    public static void register() {
        new ProjectileEffectType("Explosion", 5, Particles.EXPLOSION_HUGE, XMaterial.TNT, ProjectileEffectExplosion.class, false);
    }
}
