package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.morphs.*;
import be.isach.ultracosmetics.version.ServerVersion;
import be.isach.ultracosmetics.version.VersionManager;
import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;

/**
 * Morph types.
 *
 * @author iSach
 * @since 12-19-2015
 */
public class MorphType extends CosmeticEntType<Morph> {

    private final boolean doesSkillExist;

    private MorphType(String configName, XMaterial material, XEntityType disguiseType) {
        this(configName, material, disguiseType, MorphBasic.class, false);
    }

    private MorphType(String configName, XMaterial material, XEntityType disguiseType, Class<? extends Morph> clazz) {
        this(configName, material, disguiseType, clazz, true);
    }

    private MorphType(String configName, XMaterial material, XEntityType disguiseType, Class<? extends Morph> clazz, boolean doesSkillExist) {
        super(Category.MORPHS, configName, material, disguiseType, clazz);
        this.doesSkillExist = doesSkillExist;
        if (GENERATE_MISSING_MESSAGES) {
            MessageManager.addMessage(getConfigPath() + ".name", configName);
            if (doesSkillExist) {
                MessageManager.addMessage(getConfigPath() + ".skill", "Skill");
            }
        }
    }

    /**
     * Get the skill message.
     *
     * @return The skill message of the morph.
     */
    public Component getSkill() {
        return MessageManager.getMessage(getConfigPath() + ".skill");
    }

    public boolean canUseSkill() {
        return doesSkillExist && SettingsManager.getConfig().getBoolean(getConfigPath() + ".Skill-Enabled", true);
    }

    /**
     * Get the morph Disguise Type.
     *
     * @return disguise type
     */
    public EntityType getDisguiseType() {
        return getEntityType();
    }

    @Override
    public void setupConfig(CustomConfiguration config, String path) {
        super.setupConfig(config, path);
        if (doesSkillExist) {
            config.addDefault(path + ".Skill-Enabled", true, "Whether this morph's skill should be enabled.");
        }
        if (MorphNoFall.class.isAssignableFrom(getClazz())) {
            config.addDefault(path + ".No-Fall-Damage", true,
                    "Whether to disable fall damage while this morph is equipped.",
                    "(Normal use of the morph may cause fall damage otherwise.)");
        }
    }

