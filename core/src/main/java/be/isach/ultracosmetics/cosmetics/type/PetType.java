package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.pets.*;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.SmartLogger;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Pet types.
 *
 * @author iSach
 * @since 12-20-2015
 */
public class PetType extends CosmeticEntType<Pet> {

    private static final Map<XEntityType, Class<? extends Pet>> PET_MAP = new HashMap<>();

    private final String customization;

    private PetType(String configName, XMaterial material, XEntityType entityType, Class<? extends Pet> clazz, String customization) {
        super(Category.PETS, configName, material, entityType, clazz);
        this.customization = customization;

        PET_MAP.putIfAbsent(entityType, clazz);

        if (GENERATE_MISSING_MESSAGES) {
            MessageManager.addMessage(getConfigPath() + ".menu-name", configName);
            MessageManager.addMessage(getConfigPath() + ".entity-displayname", "<bold><playername>'s " + configName);
        }
    }

    private PetType(String configName, XMaterial material, XEntityType entityType, Class<? extends Pet> clazz) {
        this(configName, material, entityType, clazz, null);
    }

    public Component getEntityName(Player player) {
        return MessageManager.getMessage("Pets." + getConfigName() + ".entity-displayname",
                Placeholder.unparsed("playername", player.getName())
        );
    }

    @Override
    public Component getName() {
        return MessageManager.getMessage("Pets." + getConfigName() + ".menu-name");
    }

    @Override
    public Pet equip(UltraPlayer player, UltraCosmetics ultraCosmetics) {
        Pet pet = super.equip(player, ultraCosmetics);
        if (pet != null && customization != null) {
            if (!pet.customize(customization)) {
                UltraCosmeticsData.get().getPlugin().getSmartLogger().write(LogLevel.WARNING, "Invalid customization string for pet " + getConfigName());
                player.sendMessage(ChatColor.RED + "Invalid customization string, please contact an admin.");
            }
        }
        return pet;
    }

