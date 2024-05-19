package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffect;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectBasicTrail;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectChristmas;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectHelix;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectNote;
import be.isach.ultracosmetics.cosmetics.projectileeffects.ProjectileEffectRainbow;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;

public class ProjectileEffectType extends CosmeticParticleType<ProjectileEffect> {

    public ProjectileEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ProjectileEffect> clazz, boolean supportsParticleMultiplier) {
        this(configName, repeatDelay, effect, material, clazz);
    }

    public ProjectileEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ProjectileEffect> clazz) {
        super(Category.PROJECTILE_EFFECTS, configName, repeatDelay, effect, material, clazz, false);
        if (GENERATE_MISSING_MESSAGES) {
            MessageManager.addMessage(getConfigPath() + ".name", configName);
        }
    }

    public static void register(ServerVersion version) {
        // Rainbow Trail
        new ProjectileEffectType("Rainbow", 1, Particles.DUST, XMaterial.LIME_WOOL, ProjectileEffectRainbow.class);

        // Christmas Trail
        new ProjectileEffectType("Christmas", 1, Particles.DUST, XMaterial.SNOW_BLOCK, ProjectileEffectChristmas.class);

        // Helix Trails
        new ProjectileEffectType("RedstoneHelix", 1, Particles.DUST, XMaterial.REDSTONE_BLOCK, ProjectileEffectHelix.class);
        new ProjectileEffectType("FlameHelix", 1, Particles.FLAME, XMaterial.FIRE_CHARGE, ProjectileEffectHelix.class);
        new ProjectileEffectType("CursedHelix", 1, Particles.WITCH, XMaterial.PURPLE_WOOL, ProjectileEffectHelix.class);
        new ProjectileEffectType("SoulFireHelix", 1, Particles.SOUL_FIRE_FLAME, XMaterial.SOUL_CAMPFIRE, ProjectileEffectHelix.class);

        // Basic Trails
        new ProjectileEffectType("Spark", 1, Particles.FIREWORK, XMaterial.FIREWORK_ROCKET, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Redstone", 1, Particles.DUST, XMaterial.REDSTONE, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Flame", 1, Particles.FLAME, XMaterial.TORCH, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Water", 1, Particles.SPLASH, XMaterial.WATER_BUCKET, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Witch", 1, Particles.WITCH, XMaterial.BREWING_STAND, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Emerald", 1, Particles.HAPPY_VILLAGER, XMaterial.EMERALD, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Note", 2, Particles.NOTE, XMaterial.NOTE_BLOCK, ProjectileEffectNote.class);
        new ProjectileEffectType("Love", 2, Particles.HEART, XMaterial.PINK_WOOL, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Lava", 1, Particles.LAVA, XMaterial.LAVA_BUCKET, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Snow", 1, Particles.SNOW_SHOVEL, XMaterial.SNOWBALL, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Slime", 1, Particles.ITEM_SLIME, XMaterial.SLIME_BALL, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Crit", 1, Particles.CRIT, XMaterial.IRON_SWORD, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("MagicCrit", 1, Particles.ENCHANTED_HIT, XMaterial.DIAMOND_SWORD, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Explosion", 5, Particles.EXPLOSION_EMITTER, XMaterial.TNT, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Cloud", 1, Particles.CLOUD, XMaterial.WHITE_WOOL, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Smoke", 1, Particles.SMOKE, XMaterial.GRAY_WOOL, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Portal", 1, Particles.PORTAL, XMaterial.OBSIDIAN, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("DragonBreath", 1, Particles.DRAGON_BREATH, XMaterial.DRAGON_BREATH, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("SquidInk", 1, Particles.SQUID_INK, XMaterial.INK_SAC, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("SoulFireFlame", 1, Particles.SOUL_FIRE_FLAME, XMaterial.SOUL_TORCH, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Snowflake", 1, Particles.SNOWFLAKE, XMaterial.POWDER_SNOW_BUCKET, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("GlowSquid", 1, Particles.GLOW, XMaterial.GLOW_SQUID_SPAWN_EGG, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("GlowSquidInk", 1, Particles.GLOW_SQUID_INK, XMaterial.GLOW_INK_SAC, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("Scrape", 1, Particles.SCRAPE, XMaterial.OXIDIZED_COPPER, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("WaxOff", 1, Particles.WAX_OFF, XMaterial.WAXED_COPPER_BLOCK, ProjectileEffectBasicTrail.class);
        new ProjectileEffectType("WaxOn", 1, Particles.WAX_ON, XMaterial.HONEYCOMB, ProjectileEffectBasicTrail.class);
        if (version.isAtLeast(ServerVersion.v1_19)) {
            new ProjectileEffectType("SculkSoul", 1, Particles.SCULK_SOUL, XMaterial.SCULK_CATALYST, ProjectileEffectBasicTrail.class);
        }
    }
}
