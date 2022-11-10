package be.isach.ultracosmetics.cosmetics;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.EmoteType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.cosmetics.type.HatType;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.cosmetics.type.SuitType;
import be.isach.ultracosmetics.util.ItemFactory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Cosmetic category enum.
 *
 * @author iSach
 * @since 06-20-2016
 */
public enum Category {

    PETS("Pets", "%petname%", "pets", "pe", PetType::enabled, PetType::values, PetType::valueOf, () -> UltraCosmeticsData.get().getServerVersion().isMobChipAvailable()),
    GADGETS("Gadgets", "%gadgetname%", "gadgets", "g", GadgetType::enabled, GadgetType::values, GadgetType::valueOf),
    EFFECTS("Particle-Effects", "%effectname%", "particleeffects", "ef", ParticleEffectType::enabled, ParticleEffectType::values, ParticleEffectType::valueOf),
    MOUNTS("Mounts", "%mountname%", "mounts", "mou", MountType::enabled, MountType::values, MountType::valueOf),
    MORPHS("Morphs", "%morphname%", "morphs", "mor", MorphType::enabled, MorphType::values, MorphType::valueOf, () -> Bukkit.getPluginManager().isPluginEnabled("LibsDisguises")),
    HATS("Hats", "%hatname%", "hats", "h", HatType::enabled, HatType::values, HatType::valueOf),
    SUITS_HELMET(ArmorSlot.HELMET),
    SUITS_CHESTPLATE(ArmorSlot.CHESTPLATE),
    SUITS_LEGGINGS(ArmorSlot.LEGGINGS),
    SUITS_BOOTS(ArmorSlot.BOOTS),
    EMOTES("Emotes", "%emotename%", "emotes", "e", EmoteType::enabled, EmoteType::values, EmoteType::valueOf),
    ;

    // Avoids counting suit categories multiple times since they share settings
    public static int enabledSize() {
        int size = SUITS_HELMET.isEnabled() ? 1 : 0;
        for (Category cat : enabled()) {
            if (cat.isSuits()) continue;
            size += 1;
        }
        return size;
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

    public static void forEachCosmetic(Consumer<CosmeticType<?>> func) {
        for (Category cat : values()) {
            for (CosmeticType<?> type : cat.getValues()) {
                func.accept(type);
            }
        }
    }

    public static Category fromSlot(ArmorSlot slot) {
        return Category.valueOf("SUITS_" + slot.name());
    }

    /**
     * The config path name.
     */
    private final String configPath;

    private final String chatPlaceholder;
    private final String permission;
    private final String prefix;
    private final Supplier<List<? extends CosmeticType<?>>> enabledFunc;
    private final Supplier<List<? extends CosmeticType<?>>> valuesFunc;
    private final Function<String,? extends CosmeticType<?>> valueOfFunc;
    private final BooleanSupplier enableCondition;

    private Category(String configPath, String chatPlaceholder, String permission, String prefix, Supplier<List<? extends CosmeticType<?>>> enabledFunc,
            Supplier<List<? extends CosmeticType<?>>> valuesFunc, Function<String,? extends CosmeticType<?>> valueOfFunc, BooleanSupplier enableCondition) {
        this.configPath = configPath;
        this.chatPlaceholder = chatPlaceholder;
        this.permission = permission;
        this.prefix = prefix;
        this.enabledFunc = enabledFunc;
        this.valuesFunc = valuesFunc;
        this.valueOfFunc = valueOfFunc;
        this.enableCondition = enableCondition;
    }

    private Category(String configPath, String chatPlaceholder, String permission, String prefix, Supplier<List<? extends CosmeticType<?>>> enabledFunc,
            Supplier<List<? extends CosmeticType<?>>> valuesFunc, Function<String,? extends CosmeticType<?>> valueOfFunc) {
        this(configPath, chatPlaceholder, permission, prefix, enabledFunc, valuesFunc, valueOfFunc, () -> true);
    }

    private Category(ArmorSlot slot) {
        this("Suits", "%suitname%", "suits", "suits_" + slot.toString().toLowerCase().charAt(0), SuitType::enabled, SuitType::values, s -> SuitType.getSuitPart(s, slot));
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
        return enableCondition.getAsBoolean() && SettingsManager.getConfig().getBoolean("Categories-Enabled." + configPath);
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

    public List<? extends CosmeticType<?>> getValues() {
        return valuesFunc.get();
    }

    public CosmeticType<?> valueOfType(String name) {
        if (name == null) return null;
        return valueOfFunc.apply(name);
    }

    public boolean isSuits() {
        return name().startsWith("SUITS_");
    }
}
