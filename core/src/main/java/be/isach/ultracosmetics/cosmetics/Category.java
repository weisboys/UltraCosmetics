package be.isach.ultracosmetics.cosmetics;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.EmoteType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.cosmetics.type.HatType;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.cosmetics.type.SuitCategory;
import be.isach.ultracosmetics.cosmetics.type.SuitType;
import be.isach.ultracosmetics.util.ItemFactory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Cosmetic category enum.
 *
 * @author iSach
 * @since 06-20-2016
 */
public enum Category {

    PETS("Pets", "%petname%", "pets", "pe", () -> PetType.enabled(), () -> UltraCosmeticsData.get().getServerVersion().isNmsSupported()),
    GADGETS("Gadgets", "%gadgetname%", "gadgets", "g", () -> GadgetType.enabled()),
    EFFECTS("Particle-Effects", "%effectname%", "particleeffects", "ef", () -> ParticleEffectType.enabled()),
    MOUNTS("Mounts", "%mountname%", "mounts", "mou", () -> MountType.enabled()),
    MORPHS("Morphs", "%morphname%", "morphs", "mor", () -> MorphType.enabled(), () -> Bukkit.getPluginManager().isPluginEnabled("LibsDisguises")),
    HATS("Hats", "%hatname%", "hats", "h", () -> HatType.enabled()),
    SUITS("Suits", "%suitname%", "suits", "s", () -> SuitType.enabled()),
    EMOTES("Emotes", "%emotename%", "emotes", "e", () -> EmoteType.enabled());

    public static int enabledSize() {
        return enabled().size();
    }

    public static List<Category> enabled() {
        return Arrays.stream(values()).filter(Category::isEnabled).collect(Collectors.toList());
    }

    public static Category fromString(String name) {
        String lowerName = name.toLowerCase();
        for (Category cat : values()) {
            if (lowerName.startsWith(cat.prefix)) {
                return cat;
            }
        }
        return null;
    }

    public CosmeticType<?> valueOfType(String name) {
        if (name == null) return null;
        switch (this) {
        case EFFECTS:
            return ParticleEffectType.valueOf(name);
        case EMOTES:
            return EmoteType.valueOf(name);
        case GADGETS:
            return GadgetType.valueOf(name);
        case HATS:
            return HatType.valueOf(name);
        case MORPHS:
            return MorphType.valueOf(name);
        case MOUNTS:
            return MountType.valueOf(name);
        case PETS:
            return PetType.valueOf(name);
        case SUITS:
            // at least return something
            return SuitCategory.valueOf(name).getHelmet();
        }
        return null;
    }

    /**
     * The config path name.
     */
    private final String configPath;

    private final String chatPlaceholder;
    private final String permission;
    private final String prefix;
    private final Supplier<List<? extends CosmeticType<?>>> enabledFunc;
    private final Supplier<Boolean> enableCondition;

    /**
     * Category of Cosmetic.
     *
     * @param configPath      The config path name.
     * @param chatPlaceholder
     * @param prefix          TODO
     */
    private Category(String configPath, String chatPlaceholder, String permission, String prefix, Supplier<List<? extends CosmeticType<?>>> enabledFunc, Supplier<Boolean> enableCondition) {
        this.configPath = configPath;
        this.chatPlaceholder = chatPlaceholder;
        this.permission = permission;
        this.prefix = prefix;
        this.enabledFunc = enabledFunc;
        this.enableCondition = enableCondition;
    }

    private Category(String configPath, String chatPlaceholder, String permission, String prefix, Supplier<List<? extends CosmeticType<?>>> enabledFunc) {
        this(configPath, chatPlaceholder, permission, prefix, enabledFunc, () -> true);
    }

    /**
     * Gets the ItemStack in Main Menu.
     *
     * @return The ItemStack in Main Menu.
     */
    public ItemStack getItemStack() {
        ItemStack is;
        if (SettingsManager.getConfig().contains("Categories." + configPath + ".Main-Menu-Item")) {
            is = ItemFactory.getItemStackFromConfig("Categories." + configPath + ".Main-Menu-Item");
        } else {
            // TODO: not sure when this is used?
            is = ItemFactory.createSkull("5059d59eb4e59c31eecf9ece2f9cf3934e45c0ec476fc86bfaef8ea913ea710", "");
        }
        ItemMeta itemMeta = is.getItemMeta();
        itemMeta.setDisplayName(MessageManager.getMessage("Menu." + configPath + ".Button.Name"));
        is.setItemMeta(itemMeta);
        return is;
    }

    /**
     * Checks if the category is enabled.
     *
     * @return {@code true} if enabled, otherwise {@code false}.
     */
    public boolean isEnabled() {
        return enableCondition.get() && SettingsManager.getConfig().getBoolean("Categories-Enabled." + configPath);
    }

    /**
     * Checks if the category should have a back arrow in its menu.
     *
     * @return {@code true} if has arrow, otherwise {@code false}
     */
    public boolean hasGoBackArrow() {
        return !(!UltraCosmeticsData.get().areTreasureChestsEnabled() && enabledSize() == 1)
                && SettingsManager.getConfig().getBoolean("Categories." + configPath + ".Go-Back-Arrow");
    }

    /**
     * @return Config Path.
     */
    public String getConfigPath() {
        return configPath;
    }

    public String getMessagesName() {
        // Like configPath but value is different for Category.EFFECTS
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public String getActivateTooltip() {
        return MessageManager.getMessage("Menu." + configPath + ".Button.Tooltip-Equip");
    }

    public String getDeactivateTooltip() {
        return MessageManager.getMessage("Menu." + configPath + ".Button.Tooltip-Unequip");
    }

    public String getChatPlaceholder() {
        return chatPlaceholder;
    }

    public String getPermission() {
        return "ultracosmetics." + permission;
    }

    public String getActivateMessage() {
        return MessageManager.getMessage(configPath + ".Equip");
    }

    public String getDeactivateMessage() {
        return MessageManager.getMessage(configPath + ".Unequip");
    }

    public List<? extends CosmeticType<?>> getEnabled() {
        return enabledFunc.get();
    }
}