    public static void register(ServerVersion serverVersion) {
        // Pets that don't work
        /* new PetType("Squid", XMaterial.INK_SAC, XEntityType.SQUID, Pet.class); */
        /* new PetType("Ghast", XMaterial.FIRE_CHARGE, XEntityType.GHAST, Pet.class); */
        /* new PetType("GlowSquid", XMaterial.GLOW_INK_SAC, XEntityType.GLOW_SQUID, Pet.class); */
        /* new PetType("Phantom", XMaterial.PHANTOM_MEMBRANE, XEntityType.PHANTOM, Pet.class); */

        new PetType("Piggy", XMaterial.PORKCHOP, XEntityType.PIG, Pet.class);
        new PetType("EasterBunny", XMaterial.CARROT, XEntityType.RABBIT, PetEasterBunny.class);
        new PetType("Cow", XMaterial.MILK_BUCKET, XEntityType.COW, Pet.class);
        new PetType("Mooshroom", XMaterial.RED_MUSHROOM, XEntityType.MOOSHROOM, PetMooshroom.class);
        new PetType("Dog", XMaterial.BONE, XEntityType.WOLF, PetDog.class);
        new PetType("Chick", XMaterial.EGG, XEntityType.CHICKEN, Pet.class);
        new PetType("ChristmasElf", XMaterial.BEACON, XEntityType.VILLAGER, PetChristmasElf.class);
        new PetType("IronGolem", XMaterial.IRON_INGOT, XEntityType.IRON_GOLEM, PetIronGolem.class);
        new PetType("Snowman", XMaterial.SNOWBALL, XEntityType.SNOW_GOLEM, PetSnowman.class);
        new PetType("Villager", XMaterial.EMERALD, XEntityType.VILLAGER, PetVillager.class);
        new PetType("Bat", XMaterial.COAL, XEntityType.BAT, Pet.class);
        new PetType("Sheep", XMaterial.WHITE_WOOL, XEntityType.SHEEP, PetSheep.class);
        new PetType("Wither", XMaterial.WITHER_SKELETON_SKULL, XEntityType.WITHER, PetWither.class) {
            @Override
            public void setupConfig(CustomConfiguration config, String path) {
                super.setupConfig(config, path);
                config.addDefault(path + ".Bossbar", "in range",
                        "Sets who the bossbar is visible for. (Has no effect on 1.8)",
                        "'in range': vanilla behavior, visible to all players in range.",
                        "'owner': only visible to pet owner",
                        "'none': not visible to any players");
            }
        };
        new PetType("Slime", XMaterial.SLIME_BALL, XEntityType.SLIME, PetSlime.class);
        new PetType("Silverfish", XMaterial.GRAY_DYE, XEntityType.SILVERFISH, Pet.class);
        new PetType("Blaze", XMaterial.BLAZE_ROD, XEntityType.BLAZE, Pet.class);
        new PetType("Creeper", XMaterial.GUNPOWDER, XEntityType.CREEPER, PetCreeper.class);
        new PetType("Enderman", XMaterial.ENDER_PEARL, XEntityType.ENDERMAN, PetEnderman.class);
        new PetType("Skeleton", XMaterial.BOW, XEntityType.SKELETON, PetSkeleton.class);
        new PetType("Zombie", XMaterial.ROTTEN_FLESH, XEntityType.ZOMBIE, PetZombie.class);

        new PetType("CaveSpider", XMaterial.SPIDER_EYE, XEntityType.CAVE_SPIDER, Pet.class);
        new PetType("Spider", XMaterial.STRING, XEntityType.SPIDER, Pet.class);
        new PetType("Endermite", XMaterial.ENDER_EYE, XEntityType.ENDERMITE, Pet.class);

        new PetType("Guardian", XMaterial.PRISMARINE_SHARD, XEntityType.GUARDIAN, Pet.class);
        new PetType("MagmaCube", XMaterial.MAGMA_CREAM, XEntityType.MAGMA_CUBE, PetMagmaCube.class);
        new PetType("Witch", XMaterial.POTION, XEntityType.WITCH, Pet.class);
        new PetType("Horse", XMaterial.LEATHER_HORSE_ARMOR.or(XMaterial.LEATHER), XEntityType.HORSE, PetHorse.class);

        // Cases fall through, so for example v1_19 gets all pets of 1.19 and below.
        switch (serverVersion) {
            case NEW:
            case v1_20:
                new PetType("Sniffer", XMaterial.TORCHFLOWER_SEEDS, XEntityType.SNIFFER, PetSniffer.class);
            case v1_19:
                new PetType("Frog", XMaterial.LILY_PAD, XEntityType.FROG, PetFrog.class);
                new PetType("Warden", XMaterial.SCULK_SHRIEKER, XEntityType.WARDEN, PetWarden.class) {
                    @Override
                    public void setupConfig(CustomConfiguration config, String path) {
                        super.setupConfig(config, path);
                        config.addDefault(path + ".Block-Effect", true,
                                "Whether the darkness effect is blocked while this pet is equipped.",
                                "Please note that this will also block darkness from real wardens,",
                                "due to Spigot API limitations.");
                    }
                };
                new PetType("Allay", XMaterial.ALLAY_SPAWN_EGG, XEntityType.ALLAY, PetAllay.class);
                new PetType("Tadpole", XMaterial.TADPOLE_BUCKET, XEntityType.TADPOLE, PetTadpole.class);
            case v1_18:
                new PetType("Goat", XMaterial.GOAT_HORN.or(XMaterial.WHEAT), XEntityType.GOAT, PetGoat.class);
            case v1_17:
                break;
        }

        new PetType("Axolotl", XMaterial.AXOLOTL_BUCKET, XEntityType.AXOLOTL, PetAxolotl.class) {
            @Override
            public void setupConfig(CustomConfiguration config, String path) {
                super.setupConfig(config, path);
                config.addDefault(path + ".Fast", false, "https://imgur.com/a/EKWwQ6w");
            }
        };
        new PetType("Piglin", XMaterial.GOLD_INGOT, XEntityType.PIGLIN, PetPiglin.class);
        new PetType("Strider", XMaterial.WARPED_FUNGUS, XEntityType.STRIDER, Pet.class);
        new PetType("Hoglin", XMaterial.COOKED_PORKCHOP, XEntityType.HOGLIN, PetHoglin.class);
        new PetType("PiglinBrute", XMaterial.GOLDEN_AXE, XEntityType.PIGLIN_BRUTE, PetPiglinBrute.class);
        new PetType("Zoglin", XMaterial.ZOGLIN_SPAWN_EGG, XEntityType.ZOGLIN, Pet.class);
        new PetType("ZombifiedPiglin", XMaterial.GOLDEN_SWORD, XEntityType.ZOMBIFIED_PIGLIN, PetZombifiedPiglin.class);
        new PetType("Bee", XMaterial.HONEYCOMB, XEntityType.BEE, Pet.class);
        new PetType("Panda", XMaterial.BAMBOO, XEntityType.PANDA, PetPanda.class);
        new PetType("Fox", XMaterial.SWEET_BERRIES, XEntityType.FOX, PetFox.class);
        new PetType("Kitty", XMaterial.TROPICAL_FISH, XEntityType.CAT, PetKitty.class);
        new PetType("Ocelot", XMaterial.COD, XEntityType.OCELOT, Pet.class);
        new PetType("WanderingTrader", XMaterial.WANDERING_TRADER_SPAWN_EGG, XEntityType.WANDERING_TRADER, PetWanderingTrader.class);
        new PetType("Pillager", XMaterial.CROSSBOW, XEntityType.PILLAGER, Pet.class);
        new PetType("Ravager", XMaterial.RAVAGER_SPAWN_EGG, XEntityType.RAVAGER, Pet.class);
        new PetType("Cod", XMaterial.COD_BUCKET, XEntityType.COD, Pet.class);
        new PetType("Pufferfish", XMaterial.PUFFERFISH, XEntityType.PUFFERFISH, PetPufferfish.class);
        new PetType("Salmon", XMaterial.SALMON_BUCKET, XEntityType.SALMON, Pet.class);
        new PetType("TropicalFish", XMaterial.TROPICAL_FISH_BUCKET, XEntityType.TROPICAL_FISH, PetTropicalFish.class);
        new PetType("Turtle", XMaterial.TURTLE_HELMET, XEntityType.TURTLE, Pet.class);
        new PetType("Dolphin", XMaterial.DOLPHIN_SPAWN_EGG, XEntityType.DOLPHIN, Pet.class);
        new PetType("Drowned", XMaterial.TRIDENT, XEntityType.DROWNED, PetDrowned.class);
        new PetType("Parrot", XMaterial.COOKIE, XEntityType.PARROT, PetParrot.class);
        new PetType("Illusioner", XMaterial.COMMAND_BLOCK, XEntityType.ILLUSIONER, Pet.class);
        new PetType("Llama", XMaterial.RED_WOOL, XEntityType.LLAMA, PetLlama.class);
        new PetType("Vex", XMaterial.IRON_SWORD, XEntityType.VEX, PetVex.class);
        new PetType("Evoker", XMaterial.TOTEM_OF_UNDYING, XEntityType.EVOKER, Pet.class);
        new PetType("Vindicator", XMaterial.IRON_AXE, XEntityType.VINDICATOR, PetVindicator.class);
        // Some entities are here because they were split into different XEntityTypes in 1.11
        new PetType("Donkey", XMaterial.CHEST, XEntityType.DONKEY, PetDonkey.class);
        new PetType("Mule", XMaterial.SADDLE, XEntityType.MULE, PetMule.class);
        new PetType("SkeletonHorse", XMaterial.BONE_BLOCK, XEntityType.SKELETON_HORSE, Pet.class);
        new PetType("ZombieHorse", XMaterial.ZOMBIE_HORSE_SPAWN_EGG, XEntityType.ZOMBIE_HORSE, Pet.class);
        new PetType("ElderGuardian", XMaterial.PRISMARINE_CRYSTALS, XEntityType.ELDER_GUARDIAN, PetElderGuardian.class) {
            @Override
            public void setupConfig(CustomConfiguration config, String path) {
                super.setupConfig(config, path);
                config.addDefault(path + ".Block-Effect", true,
                        "Whether the mining fatigue effect is blocked while this pet is equipped.",
                        "Please note that this will also block mining fatigue from real elder guardians,",
                        "due to Spigot API limitations.");
            }
        };
        new PetType("WitherSkeleton", XMaterial.STONE_SWORD, XEntityType.WITHER_SKELETON, PetWitherSkeleton.class);
        new PetType("ZombieVillager", XMaterial.GOLDEN_APPLE, XEntityType.ZOMBIE_VILLAGER, PetZombieVillager.class);
        new PetType("Husk", XMaterial.SAND, XEntityType.HUSK, Pet.class);
        new PetType("Stray", XMaterial.ARROW, XEntityType.STRAY, PetStray.class);
        new PetType("PolarBear", XMaterial.SNOW_BLOCK, XEntityType.POLAR_BEAR, Pet.class);
        new PetType("Shulker", XMaterial.SHULKER_BOX, XEntityType.SHULKER, PetShulker.class);

        if (UltraCosmeticsData.get().getVersionManager().isUsingNMS()) {
            new PetType("Pumpling", XMaterial.PUMPKIN, XEntityType.ZOMBIE, UltraCosmeticsData.get().getVersionManager().getModule().getPumplingClass());
        }

        ConfigurationSection pets = getCustomConfig(Category.PETS);
        if (pets == null) return;

        Optional<XMaterial> mat;
        SmartLogger log = UltraCosmeticsData.get().getPlugin().getSmartLogger();
        for (String key : pets.getKeys(false)) {
            ConfigurationSection pet = pets.getConfigurationSection(key);
            if (!pet.isString("type") || !pet.isString("item") || !pet.isString("customization")) {
                log.write(LogLevel.WARNING, "Incomplete custom pet '" + key + "'");
                continue;
            }
            Optional<XEntityType> optionalType = XEntityType.of(pet.getString("type").toUpperCase());
            if (optionalType.isEmpty()) {
                log.write(LogLevel.WARNING, "Invalid entity type for custom pet '" + key + "'");
                continue;
            }
            XEntityType type = optionalType.get();
            if (!PET_MAP.containsKey(type)) {
                log.write(LogLevel.WARNING, "Entity type '" + type + "' for pet '" + key + "' does not exist as a pet.");
                continue;
            }
            mat = XMaterial.matchXMaterial(pet.getString("item"));
            if (mat.isEmpty() || !mat.get().parseMaterial().isItem()) {
                log.write(LogLevel.WARNING, "Invalid item for custom pet '" + key + "'");
                continue;
            }
            MessageManager.addMessage(Category.PETS.getConfigPath() + "." + key + ".menu-name", key);
            MessageManager.addMessage(Category.PETS.getConfigPath() + "." + key + ".entity-displayname", "&l%playername%'s " + key);
            MessageManager.addMessage(Category.PETS.getConfigPath() + "." + key + ".Description", "A custom pet!");
            new PetType(key, mat.get(), type, PET_MAP.get(type), pet.getString("customization"));
        }
    }
}