    public static void register() {
        ServerVersion version = UltraCosmeticsData.get().getServerVersion();
        VersionManager vm = UltraCosmeticsData.get().getVersionManager();

        new MorphType("Bat", XMaterial.COAL, XEntityType.BAT, MorphBat.class);
        new MorphType("Blaze", XMaterial.BLAZE_POWDER, XEntityType.BLAZE, MorphBlaze.class);
        new MorphType("Chicken", XMaterial.EGG, XEntityType.CHICKEN, MorphChicken.class);
        new MorphType("Pig", XMaterial.PORKCHOP, XEntityType.PIG, MorphPig.class);
        new MorphType("Enderman", XMaterial.ENDER_PEARL, XEntityType.ENDERMAN, MorphEnderman.class) {
            @Override
            public void setupConfig(CustomConfiguration config, String path) {
                super.setupConfig(config, path);
                config.addDefault(path + ".Mode", "Ray trace",
                        "Changes how the enderman morph teleports.",
                        "'Ray trace' (default) teleports a player to the block they're looking at,",
                        "up to 16 blocks away. Doesn't perform as well as the other options.",
                        "'Fast', teleports player 16 blocks in the direction they are looking,",
                        "while trying to avoid suffocation. Players will be able to teleport through walls.",
                        "'Enderpearl' simply launches an ender pearl in the direction the player is looking.");
            }
        };
        new MorphType("Slime", XMaterial.SLIME_BALL, XEntityType.SLIME, MorphSlime.class);
        new MorphType("Creeper", XMaterial.GUNPOWDER, XEntityType.CREEPER, MorphCreeper.class);
        new MorphType("Snowman", XMaterial.SNOWBALL, XEntityType.SNOW_GOLEM, MorphSnowman.class);
        new MorphType("Cow", XMaterial.MILK_BUCKET, XEntityType.COW, MorphCow.class);
        new MorphType("Mooshroom", XMaterial.RED_MUSHROOM, XEntityType.MOOSHROOM, MorphMooshroom.class);
        new MorphType("Villager", XMaterial.EMERALD, XEntityType.VILLAGER, MorphVillager.class);
        new MorphType("Witch", XMaterial.POISONOUS_POTATO, XEntityType.WITCH, MorphWitch.class);
        new MorphType("Sheep", XMaterial.WHITE_WOOL, XEntityType.SHEEP, MorphSheep.class);
        new MorphType("CaveSpider", XMaterial.SPIDER_EYE, XEntityType.CAVE_SPIDER, MorphCaveSpider.class, false);
        new MorphType("Endermite", XMaterial.ENDER_EYE, XEntityType.ENDERMITE, MorphEndermite.class, false);
        new MorphType("Guardian", XMaterial.PRISMARINE_SHARD, XEntityType.GUARDIAN, MorphGuardian.class, false);
        new MorphType("Wolf", XMaterial.BONE, XEntityType.WOLF);
        new MorphType("IronGolem", XMaterial.IRON_INGOT, XEntityType.IRON_GOLEM);
        new MorphType("Wither", XMaterial.WITHER_SKELETON_SKULL, XEntityType.WITHER);
        new MorphType("Skeleton", XMaterial.BONE, XEntityType.SKELETON);
        new MorphType("Zombie", XMaterial.ROTTEN_FLESH, XEntityType.ZOMBIE);
        new MorphType("Squid", XMaterial.INK_SAC, XEntityType.SQUID);
        new MorphType("Spider", XMaterial.STRING, XEntityType.SPIDER);
        new MorphType("Ghast", XMaterial.GHAST_TEAR, XEntityType.GHAST);
        new MorphType("MagmaCube", XMaterial.MAGMA_CREAM, XEntityType.MAGMA_CUBE);
        new MorphType("Horse", XMaterial.SADDLE, XEntityType.HORSE);

        switch (version) {
            case NEW:
            case v1_20:
                new MorphType("Sniffer", XMaterial.SNIFFER_EGG, XEntityType.SNIFFER, MorphSniffer.class, false);
            case v1_19:
                new MorphType("Allay", XMaterial.ALLAY_SPAWN_EGG, XEntityType.ALLAY, MorphAllay.class);
                new MorphType("Frog", XMaterial.FROG_SPAWN_EGG, XEntityType.FROG, MorphFrog.class, false);
                new MorphType("Warden", XMaterial.SCULK_SHRIEKER, XEntityType.WARDEN);
                new MorphType("Tadpole", XMaterial.TADPOLE_BUCKET, XEntityType.TADPOLE);
            case v1_18:
                new MorphType("Goat", XMaterial.GOAT_HORN.or(XMaterial.WHEAT), XEntityType.GOAT, MorphGoat.class, false);
            case v1_17:
                break;
        }
        new MorphType("Axolotl", XMaterial.AXOLOTL_BUCKET, XEntityType.AXOLOTL, MorphAxolotl.class);
        new MorphType("GlowSquid", XMaterial.GLOW_INK_SAC, XEntityType.GLOW_SQUID, MorphGlowSquid.class, false);
        new MorphType("Piglin", XMaterial.GOLD_INGOT, XEntityType.PIGLIN);
        new MorphType("Strider", XMaterial.WARPED_FUNGUS_ON_A_STICK, XEntityType.STRIDER);
        new MorphType("Hoglin", XMaterial.COOKED_PORKCHOP, XEntityType.HOGLIN);
        new MorphType("PiglinBrute", XMaterial.GOLDEN_AXE, XEntityType.PIGLIN_BRUTE);
        new MorphType("Zoglin", XMaterial.ZOGLIN_SPAWN_EGG, XEntityType.ZOGLIN);
        new MorphType("ZombifiedPiglin", XMaterial.GOLDEN_SWORD, XEntityType.ZOMBIFIED_PIGLIN);
        new MorphType("Bee", XMaterial.HONEYCOMB, XEntityType.BEE, MorphBee.class);
        new MorphType("Cat", XMaterial.TROPICAL_FISH, XEntityType.CAT, MorphCat.class);
        new MorphType("Fox", XMaterial.SWEET_BERRIES, XEntityType.FOX, MorphFox.class, false);
        new MorphType("Panda", XMaterial.BAMBOO, XEntityType.PANDA);
        new MorphType("Ocelot", XMaterial.COD, XEntityType.OCELOT);
        new MorphType("WanderingTrader", XMaterial.WANDERING_TRADER_SPAWN_EGG, XEntityType.WANDERING_TRADER);
        new MorphType("Pillager", XMaterial.CROSSBOW, XEntityType.PILLAGER);
        new MorphType("Ravager", XMaterial.RAVAGER_SPAWN_EGG, XEntityType.RAVAGER);
        new MorphType("Cod", XMaterial.COD_BUCKET, XEntityType.COD, MorphCod.class, false);
        new MorphType("Dolphin", XMaterial.DOLPHIN_SPAWN_EGG, XEntityType.DOLPHIN, MorphDolphin.class, false);
        new MorphType("Drowned", XMaterial.TRIDENT, XEntityType.DROWNED, MorphDrowned.class, false);
        new MorphType("Pufferfish", XMaterial.PUFFERFISH, XEntityType.PUFFERFISH);
        new MorphType("Salmon", XMaterial.SALMON_BUCKET, XEntityType.SALMON);
        new MorphType("TropicalFish", XMaterial.TROPICAL_FISH_BUCKET, XEntityType.TROPICAL_FISH);
        new MorphType("Turtle", XMaterial.TURTLE_HELMET, XEntityType.TURTLE);
        new MorphType("Phantom", XMaterial.PHANTOM_MEMBRANE, XEntityType.PHANTOM);
        new MorphType("Parrot", XMaterial.COOKIE, XEntityType.PARROT, MorphParrot.class);
        new MorphType("Illusioner", XMaterial.COMMAND_BLOCK, XEntityType.ILLUSIONER);
        new MorphType("Llama", XMaterial.RED_WOOL, XEntityType.LLAMA, MorphLlama.class);
        new MorphType("Evoker", XMaterial.TOTEM_OF_UNDYING, XEntityType.EVOKER, MorphEvoker.class, false);
        new MorphType("WitherSkeleton", XMaterial.WITHER_SKELETON_SKULL, XEntityType.WITHER_SKELETON, MorphWitherSkeleton.class);
        new MorphType("Donkey", XMaterial.CHEST, XEntityType.DONKEY, MorphDonkey.class, false);
        new MorphType("Vex", XMaterial.IRON_SWORD, XEntityType.VEX);
        new MorphType("Vindicator", XMaterial.IRON_AXE, XEntityType.VINDICATOR);
        new MorphType("Mule", XMaterial.SADDLE, XEntityType.MULE);
        new MorphType("SkeletonHorse", XMaterial.BONE_BLOCK, XEntityType.SKELETON_HORSE);
        new MorphType("ZombieHorse", XMaterial.ZOMBIE_HORSE_SPAWN_EGG, XEntityType.ZOMBIE_HORSE);
        if (vm.isUsingNMS()) {
            new MorphType("ElderGuardian", XMaterial.PRISMARINE_CRYSTALS, XEntityType.ELDER_GUARDIAN, UltraCosmeticsData.get().getVersionManager().getModule().getElderGuardianClass());
        }
        new MorphType("PolarBear", XMaterial.SNOW_BLOCK, XEntityType.POLAR_BEAR, MorphPolarBear.class, vm.isUsingNMS());
        new MorphType("Shulker", XMaterial.SHULKER_BOX, XEntityType.SHULKER);
    }
}
