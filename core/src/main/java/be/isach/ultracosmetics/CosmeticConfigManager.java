package be.isach.ultracosmetics;

import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.EmoteType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.cosmetics.type.HatType;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.cosmetics.type.SuitCategory;
import be.isach.ultracosmetics.util.ServerVersion;

import org.bukkit.entity.LivingEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cosmetic manager.
 *
 * @author iSach
 * @since 08-09-2016
 */
public class CosmeticConfigManager {

    private UltraCosmetics ultraCosmetics;

    public CosmeticConfigManager(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
    }

    /**
     * Setup default Cosmetics config.
     */
    public void setupCosmeticsConfigs() {
        CustomConfiguration config = ultraCosmetics.getConfig();
        for (Category category : Category.values()) {
            config.addDefault("Categories-Enabled." + category.getConfigPath(), true);
            config.addDefault("Categories." + category.getConfigPath() + ".Go-Back-Arrow", true, "Want Go back To Menu Item in that menu?");
        }

        config.addDefault("TreasureChests.Loots.Emotes.Enabled", true);
        config.addDefault("TreasureChests.Loots.Emotes.Chance", 5);
        config.addDefault("TreasureChests.Loots.Emotes.Message.enabled", true);
        config.addDefault("TreasureChests.Loots.Emotes.Message.message", "%prefix% &6&l%name% found rare %emote%");
        config.addDefault("Ammo-System-For-Gadgets.Show-Ammo-In-Menu-As-Item-Amount", true, "Do you want that in the gadgets menu", "each gadget item has an amount", "corresponding to your ammo.");

        ServerVersion version = UltraCosmeticsData.get().getServerVersion();
        GadgetType.register(version);
        MountType.register(version);
        ParticleEffectType.register(version);
        PetType.register(version);
        HatType.register();
        for (SuitCategory sc : SuitCategory.values()) {
            sc.initializeSuitParts();
        }
        MorphType.register();
        EmoteType.register();

        for (CosmeticType<?> type : CosmeticType.valuesOf(Category.GADGETS)) {
            GadgetType gadgetType = (GadgetType) type;
            setupCosmetic(config, gadgetType);
            if (gadgetType.getConfigName().equalsIgnoreCase("paintballgun")) {
                // default "" so we don't have to deal with null
                if (config.getString("Gadgets." + gadgetType.getConfigName() + ".Block-Type", "").equals("STAINED_CLAY")) {
                    config.set("Gadgets." + gadgetType.getConfigName() + ".Block-Type", "_TERRACOTTA", "With what block will it paint?", "Uses all blocks that end with the supplied string. For values, see:", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                }
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Block-Type", "_TERRACOTTA", "With what block will it paint?", "Uses all blocks that end with the supplied string. For values, see:", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Particle.Enabled", false, "Should it display particles?");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Particle.Effect", "FIREWORKS_SPARK", "what particles? (List: http://pastebin.com/CVKkufck)");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Radius", 2, "The radius of painting.");
                List<String> blackListedBlocks = new ArrayList<>();
                blackListedBlocks.add("REDSTONE_BLOCK");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".BlackList", blackListedBlocks, "A list of the BLOCKS that", "can't be painted.");
            }
            if (UltraCosmeticsData.get().isAmmoEnabled()) {
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Ammo.Enabled", true, "You want this gadget to need ammo?");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Ammo.Price", 500, "What price for the ammo?");
                config.addDefault("Gadgets." + gadgetType.getConfigName() + ".Ammo.Result-Amount", 20, "And how much ammo is given", "when bought?");
            }
        }

