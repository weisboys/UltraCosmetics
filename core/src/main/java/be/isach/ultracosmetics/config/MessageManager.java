package be.isach.ultracosmetics.config;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.util.SmartLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Message manager.
 *
 * @author iSach
 * @since 03-08-2015
 */
public class MessageManager {
    private static MessageManager instance;
    private final SettingsManager messagesConfig;
    private final MiniMessage minimessage;
    private final BukkitAudiences audiences = BukkitAudiences.create(UltraCosmeticsData.get().getPlugin());
    // should be set to true by the time anybody else can read this
    private boolean success = false;

    /*
      Load the messages config
     */
    private MessageManager() {
        String langFile = "messages_" + UltraCosmeticsData.get().getLanguage();
        messagesConfig = new SettingsManager(langFile);
        if (!messagesConfig.success()) {
            minimessage = null;
            return;
        }
        Reader reader = UltraCosmeticsData.get().getPlugin().getFileReader("messages/" + langFile + ".yml");
        loadMessages(YamlConfiguration.loadConfiguration(reader));
        messagesConfig.save();
        minimessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.font())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.newline())
                        .resolver(StandardTags.rainbow())
                        .resolver(StandardTags.reset())
                        .build())
                .tags(Placeholder.component("prefix", Component.text(messagesConfig.getString("Prefix"))))
                .build();
        success = true;
    }

    private @NotNull Component getMessageInternal(String messagePath, TagResolver.Single... placeholders) {
        if (!messagesConfig.fileConfiguration.isString(messagePath)) {
            throw new IllegalArgumentException("No such message key: " + messagePath);
        }
        return minimessage.deserialize(messagesConfig.getString(messagePath), placeholders);
    }

    /**
     * Set up the messages in the config.
     */
    private void loadMessages(YamlConfiguration defaults) {
        ConfigurationSection menuBlock = messagesConfig.getConfigurationSection("Menu");
        if (menuBlock != null && menuBlock.isString("Gadgets")) {
            upgradeCategoryStrings(menuBlock);
        }

        String oldGadgetKey = "Treasure-Chests-Loot.gadget";
        String newGadgetKey = "Treasure-Chests-Loot.Gadget";
        String gadgetMessage = messagesConfig.getString(oldGadgetKey);
        if (gadgetMessage != null) {
            messagesConfig.set(newGadgetKey, gadgetMessage);
            messagesConfig.set(oldGadgetKey, null);
        }
        if (messagesConfig.getString("Prefix") != null && messagesConfig.getString("Prefix").startsWith("&")) {
            minimessageMigration();
        }
        for (String key : defaults.getKeys(true)) {
            addMessage(key, defaults.getString(key));
        }
    }

    private void upgradeCategoryStrings(ConfigurationSection menuBlock) {
        messagesConfig.set("Menu", null);
        Map<Category, Map<String, String>> buttons = new HashMap<>();
        Map<Category, String> menuNames = new HashMap<>();
        for (Category cat : Category.values()) {
            Map<String, String> catSection = new HashMap<>();
            catSection.put("Name", menuBlock.getString(cat.getConfigPath()));
            buttons.put(cat, catSection);
            menuNames.put(cat, messagesConfig.getString("Menus." + cat.getConfigPath()));
        }
        addButton(buttons, menuBlock, Category.PETS, "Spawn", "Despawn");
        addButton(buttons, menuBlock, Category.GADGETS, "Activate", "Deactivate");
        addButton(buttons, menuBlock, Category.EFFECTS, "Summon", "Unsummon");
        addButton(buttons, menuBlock, Category.MOUNTS, "Spawn", "Despawn");
        addButton(buttons, menuBlock, Category.MORPHS, "Morph", "Unmorph");
        addButton(buttons, menuBlock, Category.HATS, "Equip", "Unequip");
        addButton(buttons, menuBlock, Category.SUITS_HELMET, "Equip", "Unequip");
        addButton(buttons, menuBlock, Category.EMOTES, "Equip", "Unequip");
        for (Entry<Category, Map<String, String>> catMap : buttons.entrySet()) {
            messagesConfig.set("Menu." + catMap.getKey().getConfigPath() + ".Title", menuNames.get(catMap.getKey()));
            for (Entry<String, String> translation : catMap.getValue().entrySet()) {
                messagesConfig.set("Menu." + catMap.getKey().getConfigPath() + ".Button." + translation.getKey(), translation.getValue());
            }
        }
        messagesConfig.set("Menu.Main.Button.Name", menuBlock.getString("Main-Menu"));
        migrateKey("Menus.Main-Menu", "Menu.Main.Title");
        migrateKey("Menus.Buy-Ammo", "Menu.Buy-Ammo.Title");
        migrateKey("Menus.Rename-Pet", "Menu.Purchase-Rename.Title");
        migrateKey("Rename-Pet-Purchase", "Menu.Purchase-Rename.Button.Showcase");
        migrateKey("Rename-Pet", "Menu.Rename-Pet.Button.Name");
        migrateKey("Rename-Pet-Placeholder", "Menu.Rename-Pet.Placeholder");
        migrateKey("Rename-Pet-Title", "Menu.Rename-Pet.Title");

        migrateMiscButton(menuBlock, "Previous-Page");
        migrateMiscButton(menuBlock, "Next-Page");
        messagesConfig.set("Menus", null);

        // Only categories that don't already use "Equip" path
        migrateActivateMsg(Category.PETS, "Spawn", "Despawn");
        migrateActivateMsg(Category.EFFECTS, "Summon", "Unsummon");
        migrateActivateMsg(Category.MOUNTS, "Spawn", "Despawn");
        migrateActivateMsg(Category.MORPHS, "Morph", "Unmorph");

        migrateClearMsg("Cosmetics", "Cosmetics");
        for (Category cat : Category.values()) {
            String configName = cat.getMessagesName();
            migrateClearMsg(cat.getConfigPath(), configName.substring(0, configName.length() - 1));
        }
    }

    private void addButton(Map<Category, Map<String, String>> buttons, ConfigurationSection menuBlock, Category cat, String oldEquipKey, String oldUnequipKey) {
        buttons.get(cat).put("Tooltip-Equip", menuBlock.getString(oldEquipKey));
        buttons.get(cat).put("Tooltip-Unequip", menuBlock.getString(oldUnequipKey));
    }

    private void migrateMiscButton(ConfigurationSection section, String key) {
        messagesConfig.set("Menu.Misc.Button." + key, section.getString(key));
    }

    private void migrateActivateMsg(Category cat, String oldEquipKey, String oldUnequipKey) {
        migrateKey(cat.getConfigPath() + "." + oldEquipKey, cat.getConfigPath() + ".Equip");
        migrateKey(cat.getConfigPath() + "." + oldUnequipKey, cat.getConfigPath() + ".Unequip");
    }

    private void migrateClearMsg(String newKey, String oldKey) {
        migrateKey("Clear-" + oldKey, "Clear." + newKey);
    }

    private void migrateKey(String oldKey, String newKey) {
        messagesConfig.set(newKey, messagesConfig.getString(oldKey));
        messagesConfig.set(oldKey, null);
    }

    private void minimessageMigration() {
        SmartLogger log = UltraCosmeticsData.get().getPlugin().getSmartLogger();
        log.write(SmartLogger.LogLevel.WARNING, "Your messages file is using legacy color codes, it will be upgraded now");
        ConfigurationSection config = messagesConfig.fileConfiguration;
        LegacyComponentSerializer deserializer = LegacyComponentSerializer.legacyAmpersand();
        for (String key : config.getKeys(true)) {
            config.set(key, minimessage.serialize(deserializer.deserialize(config.getString(key))));
        }
        save();
    }

    public static boolean load() {
        destroy();
        instance = new MessageManager();
        return instance.success;
    }

    private static MessageManager getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    /**
     * Add a message in the messages.yml file.
     *
     * @param path    The config path.
     * @param message The config value.
     */
    public static void addMessage(String path, String message) {
        if (getInstance().messagesConfig.addDefault(path, message)) {
            // Has its own if-block to avoid the dead code warning
            if (CosmeticType.GENERATE_MISSING_MESSAGES) {
                UltraCosmeticsData.get().getPlugin().getSmartLogger().write("Adding message " + path);
            }
        }
    }

    /**
     * Gets a message.
     *
     * @param messagePath The path of the message in the config.
     * @return a message from a config path.
     */
    public static @NotNull Component getMessage(String messagePath, TagResolver.Single... placeholders) {
        return getInstance().getMessageInternal(messagePath, placeholders);
    }

    public static String toLegacy(Component component) {
        return BukkitComponentSerializer.legacy().serialize(component);
    }

    public static String getLegacyMessage(String messagePath, TagResolver.Single... placeholders) {
        return toLegacy(getMessage(messagePath, placeholders));
    }

    public static void send(CommandSender sender, String messagePath, TagResolver.Single... placeholders) {
        getInstance().audiences.sender(sender).sendMessage(getMessage(messagePath, placeholders));
    }

    public static BukkitAudiences getAudiences() {
        return getInstance().audiences;
    }

    public static MiniMessage getMiniMessage() {
        return getInstance().minimessage;
    }

    public static void save() {
        getInstance().messagesConfig.save();
    }

    public static void destroy() {
        if (instance != null) {
            instance.audiences.close();
            instance = null;
        }
    }
}
