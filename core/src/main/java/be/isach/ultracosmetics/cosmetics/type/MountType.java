package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.cosmetics.mounts.MountDragon;
import be.isach.ultracosmetics.cosmetics.mounts.MountDruggedHorse;
import be.isach.ultracosmetics.cosmetics.mounts.MountEcologistHorse;
import be.isach.ultracosmetics.cosmetics.mounts.MountGlacialSteed;
import be.isach.ultracosmetics.cosmetics.mounts.MountHypeCart;
import be.isach.ultracosmetics.cosmetics.mounts.MountInfernalHorror;
import be.isach.ultracosmetics.cosmetics.mounts.MountMoltenSnake;
import be.isach.ultracosmetics.cosmetics.mounts.MountNyanSheep;
import be.isach.ultracosmetics.cosmetics.mounts.MountOfFire;
import be.isach.ultracosmetics.cosmetics.mounts.MountOfWater;
import be.isach.ultracosmetics.cosmetics.mounts.MountRudolph;
import be.isach.ultracosmetics.cosmetics.mounts.MountSnake;
import be.isach.ultracosmetics.cosmetics.mounts.MountWalkingDead;
import be.isach.ultracosmetics.util.ServerVersion;
import be.isach.ultracosmetics.version.VersionManager;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
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

    private final static List<MountType> ENABLED = new ArrayList<>();
    private final static List<MountType> VALUES = new ArrayList<>();

    public static List<MountType> enabled() {
        return ENABLED;
    }

    public static List<MountType> values() {
        return VALUES;
    }

    public static MountType valueOf(String s) {
        for (MountType mountType : VALUES) {
            if (mountType.getConfigName().equalsIgnoreCase(s)) return mountType;
        }
        return null;
    }

    public static void checkEnabled() {
        ENABLED.addAll(values().stream().filter(MountType::isEnabled).collect(Collectors.toList()));
    }

    private final int repeatDelay;
    private final List<XMaterial> defaultBlocks;
    private final double defaultSpeed;
    private final double movementSpeed;

    private MountType(String configName, XMaterial material, EntityType entityType, int repeatDelay, List<XMaterial> defaultBlocks, double defaultSpeed, Class<? extends Mount> mountClass) {
        super(Category.MOUNTS, configName, material, entityType, mountClass);
        this.repeatDelay = repeatDelay;
        this.defaultBlocks = defaultBlocks;
        this.defaultSpeed = defaultSpeed;
        this.movementSpeed = SettingsManager.getConfig().getDouble("Mounts." + configName + ".Speed", defaultSpeed);
        VALUES.add(this);
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

    public static void register(ServerVersion version) {
        VersionManager vm = UltraCosmeticsData.get().getVersionManager();
        new MountType("DruggedHorse", XMaterial.SUGAR, EntityType.HORSE, 2, null, 1.1, MountDruggedHorse.class);
        new MountType("GlacialSteed", XMaterial.PACKED_ICE, EntityType.HORSE, 2, Arrays.asList(XMaterial.SNOW_BLOCK), 0.4, MountGlacialSteed.class);
        new MountType("MountOfFire", XMaterial.BLAZE_POWDER, EntityType.HORSE, 2, Arrays.asList(XMaterial.ORANGE_TERRACOTTA, XMaterial.YELLOW_TERRACOTTA, XMaterial.RED_TERRACOTTA), 0.4, MountOfFire.class);
        new MountType("Snake", XMaterial.WHEAT_SEEDS, EntityType.SHEEP, 2, null, 0.3, MountSnake.class);
        new MountType("HypeCart", XMaterial.MINECART, EntityType.MINECART, 1, null, 0, MountHypeCart.class);
        new MountType("MoltenSnake", XMaterial.MAGMA_CREAM, EntityType.MAGMA_CUBE, 1, null, 0.4, MountMoltenSnake.class);
        new MountType("MountOfWater", XMaterial.LIGHT_BLUE_DYE, EntityType.HORSE, 2, Arrays.asList(XMaterial.LIGHT_BLUE_TERRACOTTA, XMaterial.CYAN_TERRACOTTA, XMaterial.BLUE_TERRACOTTA), 0.4, MountOfWater.class);
        new MountType("NyanSheep", XMaterial.CYAN_DYE, EntityType.SHEEP, 1, null, 0.4, MountNyanSheep.class);
        new MountType("EcologistHorse", XMaterial.GREEN_DYE, EntityType.HORSE, 2, Arrays.asList(XMaterial.LIME_TERRACOTTA, XMaterial.GREEN_TERRACOTTA), 0.4, MountEcologistHorse.class);
        new MountType("Rudolph", XMaterial.DEAD_BUSH, horseOrType("MULE", version), 1, null, 0.4, MountRudolph.class);
        new MountType("WalkingDead", XMaterial.ROTTEN_FLESH, horseOrType("ZOMBIE_HORSE", version), 2, null, 0.4, MountWalkingDead.class);
        new MountType("InfernalHorror", XMaterial.BONE, horseOrType("SKELETON_HORSE", version), 2, null, 0.4, MountInfernalHorror.class);

        if (version.isNmsSupported()) {
            new MountType("Slime", XMaterial.SLIME_BALL, EntityType.SLIME, 2, null, 0.8, vm.getModule().getSlimeClass());
            new MountType("Spider", XMaterial.COBWEB, EntityType.SPIDER, 2, null, 0.4, vm.getModule().getSpiderClass());
            new MountType("Dragon", XMaterial.DRAGON_EGG, EntityType.ENDER_DRAGON, 1, null, 0.7, MountDragon.class);
        }
    }

    private static EntityType horseOrType(String name, ServerVersion version) {
        if (version == ServerVersion.v1_8) return EntityType.HORSE;
        return EntityType.valueOf(name);
    }
}
