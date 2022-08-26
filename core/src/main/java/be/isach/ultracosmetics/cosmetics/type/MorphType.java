package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.morphs.*;
import be.isach.ultracosmetics.util.ServerVersion;
import com.cryptomorin.xseries.XMaterial;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import java.util.ArrayList;
import java.util.List;
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
    private final DisguiseType disguiseType;

    private MorphType(String configName, XMaterial material, DisguiseType disguiseType, Class<? extends Morph> clazz) {
        super(Category.MORPHS, configName, material, clazz);
        this.disguiseType = disguiseType;

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

    /**
     * Get the morph Disguise Type.
     *
     * @return
     */
    public DisguiseType getDisguiseType() {
        return disguiseType;
    }

    public static void register() {
        new MorphType("Bat", XMaterial.COAL, DisguiseType.BAT, MorphBat.class);
        new MorphType("Blaze", XMaterial.BLAZE_POWDER, DisguiseType.BLAZE, MorphBlaze.class);
        new MorphType("Chicken", XMaterial.EGG, DisguiseType.CHICKEN, MorphChicken.class);
        new MorphType("Pig", XMaterial.PORKCHOP, DisguiseType.PIG, MorphPig.class);
        new MorphType("Enderman", XMaterial.ENDER_PEARL, DisguiseType.ENDERMAN, MorphEnderman.class);
        new MorphType("Slime", XMaterial.SLIME_BALL, DisguiseType.SLIME, MorphSlime.class);
        new MorphType("Creeper", XMaterial.GUNPOWDER, DisguiseType.CREEPER, MorphCreeper.class);
        new MorphType("Snowman", XMaterial.SNOWBALL, DisguiseType.SNOWMAN, MorphSnowman.class);
        new MorphType("ElderGuardian", XMaterial.PRISMARINE_CRYSTALS, DisguiseType.ELDER_GUARDIAN, UltraCosmeticsData.get().getVersionManager().getMorphs().getElderGuardianClass());
        new MorphType("Cow", XMaterial.MILK_BUCKET, DisguiseType.COW, MorphCow.class);
        new MorphType("Mooshroom", XMaterial.RED_MUSHROOM, DisguiseType.MUSHROOM_COW, MorphMooshroom.class);
        new MorphType("Villager", XMaterial.EMERALD, DisguiseType.VILLAGER, MorphVillager.class);

        if (UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_12_R1)) {
            new MorphType("Witch", XMaterial.POISONOUS_POTATO, getDisguiseType("WITCH"), MorphWitch.class);
            new MorphType("PolarBear", XMaterial.SNOW_BLOCK, getDisguiseType("POLAR_BEAR"), MorphPolarBear.class);
            new MorphType("Llama", XMaterial.RED_WOOL, getDisguiseType("LLAMA"), MorphLlama.class);
            new MorphType("Parrot", XMaterial.COOKIE, getDisguiseType("PARROT"), MorphParrot.class);
        }

        new MorphType("Sheep", XMaterial.WHITE_WOOL, DisguiseType.SHEEP, MorphSheep.class);
        new MorphType("WitherSkeleton", XMaterial.WITHER_SKELETON_SKULL, DisguiseType.WITHER_SKELETON, MorphWitherSkeleton.class);
    }

    private static DisguiseType getDisguiseType(String type) {
        return DisguiseType.valueOf(type);
    }
}
