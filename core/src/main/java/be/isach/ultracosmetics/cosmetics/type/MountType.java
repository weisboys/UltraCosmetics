package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.cosmetics.mounts.MountDonkey;
import be.isach.ultracosmetics.cosmetics.mounts.MountDragon;
import be.isach.ultracosmetics.cosmetics.mounts.MountDruggedHorse;
import be.isach.ultracosmetics.cosmetics.mounts.MountEcologistHorse;
import be.isach.ultracosmetics.cosmetics.mounts.MountGlacialSteed;
import be.isach.ultracosmetics.cosmetics.mounts.MountHorse;
import be.isach.ultracosmetics.cosmetics.mounts.MountHypeCart;
import be.isach.ultracosmetics.cosmetics.mounts.MountInfernalHorror;
import be.isach.ultracosmetics.cosmetics.mounts.MountMoltenSnake;
import be.isach.ultracosmetics.cosmetics.mounts.MountMule;
import be.isach.ultracosmetics.cosmetics.mounts.MountNyanSheep;
import be.isach.ultracosmetics.cosmetics.mounts.MountOfFire;
import be.isach.ultracosmetics.cosmetics.mounts.MountOfWater;
import be.isach.ultracosmetics.cosmetics.mounts.MountPig;
import be.isach.ultracosmetics.cosmetics.mounts.MountRudolph;
import be.isach.ultracosmetics.cosmetics.mounts.MountSnake;
import be.isach.ultracosmetics.cosmetics.mounts.MountStrider;
import be.isach.ultracosmetics.cosmetics.mounts.MountWalkingDead;
import be.isach.ultracosmetics.util.ServerVersion;
import be.isach.ultracosmetics.version.VersionManager;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A cosmetic type.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class MountType extends CosmeticEntType<Mount> {

    private final int repeatDelay;
    private final List<XMaterial> defaultBlocks;
    private final double defaultSpeed;
    private final double movementSpeed;

    private MountType(String configName, XMaterial material, EntityType entityType, int repeatDelay, double defaultSpeed, Class<? extends Mount> mountClass, List<XMaterial> defaultBlocks) {
        super(Category.MOUNTS, configName, material, entityType, mountClass);
        this.repeatDelay = repeatDelay;
        this.defaultBlocks = defaultBlocks;
        this.defaultSpeed = defaultSpeed;
        this.movementSpeed = SettingsManager.getConfig().getDouble("Mounts." + configName + ".Speed", defaultSpeed);
        setupConfigLate(SettingsManager.getConfig(), getConfigPath());
    }

    private MountType(String configName, XMaterial material, EntityType entityType, int repeatDelay, double defaultSpeed, Class<? extends Mount> mountClass) {
        this(configName, material, entityType, repeatDelay, defaultSpeed, mountClass, null);
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public double getDefaultMovementSpeed() {
        return defaultSpeed;
    }

    public String getMenuName() {
        return MessageManager.getMessage("Mounts." + getConfigName() + ".menu-name");
    }

    @Override
    public String getName() {
        return getMenuName();
    }

    public String getName(Player player) {
        return MessageManager.getMessage("Mounts." + getConfigName() + ".entity-displayname").replace("%playername%", player.getName());
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }

    public List<XMaterial> getDefaultBlocks() {
        return defaultBlocks;
    }

    public boolean doesPlaceBlocks() {
        return defaultBlocks != null;
    }

    public void setupConfigLate(CustomConfiguration config, String path) {
        if (LivingEntity.class.isAssignableFrom(getEntityType().getEntityClass())) {
            config.addDefault(path + ".Speed", getDefaultMovementSpeed(), "The movement speed of the mount, see:", "https://minecraft.fandom.com/wiki/Attribute#Attributes_available_on_all_living_entities");
        }
        if (doesPlaceBlocks()) {
            // Don't use Stream#toList(), it doesn't exist in Java 8
            config.addDefault(path + ".Blocks-To-Place", getDefaultBlocks().stream().map(m -> m.name()).collect(Collectors.toList()), "Blocks to choose from as this mount walks.");
        }
    }

    public static void register(ServerVersion version) {
        VersionManager vm = UltraCosmeticsData.get().getVersionManager();
        new MountType("DruggedHorse", XMaterial.SUGAR, EntityType.HORSE, 2, 1.1, MountDruggedHorse.class);
        new MountType("GlacialSteed", XMaterial.PACKED_ICE, EntityType.HORSE, 2, 0.4, MountGlacialSteed.class, Arrays.asList(XMaterial.SNOW_BLOCK));
        new MountType("MountOfFire", XMaterial.BLAZE_POWDER, EntityType.HORSE, 2, 0.4, MountOfFire.class, Arrays.asList(XMaterial.ORANGE_TERRACOTTA, XMaterial.YELLOW_TERRACOTTA, XMaterial.RED_TERRACOTTA));
        new MountType("Snake", XMaterial.WHEAT_SEEDS, EntityType.SHEEP, 2, 0.3, MountSnake.class);
        new MountType("HypeCart", XMaterial.MINECART, EntityType.MINECART, 1, 0, MountHypeCart.class);
        new MountType("MoltenSnake", XMaterial.MAGMA_CREAM, EntityType.MAGMA_CUBE, 1, 0.4, MountMoltenSnake.class);
        new MountType("MountOfWater", XMaterial.LIGHT_BLUE_DYE, EntityType.HORSE, 2, 0.4, MountOfWater.class, Arrays.asList(XMaterial.LIGHT_BLUE_TERRACOTTA, XMaterial.CYAN_TERRACOTTA, XMaterial.BLUE_TERRACOTTA));
        new MountType("NyanSheep", XMaterial.CYAN_DYE, EntityType.SHEEP, 1, 0.4, MountNyanSheep.class);
        new MountType("EcologistHorse", XMaterial.GREEN_DYE, EntityType.HORSE, 2, 0.4, MountEcologistHorse.class, Arrays.asList(XMaterial.LIME_TERRACOTTA, XMaterial.GREEN_TERRACOTTA));
        new MountType("Rudolph", XMaterial.DEAD_BUSH, horseOrType("MULE", version), 1, 0.4, MountRudolph.class);
        new MountType("WalkingDead", XMaterial.ROTTEN_FLESH, horseOrType("ZOMBIE_HORSE", version), 2, 0.4, MountWalkingDead.class);
        new MountType("InfernalHorror", XMaterial.BONE, horseOrType("SKELETON_HORSE", version), 2, 0.4, MountInfernalHorror.class);
        new MountType("Horse", XMaterial.SADDLE, horseOrType("HORSE", version), 0, 0.3, MountHorse.class);
        new MountType("Donkey", XMaterial.CHEST, horseOrType("DONKEY", version), 0, 0.25, MountDonkey.class);
        new MountType("Mule", XMaterial.ENDER_CHEST, horseOrType("MULE", version), 0, 0.25, MountMule.class);
        new MountType("Pig", XMaterial.PORKCHOP, EntityType.PIG, 0, 0.35, MountPig.class);

        if (version.isAtLeast(ServerVersion.v1_16)) {
            new MountType("Strider", XMaterial.WARPED_FUNGUS_ON_A_STICK, EntityType.STRIDER, 0, 0.35, MountStrider.class);
        }

        if (version.isNmsSupported()) {
            new MountType("Slime", XMaterial.SLIME_BALL, EntityType.SLIME, 2, 0.8, vm.getModule().getSlimeClass());
            new MountType("Spider", XMaterial.COBWEB, EntityType.SPIDER, 2, 0.4, vm.getModule().getSpiderClass());
            new MountType("Dragon", XMaterial.DRAGON_EGG, EntityType.ENDER_DRAGON, 1, 0.7, MountDragon.class) {
                @Override
                public void setupConfig(CustomConfiguration config, String path) {
                    super.setupConfig(config, path);
                    config.addDefault("Mounts.Dragon.Stationary", false, "If true, the dragon will not move.");
                }
            };
        }
    }

    private static EntityType horseOrType(String name, ServerVersion version) {
        if (!version.isAtLeast(ServerVersion.v1_11)) return EntityType.HORSE;
        return EntityType.valueOf(name);
    }
}
