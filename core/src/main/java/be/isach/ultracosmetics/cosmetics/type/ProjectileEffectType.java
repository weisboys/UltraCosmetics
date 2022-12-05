package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffect;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectBasicTrail;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectChristmas;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectHelix;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectRainbow;
import be.isach.ultracosmetics.util.Particles;

import com.cryptomorin.xseries.XMaterial;

public class ProjectileEffectType extends CosmeticParticleType<ProjectileEffect> {

    public ProjectileEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ProjectileEffect> clazz, boolean supportsParticleMultiplier) {
        super(Category.PROJECTILE_EFFECTS, configName, repeatDelay, effect, material, clazz, supportsParticleMultiplier);
    }

    public static void register() {
        // Rainbow Trail
        new ProjectileEffectType("Rainbow", 1, Particles.REDSTONE, XMaterial.LIME_WOOL, ProjectileEffectRainbow.class, false);
        // Christmas Trail
        new ProjectileEffectType("Christmas", 1, Particles.REDSTONE, XMaterial.SNOW_BLOCK, ProjectileEffectChristmas.class, false);
        // Helix Trails
        new ProjectileEffectType("RedstoneHelix", 1, Particles.REDSTONE, XMaterial.REDSTONE_BLOCK, ProjectileEffectHelix.class, false);
        new ProjectileEffectType("FlameHelix", 1, Particles.FLAME, XMaterial.FIRE_CHARGE, ProjectileEffectHelix.class, false);
        new ProjectileEffectType("CursedHelix", 1, Particles.SPELL_WITCH, XMaterial.PURPLE_WOOL, ProjectileEffectHelix.class, false);
        // Basic Trails
        new ProjectileEffectType("Spark", 1, Particles.FIREWORKS_SPARK, XMaterial.FIREWORK_ROCKET, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Redstone", 1, Particles.REDSTONE, XMaterial.REDSTONE, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Flame", 1, Particles.FLAME, XMaterial.TORCH, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Water", 1, Particles.WATER_SPLASH, XMaterial.WATER_BUCKET, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Witch", 1, Particles.SPELL_WITCH, XMaterial.BREWING_STAND, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Emerald", 1, Particles.VILLAGER_HAPPY, XMaterial.EMERALD, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Note", 2, Particles.NOTE, XMaterial.NOTE_BLOCK, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Love", 2, Particles.HEART, XMaterial.PINK_WOOL, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Lava", 1, Particles.LAVA, XMaterial.LAVA_BUCKET, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Snow", 1, Particles.SNOWBALL, XMaterial.SNOWBALL, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Slime", 1, Particles.SLIME, XMaterial.SLIME_BALL, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Crit", 1, Particles.CRIT, XMaterial.IRON_SWORD, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("MagicCrit", 1, Particles.CRIT_MAGIC, XMaterial.DIAMOND_SWORD, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Explosion", 5, Particles.EXPLOSION_HUGE, XMaterial.TNT, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Cloud", 1, Particles.CLOUD, XMaterial.WHITE_WOOL, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Smoke", 1, Particles.SMOKE_NORMAL, XMaterial.GRAY_WOOL, ProjectileEffectBasicTrail.class, false);
        new ProjectileEffectType("Portal", 1, Particles.PORTAL, XMaterial.OBSIDIAN, ProjectileEffectBasicTrail.class, false);
    }
}
