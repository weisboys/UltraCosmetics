package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.particleeffects.*;
import be.isach.ultracosmetics.util.Particles;

import com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Particle effect types.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class ParticleEffectType extends CosmeticType<ParticleEffect> {

    private final static List<ParticleEffectType> ENABLED = new ArrayList<>();
    private final static List<ParticleEffectType> VALUES = new ArrayList<>();

    public static List<ParticleEffectType> enabled() {
        return ENABLED;
    }

    public static List<ParticleEffectType> values() {
        return VALUES;
    }

    public static ParticleEffectType valueOf(String s) {
        for (ParticleEffectType particleEffectType : VALUES) {
            if (particleEffectType.getConfigName().equalsIgnoreCase(s)) return particleEffectType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    private final Particles effect;
    private final int repeatDelay;
    private final double particleMultiplier;

    private ParticleEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ParticleEffect> clazz, boolean supportsParticleMultiplier) {
        super(Category.EFFECTS, configName, material, clazz);
        this.repeatDelay = repeatDelay;
        this.effect = effect;
        if (supportsParticleMultiplier) {
            String path = getCategory().getConfigPath() + "." + configName + ".Particle-Multiplier";
            if (!SettingsManager.getConfig().isDouble(path)) {
                particleMultiplier = 1;
                SettingsManager.getConfig().set(getCategory().getConfigPath() + "." + configName + ".Particle-Multiplier", 1.0, "A multiplier applied to the number", "of particles displayed. 1.0 is 100%");
            } else {
                particleMultiplier = SettingsManager.getConfig().getDouble(path);
            }
        } else {
            // particleMultiplier is final so we have to assign it a value no matter what
            particleMultiplier = 1;
        }

        VALUES.add(this);
    }

    public Particles getEffect() {
        return effect;
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }
    
    public double getParticleMultiplier() {
        return particleMultiplier;
    }

    public static void register() {
        new ParticleEffectType("SnowCloud", 1, Particles.SNOW_SHOVEL, XMaterial.SNOWBALL, ParticleEffectSnowCloud.class, true);
        new ParticleEffectType("BloodHelix", 1, Particles.REDSTONE, XMaterial.REDSTONE, ParticleEffectBloodHelix.class, true);
        new ParticleEffectType("FrostLord", 1, Particles.SNOW_SHOVEL, XMaterial.PACKED_ICE, ParticleEffectFrostLord.class, true);
        new ParticleEffectType("FlameRings", 1, Particles.FLAME, XMaterial.BLAZE_POWDER, ParticleEffectFlameRings.class, false);
        new ParticleEffectType("GreenSparks", 1, Particles.VILLAGER_HAPPY, XMaterial.EMERALD, ParticleEffectGreenSparks.class, false);
        new ParticleEffectType("FrozenWalk", 1, Particles.SNOW_SHOVEL, XMaterial.SNOWBALL, ParticleEffectFrozenWalk.class, false);
        new ParticleEffectType("Music", 4, Particles.FLAME, XMaterial.MUSIC_DISC_MALL, ParticleEffectMusic.class, true);
        new ParticleEffectType("Enchanted", 1, Particles.ENCHANTMENT_TABLE, XMaterial.BOOK, ParticleEffectEnchanted.class, true);
        new ParticleEffectType("Inferno", 1, Particles.FLAME, XMaterial.NETHER_WART, ParticleEffectInferno.class, true);
        new ParticleEffectType("AngelWings", 2, Particles.REDSTONE, XMaterial.FEATHER, ParticleEffectAngelWings.class, true);
        new ParticleEffectType("SuperHero", 2, Particles.REDSTONE, XMaterial.GLOWSTONE_DUST, ParticleEffectSuperHero.class, true);
        new ParticleEffectType("SantaHat", 2, Particles.REDSTONE, XMaterial.BEACON, ParticleEffectSantaHat.class, true);
        new ParticleEffectType("EnderAura", 1, Particles.PORTAL, XMaterial.ENDER_EYE, ParticleEffectEnderAura.class, true);
        new ParticleEffectType("FlameFairy", 1, Particles.FLAME, XMaterial.BLAZE_POWDER, ParticleEffectFlameFairy.class, false);
        new ParticleEffectType("RainCloud", 1, Particles.DRIP_WATER, XMaterial.LAPIS_LAZULI, ParticleEffectRainCloud.class, true);
        new ParticleEffectType("CrushedCandyCane", 1, Particles.ITEM_CRACK, XMaterial.WHITE_DYE, ParticleEffectCrushedCandyCane.class, true);
        new ParticleEffectType("InLove", 1, Particles.HEART, XMaterial.RED_DYE, ParticleEffectInLove.class, true);
        new ParticleEffectType("MagicalRods", 3, Particles.REDSTONE, XMaterial.BLAZE_ROD, ParticleEffectMagicalRods.class, true);
        new ParticleEffectType("FireWaves", 4, Particles.FLAME, XMaterial.GOLD_NUGGET, ParticleEffectFireWaves.class, true);
    }
}