        for (CosmeticType<?> type : CosmeticType.valuesOf(Category.MOUNTS)) {
            MountType mountType = (MountType) type;
            setupCosmetic(config, mountType);
            // If the mount type has a movement speed (is LivingEntity)
            if (LivingEntity.class.isAssignableFrom(mountType.getEntityType().getEntityClass())) {
                config.addDefault("Mounts." + mountType.getConfigName() + ".Speed", mountType.getDefaultMovementSpeed(), "The movement speed of the mount, see:", "https://minecraft.fandom.com/wiki/Attribute#Attributes_available_on_all_living_entities");
            }
            if (mountType.doesPlaceBlocks()) {
                // Don't use Stream#toList(), it doesn't exist in Java 8
                config.addDefault("Mounts." + mountType.getConfigName() + ".Blocks-To-Place", mountType.getDefaultBlocks().stream().map(m -> m.name()).collect(Collectors.toList()), "Blocks to choose from as this mount walks.");
            }
        }
        config.addDefault("Mounts.Dragon.Stationary", false, "If true, the dragon will not move.");
        for (SuitCategory suit : SuitCategory.values()) {
            setupCosmetic(config, suit.getConfigPath());
        }
        config.addDefault("Suits.Rave.Update-Delay-In-Creative", 10,
                "How many ticks UC should wait between updating the rave suit for creative players.",
                "Setting this to a higher value allows more time between updates,",
                "meaning players shouldn't have their inventories close immediately after opening.",
                "Set to 1 or less to update every tick.");

        if (Category.MORPHS.isEnabled()) {
            setupCategory(config, CosmeticType.valuesOf(Category.MORPHS));
        }
        setupCategory(config, CosmeticType.valuesOf(Category.PETS));
        config.addDefault("Pets.Axolotl.Fast", false, "https://imgur.com/a/EKWwQ6w");
        config.addDefault("Pets.Wither.Bossbar", "in range",
                "Sets who the bossbar is visible for. (Has no effect on 1.8)",
                "'in range': vanilla behavior, visible to all players in range.",
                "'owner': only visible to pet owner",
                "'none': not visible to any players");
        config.addDefault("Pets.Warden.Block-Effect", true,
                "Whether the darkness effect is blocked while this pet is equipped.",
                "Please note that this will also block darkness from real wardens,",
                "due to Spigot API limitations.");
        config.addDefault("Pets.ElderGuardian.Block-Effect", true,
                "Whether the mining fatigue effect is blocked while this pet is equipped.",
                "Please note that this will also block mining fatigue from real elder guardians,",
                "due to Spigot API limitations.");
        setupCategory(config, CosmeticType.valuesOf(Category.HATS));
        setupCategory(config, CosmeticType.valuesOf(Category.EMOTES));
        setupCategory(config, CosmeticType.valuesOf(Category.EFFECTS));

        try {
            config.save(ultraCosmetics.getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCategory(CustomConfiguration config, List<? extends CosmeticType<?>> types) {
        for (CosmeticType<?> type : types) {
            setupCosmetic(config, type);
        }
    }

    private void setupCosmetic(CustomConfiguration config, CosmeticType<?> type) {
        setupCosmetic(config, type.getConfigPath());
        if (PlayerAffectingCosmetic.class.isAssignableFrom(type.getClazz())) {
            config.addDefault(type.getCategory().getConfigPath() + "." + type.getConfigName() + ".Affect-Players", true, "Should it affect players? (Velocity, etc.)");
        }
    }

    private void setupCosmetic(CustomConfiguration config, String path) {
        // If someone can come up with better comments for these please do, but they're pretty self-explanatory
        config.addDefault(path + ".Enabled", true);
        config.addDefault(path + ".Show-Description", true, "Whether to show description when hovering in GUI");
        String findableKey = path + ".Can-Be-Found-In-Treasure-Chests";
        int weight = 1;
        if (config.isBoolean(findableKey)) {
            weight = config.getBoolean(findableKey) ? 1 : 0;
            config.set(findableKey, null);
        }
        config.addDefault(path + ".Treasure-Chest-Weight", weight, "The higher the weight, the better the chance of", "finding this cosmetic when this category is picked.", "Fractional values are not allowed.", "Set to 0 to disable finding in chests.");
        config.addDefault(path + ".Purchase-Price", 500, "Price to buy individually in GUI", "Only works if No-Permission.Allow-Purchase is true and this setting > 0");
    }
}
