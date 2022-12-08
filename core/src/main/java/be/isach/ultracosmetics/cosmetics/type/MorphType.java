package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.morphs.Morph;
import be.isach.ultracosmetics.cosmetics.morphs.MorphBat;
import be.isach.ultracosmetics.cosmetics.morphs.MorphBlaze;
import be.isach.ultracosmetics.cosmetics.morphs.MorphChicken;
import be.isach.ultracosmetics.cosmetics.morphs.MorphCow;
import be.isach.ultracosmetics.cosmetics.morphs.MorphCreeper;
import be.isach.ultracosmetics.cosmetics.morphs.MorphEnderman;
import be.isach.ultracosmetics.cosmetics.morphs.MorphLlama;
import be.isach.ultracosmetics.cosmetics.morphs.MorphMooshroom;
import be.isach.ultracosmetics.cosmetics.morphs.MorphParrot;
import be.isach.ultracosmetics.cosmetics.morphs.MorphPig;
import be.isach.ultracosmetics.cosmetics.morphs.MorphPolarBear;
import be.isach.ultracosmetics.cosmetics.morphs.MorphSheep;
import be.isach.ultracosmetics.cosmetics.morphs.MorphSlime;
import be.isach.ultracosmetics.cosmetics.morphs.MorphSnowman;
import be.isach.ultracosmetics.cosmetics.morphs.MorphVillager;
import be.isach.ultracosmetics.cosmetics.morphs.MorphWitch;
import be.isach.ultracosmetics.cosmetics.morphs.MorphWitherSkeleton;
import be.isach.ultracosmetics.util.ServerVersion;
import be.isach.ultracosmetics.version.VersionManager;

import org.bukkit.entity.EntityType;

import com.cryptomorin.xseries.XMaterial;

/**
 * Morph types.
 *
 * @author iSach
 * @since 12-19-2015
 */
public class MorphType extends CosmeticType<Morph> {

    /**
     * Disguise Type of the morph.
     */
    private final EntityType disguiseType;
    private final boolean canUseSkill;

    private MorphType(String configName, XMaterial material, EntityType disguiseType, Class<? extends Morph> clazz) {
        this(configName, material, disguiseType, clazz, true);
    }

    private MorphType(String configName, XMaterial material, EntityType disguiseType, Class<? extends Morph> clazz, boolean canUseSkill) {
        super(Category.MORPHS, configName, material, clazz);
        this.disguiseType = disguiseType;
        this.canUseSkill = canUseSkill;
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
     * @return disguise type
     */
    public EntityType getDisguiseType() {
        return disguiseType;
    }

    public static void register() {
        ServerVersion version = UltraCosmeticsData.get().getServerVersion();
        VersionManager vm = UltraCosmeticsData.get().getVersionManager();

        new MorphType("Bat", XMaterial.COAL, EntityType.BAT, MorphBat.class);
        new MorphType("Blaze", XMaterial.BLAZE_POWDER, EntityType.BLAZE, MorphBlaze.class);
        new MorphType("Chicken", XMaterial.EGG, EntityType.CHICKEN, MorphChicken.class);
        new MorphType("Pig", XMaterial.PORKCHOP, EntityType.PIG, MorphPig.class);
        new MorphType("Enderman", XMaterial.ENDER_PEARL, EntityType.ENDERMAN, MorphEnderman.class);
        new MorphType("Slime", XMaterial.SLIME_BALL, EntityType.SLIME, MorphSlime.class);
        new MorphType("Creeper", XMaterial.GUNPOWDER, EntityType.CREEPER, MorphCreeper.class);
        new MorphType("Snowman", XMaterial.SNOWBALL, EntityType.SNOWMAN, MorphSnowman.class);
        new MorphType("Cow", XMaterial.MILK_BUCKET, EntityType.COW, MorphCow.class);
        new MorphType("Mooshroom", XMaterial.RED_MUSHROOM, EntityType.MUSHROOM_COW, MorphMooshroom.class);
        new MorphType("Villager", XMaterial.EMERALD, EntityType.VILLAGER, MorphVillager.class);
        new MorphType("Witch", XMaterial.POISONOUS_POTATO, EntityType.WITCH, MorphWitch.class);

        if (version.isAtLeast(ServerVersion.v1_10)) {
            new MorphType("PolarBear", XMaterial.SNOW_BLOCK, EntityType.POLAR_BEAR, MorphPolarBear.class, vm.isUsingNMS());
        }

        if (version.isAtLeast(ServerVersion.v1_11)) {
            new MorphType("Llama", XMaterial.RED_WOOL, EntityType.LLAMA, MorphLlama.class);
            new MorphType("WitherSkeleton", XMaterial.WITHER_SKELETON_SKULL, EntityType.WITHER_SKELETON, MorphWitherSkeleton.class);
        }

        if (version.isAtLeast(ServerVersion.v1_12)) {
            new MorphType("Parrot", XMaterial.COOKIE, EntityType.PARROT, MorphParrot.class);
        }

        new MorphType("Sheep", XMaterial.WHITE_WOOL, EntityType.SHEEP, MorphSheep.class);
        if (vm.isUsingNMS()) {
            new MorphType("ElderGuardian", XMaterial.PRISMARINE_CRYSTALS, EntityType.ELDER_GUARDIAN, UltraCosmeticsData.get().getVersionManager().getModule().getElderGuardianClass());
        }
    }
}
