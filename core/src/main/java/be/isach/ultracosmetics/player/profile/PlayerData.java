package be.isach.ultracosmetics.player.profile;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.mysql.MySqlConnectionManager;
import be.isach.ultracosmetics.mysql.tables.PlayerDataTable;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private int keys;
    private boolean gadgetsEnabled;
    private boolean morphSelfView;
    private boolean treasureNotifications;
    private boolean filterByOwned;
    private Map<PetType,String> petNames = new HashMap<>();
    private Map<GadgetType,Integer> ammo = new HashMap<>();
    private Map<Category,CosmeticType<?>> enabledCosmetics = new HashMap<>();
    private Set<CosmeticType<?>> unlockedCosmetics = new HashSet<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public boolean isGadgetsEnabled() {
        return gadgetsEnabled;
    }

    public void setGadgetsEnabled(boolean gadgetsEnabled) {
        this.gadgetsEnabled = gadgetsEnabled;
    }

    public boolean isMorphSelfView() {
        return morphSelfView;
    }

    public void setMorphSelfView(boolean morphSelfView) {
        this.morphSelfView = morphSelfView;
    }

    public boolean isTreasureNotifications() {
        return treasureNotifications;
    }

    public void setTreasureNotifications(boolean treasureNotifications) {
        this.treasureNotifications = treasureNotifications;
    }

    public boolean isFilterByOwned() {
        return filterByOwned;
    }

    public void setFilterByOwned(boolean filterByOwned) {
        this.filterByOwned = filterByOwned;
    }

    public Map<PetType,String> getPetNames() {
        return petNames;
    }

    public Map<GadgetType,Integer> getAmmo() {
        return ammo;
    }

    public Map<Category,CosmeticType<?>> getEnabledCosmetics() {
        return enabledCosmetics;
    }

    public Set<CosmeticType<?>> getUnlockedCosmetics() {
        return unlockedCosmetics;
    }

    /**
     * Loads the profile from the player file.
     */
    public void loadFromFile() {
        SettingsManager sm = SettingsManager.getData(uuid);
        if (UltraCosmeticsData.get().areCosmeticsProfilesEnabled() && sm.fileConfiguration.isConfigurationSection("enabled")) {
            cosmeticsFromFile(sm);
        }

        for (PetType pet : PetType.enabled()) {
            petNames.put(pet, sm.getString(ProfileKey.PET_NAMES.getFileKey() + "." + pet.getConfigName()));
        }

        for (GadgetType gadget : GadgetType.enabled()) {
            ammo.put(gadget, sm.getInt(ProfileKey.AMMO.getFileKey() + "." + gadget.getConfigName().toLowerCase()));
        }

        keys = sm.getInt(ProfileKey.KEYS.getFileKey());
        gadgetsEnabled = sm.getBoolean(ProfileKey.GADGETS_ENABLED.getFileKey(), true);
        morphSelfView = sm.getBoolean(ProfileKey.MORPH_VIEW.getFileKey(), true);
        treasureNotifications = sm.getBoolean(ProfileKey.TREASURE_NOTIFICATION.getFileKey(), true);
        filterByOwned = sm.getBoolean(ProfileKey.FILTER_OWNED.getFileKey(), false);
    }

    private void cosmeticsFromFile(SettingsManager sm) {
        ConfigurationSection s = sm.fileConfiguration.getConfigurationSection("enabled");
        boolean changed = false;
        for (Category cat : Category.values()) {
            String key = cat.toString().toLowerCase();
            String oldKey = key.substring(0, key.length() - 1);
            String value;
            if (s.isString(oldKey)) {
                value = s.getString(oldKey);
                s.set(key, value);
                s.set(oldKey, null);
                changed = true;
            } else {
                value = s.getString(key);
            }
            if (value == null || value.equals("none")) continue;
            enabledCosmetics.put(cat, cat.valueOfType(value));
        }
        if (changed) sm.save();
    }

    public void saveToFile() {
        SettingsManager data = SettingsManager.getData(uuid);

        data.set(ProfileKey.KEYS.getFileKey(), keys);
        data.set(ProfileKey.GADGETS_ENABLED.getFileKey(), gadgetsEnabled);
        data.set(ProfileKey.MORPH_VIEW.getFileKey(), morphSelfView);
        data.set(ProfileKey.TREASURE_NOTIFICATION.getFileKey(), treasureNotifications);
        data.set(ProfileKey.FILTER_OWNED.getFileKey(), filterByOwned);

        for (Category cat : Category.enabled()) {
            CosmeticType<?> type = enabledCosmetics.get(cat);
            data.set("enabled." + cat.toString().toLowerCase(), type == null ? null : type.getConfigName().toLowerCase());
        }

        for (Entry<PetType,String> entry : petNames.entrySet()) {
            data.set(ProfileKey.PET_NAMES.getFileKey() + "." + entry.getKey().getConfigName(), entry.getValue());
        }

        for (Entry<GadgetType,Integer> entry : ammo.entrySet()) {
            Integer amount = entry.getValue();
            // carefully handled because numeric comparisons auto-unbox but null checks do not
            if (amount != null && amount == 0) amount = null;
            data.set(ProfileKey.AMMO.getFileKey() + "." + entry.getKey().getConfigName().toLowerCase(), amount);
        }
        data.save();
    }

    public void loadFromSQL() {
        UltraCosmeticsData data = UltraCosmeticsData.get();
        MySqlConnectionManager sql = data.getPlugin().getMySqlConnectionManager();
        // update table with UUID. If it's already there, ignore
        PlayerDataTable pd = sql.getPlayerData();
        pd.addPlayer(uuid);
        Map<String,Object> settings = pd.getSettings(uuid);
        gadgetsEnabled = (boolean) settings.get(ProfileKey.GADGETS_ENABLED.getSqlKey());
        morphSelfView = (boolean) settings.get(ProfileKey.MORPH_VIEW.getSqlKey());
        treasureNotifications = (boolean) settings.get(ProfileKey.TREASURE_NOTIFICATION.getSqlKey());
        filterByOwned = (boolean) settings.get(ProfileKey.FILTER_OWNED.getSqlKey());
        keys = (int) settings.get(ProfileKey.KEYS.getSqlKey());
        petNames = sql.getPetNames().getAllPetNames(uuid);
        if (data.isAmmoEnabled()) {
            ammo = sql.getAmmoTable().getAllAmmo(uuid);
        }
        if (data.areCosmeticsProfilesEnabled()) {
            enabledCosmetics = sql.getEquippedTable().getEquipped(uuid);
        }
        if (data.getPlugin().getPermissionManager().isUsingSQL()) {
            unlockedCosmetics = sql.getUnlockedTable().getAllUnlocked(uuid);
        }
    }

    // Only used for migration; SQL normally saves on write
    public void saveToSQL() {
        MySqlConnectionManager sql = UltraCosmeticsData.get().getPlugin().getMySqlConnectionManager();
        PlayerDataTable pd = sql.getPlayerData();
        // should have been added on load but just to be safe
        pd.addPlayer(uuid);
        pd.setKeys(uuid, keys);
        pd.setSetting(uuid, ProfileKey.GADGETS_ENABLED, gadgetsEnabled);
        pd.setSetting(uuid, ProfileKey.MORPH_VIEW, morphSelfView);
        pd.setSetting(uuid, ProfileKey.TREASURE_NOTIFICATION, treasureNotifications);
        pd.setSetting(uuid, ProfileKey.FILTER_OWNED, filterByOwned);

        sql.getPetNames().setAllPetNames(uuid, petNames);
        sql.getAmmoTable().setAllAmmo(uuid, ammo);
        sql.getEquippedTable().setAllEquipped(uuid, enabledCosmetics);
        sql.getUnlockedTable().setAllUnlocked(uuid, unlockedCosmetics);
    }
}
