package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.morphs.*;
import be.isach.ultracosmetics.util.ServerVersion;

import org.bukkit.entity.EntityType;

import com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Morph types.
 *
 * @author iSach
 * @since 12-19-2015
 */
public class MorphType extends CosmeticType<Morph> {

    private final static List<MorphType> ENABLED = new ArrayList<>();
    private final static List<MorphType> VALUES = new ArrayList<>();

    public static List<MorphType> enabled() {
        return ENABLED;
    }

    public static List<MorphType> values() {
        return VALUES;
    }

    public static MorphType valueOf(String s) {
        for (MorphType morphType : VALUES) {
            if (morphType.getConfigName().equalsIgnoreCase(s)) return morphType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(CosmeticType::isEnabled).collect(Collectors.toList()));
    }

    /**
     * Disguise Type of the morph.
     */
    private final EntityType disguiseType;
    private final boolean canUseSkill;

    private MorphType(String configName, XMaterial material, EntityType disguiseType, Class<? extends Morph> clazz) {
        this(configName, material, disguiseType, clazz, v -> true);
    }

    private MorphType(String configName, XMaterial material, EntityType disguiseType, Class<? extends Morph> clazz, Predicate<ServerVersion> canUseSkill) {
        super(Category.MORPHS, configName, material, clazz);
        this.disguiseType = disguiseType;
        this.canUseSkill = canUseSkill.test(UltraCosmeticsData.get().getServerVersion());

        VALUES.add(this);
    }

    /**
     * Get the skill message.
     *
     * @return The skill message of the morph.
     */
    public String getSkill() {
        return MessageManager.getMessage("Morphs." + getConfigName() + ".skill");
    }

    public boolean canUseSkill() {
        return canUseSkill;
    }

    /**
     * Get the morph Disguise Type.
     *
     * @return
     */
    public EntityType getDisguiseType() {
        return disguiseType;
    }

    public static void register() {
        ServerVersion version = UltraCosmeticsData.get().getServerVersion();

        new MorphType("Bat", XMaterial.COAL, EntityType.BAT, MorphBat.class);
        new MorphType("Blaze", XMaterial.BLAZE_POWDER, EntityType.BLAZE, MorphBlaze.class);
        new MorphType("Chicken", XMaterial.EGG, EntityType.CHICKEN, MorphChicken.class);
        new MorphType("Pig", XMaterial.PORKCHOP, EntityType.PIG, MorphPig.class);
        new MorphType("Enderman", XMaterial.ENDER_PEARL, EntityType.ENDERMAN, MorphEnderman.class);
        new MorphType("Slime", XMaterial.SLIME_BALL, EntityType.SLIME, MorphSlime.class);
        new MorphType("Creeper", XMaterial.GUNPOWDER, EntityType.CREEPER, MorphCreeper.class);
        new MorphType("Snowman", XMaterial.SNOWBALL, EntityType.SNOWMAN, MorphSnowman.class);
        new MorphType("ElderGuardian", XMaterial.PRISMARINE_CRYSTALS, EntityType.ELDER_GUARDIAN, UltraCosmeticsData.get().getVersionManager().getModule().getElderGuardianClass());
        new MorphType("Cow", XMaterial.MILK_BUCKET, EntityType.COW, MorphCow.class);
        new MorphType("Mooshroom", XMaterial.RED_MUSHROOM, EntityType.MUSHROOM_COW, MorphMooshroom.class);
        new MorphType("Villager", XMaterial.EMERALD, EntityType.VILLAGER, MorphVillager.class);
        new MorphType("Witch", XMaterial.POISONOUS_POTATO, EntityType.WITCH, MorphWitch.class);
        new MorphType("Sheep", XMaterial.WHITE_WOOL, EntityType.SHEEP, MorphSheep.class);
        new MorphType("WitherSkeleton", XMaterial.WITHER_SKELETON_SKULL, EntityType.WITHER_SKELETON, MorphWitherSkeleton.class);
        new MorphType("CaveSpider", XMaterial.SPIDER_EYE, EntityType.CAVE_SPIDER, MorphCaveSpider.class);
        new MorphType("Donkey", XMaterial.CHEST, EntityType.DONKEY, MorphDonkey.class);
        new MorphType("Endermite", XMaterial.ENDER_EYE, EntityType.ENDERMITE, MorphEndermite.class);
        new MorphType("Guardian", XMaterial.PRISMARINE_SHARD, EntityType.GUARDIAN, MorphGuardian.class);

        if (version.isAtLeast(ServerVersion.v1_10)) {
            new MorphType("PolarBear", XMaterial.SNOW_BLOCK, EntityType.POLAR_BEAR, MorphPolarBear.class, ServerVersion::isNmsSupported);
        }

        if (version.isAtLeast(ServerVersion.v1_11)) {
            new MorphType("Llama", XMaterial.RED_WOOL, EntityType.LLAMA, MorphLlama.class);
            new MorphType("Evoker", XMaterial.TOTEM_OF_UNDYING, EntityType.EVOKER, MorphEvoker.class);
        }

        if (version.isAtLeast(ServerVersion.v1_12)) {
            new MorphType("Parrot", XMaterial.COOKIE, EntityType.PARROT, MorphParrot.class);
        }

        if (version.isAtLeast(ServerVersion.v1_13)) {
            new MorphType("Cod", XMaterial.COD_BUCKET, EntityType.COD, MorphCod.class);
            new MorphType("Dolphin", XMaterial.DOLPHIN_SPAWN_EGG, EntityType.DOLPHIN, MorphDolphin.class);
            new MorphType("Drowned", XMaterial.TRIDENT, EntityType.DROWNED, MorphDrowned.class);
        }

        if (version.isAtLeast(ServerVersion.v1_14)) {
            new MorphType("Cat", XMaterial.TROPICAL_FISH, EntityType.CAT, MorphCat.class);
            new MorphType("Fox", XMaterial.SWEET_BERRIES, EntityType.FOX, MorphFox.class);
        }

        if (version.isAtLeast(ServerVersion.v1_15)) {
            new MorphType("Bee", XMaterial.HONEYCOMB, EntityType.BEE, MorphBee.class);
        }

        if (version.isAtLeast(ServerVersion.v1_17)) {
            new MorphType("Axolotl", XMaterial.AXOLOTL_BUCKET, EntityType.AXOLOTL, MorphAxolotl.class);
            new MorphType("GlowSquid", XMaterial.GLOW_INK_SAC, EntityType.GLOW_SQUID, MorphGlowSquid.class);
        }
        
        if (version.isAtLeast(ServerVersion.v1_18)) {
            new MorphType("Goat", XMaterial.GOAT_HORN, EntityType.GOAT, MorphGoat.class);
        }

        if (version.isAtLeast(ServerVersion.v1_19)) {
            new MorphType("Allay", XMaterial.ALLAY_SPAWN_EGG, EntityType.ALLAY, MorphAllay.class);
            new MorphType("Frog", XMaterial.FROG_SPAWN_EGG, EntityType.FROG, MorphFrog.class);
        }
    }
}
