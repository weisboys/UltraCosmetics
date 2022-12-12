package be.isach.ultracosmetics.config;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

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
    private static final SettingsManager messagesConfig;
    // should be set to true by the time anybody else can read this
    private static boolean success = false;

    /**
     * Load the messages config
     */
    static {
        String langFile = "messages_" + UltraCosmeticsData.get().getLanguage();
        messagesConfig = new SettingsManager(langFile);
        if (messagesConfig.success()) {
            Reader reader = UltraCosmeticsData.get().getPlugin().getFileReader("messages/" + langFile + ".yml");
            loadMessages(YamlConfiguration.loadConfiguration(reader));
            messagesConfig.save();
            success = true;
        }
    }

    public static boolean success() {
        return success;
    }

    /**
     * Set up the messages in the config.
     */
    private static void loadMessages(YamlConfiguration defaults) {
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

        for (String key : defaults.getKeys(true)) {
            addMessage(key, defaults.getString(key));
        }
    }

    /**
     * Add a message in the messages.yml file.
     *
     * @param path    The config path.
     * @param message The config value.
     */
    public static void addMessage(String path, String message) {
        if (messagesConfig.addDefault(path, message)) {
            // Has its own if-block to avoid the dead code warning
            if (CosmeticType.GENERATE_MISSING_MESSAGES) {
                UltraCosmeticsData.get().getPlugin().getSmartLogger().write("Adding message " + path);
            }
        }
    }

    public static void save() {
        messagesConfig.save();
    }

    /**
     * Gets a message.
     *
     * @param messagePath The path of the message in the config.
     * @return a message from a config path.
     */
    public static String getMessage(String messagePath) {
        if (!messagesConfig.fileConfiguration.isString(messagePath)) {
            throw new IllegalArgumentException("No such message key: " + messagePath);
        }
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString(messagePath).replace("%prefix%", messagesConfig.getString("Prefix")));
    }

    private MessageManager() {
    }

    private static void upgradeCategoryStrings(ConfigurationSection menuBlock) {
        messagesConfig.set("Menu", null);
        Map<Category,Map<String,String>> buttons = new HashMap<>();
        Map<Category,String> menuNames = new HashMap<>();
        for (Category cat : Category.values()) {
            Map<String,String> catSection = new HashMap<>();
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
        for (Entry<Category,Map<String,String>> catMap : buttons.entrySet()) {
            messagesConfig.set("Menu." + catMap.getKey().getConfigPath() + ".Title", menuNames.get(catMap.getKey()));
            for (Entry<String,String> translation : catMap.getValue().entrySet()) {
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

    private static void addButton(Map<Category,Map<String,String>> buttons, ConfigurationSection menuBlock, Category cat, String oldEquipKey, String oldUnequipKey) {
        buttons.get(cat).put("Tooltip-Equip", menuBlock.getString(oldEquipKey));
        buttons.get(cat).put("Tooltip-Unequip", menuBlock.getString(oldUnequipKey));
    }

    private static void migrateMiscButton(ConfigurationSection section, String key) {
        messagesConfig.set("Menu.Misc.Button." + key, section.getString(key));
    }

    private static void migrateActivateMsg(Category cat, String oldEquipKey, String oldUnequipKey) {
        migrateKey(cat.getConfigPath() + "." + oldEquipKey, cat.getConfigPath() + ".Equip");
        migrateKey(cat.getConfigPath() + "." + oldUnequipKey, cat.getConfigPath() + ".Unequip");
    }

    private static void migrateClearMsg(String newKey, String oldKey) {
        migrateKey("Clear-" + oldKey, "Clear." + newKey);
    }

    private static void migrateKey(String oldKey, String newKey) {
        messagesConfig.set(newKey, messagesConfig.getString(oldKey));
        messagesConfig.set(oldKey, null);
    }

    public static void reload() {
        messagesConfig.reload();
    }
}
