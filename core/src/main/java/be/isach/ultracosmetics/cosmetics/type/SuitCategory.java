package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.suits.Suit;
import be.isach.ultracosmetics.cosmetics.suits.SuitAstronaut;
import be.isach.ultracosmetics.cosmetics.suits.SuitDiamond;
import be.isach.ultracosmetics.cosmetics.suits.SuitFrozen;
import be.isach.ultracosmetics.cosmetics.suits.SuitRave;
import be.isach.ultracosmetics.cosmetics.suits.SuitSanta;

import com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SuitCategory {
    RAVE("Rave", "rave", XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS, SuitRave.class),
    ASTRONAUT("Astronaut", "astronaut", XMaterial.GLASS, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.GOLDEN_BOOTS, SuitAstronaut.class),
    DIAMOND("Diamond", "diamond", XMaterial.DIAMOND_HELMET, XMaterial.DIAMOND_CHESTPLATE, XMaterial.DIAMOND_LEGGINGS, XMaterial.DIAMOND_BOOTS, SuitDiamond.class),
    SANTA("Santa", "santa", XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS, SuitSanta.class),
    FROZEN("Frozen", "frozen", XMaterial.PACKED_ICE, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS, SuitFrozen.class),
    ;

    private final String configName;
    private final String permissionSuffix;
    private final XMaterial helmetType;
    private final XMaterial chestplateType;
    private final XMaterial leggingsType;
    private final XMaterial bootsType;
    private SuitType helmet;
    private SuitType chestplate;
    private SuitType leggings;
    private SuitType boots;
    private final Class<? extends Suit> clazz;

    private SuitCategory(String configName, String permissionSuffix, XMaterial helmet,
            XMaterial chestplate, XMaterial leggings, XMaterial boots, Class<? extends Suit> clazz) {
        this.configName = configName;
        this.permissionSuffix = permissionSuffix;
        this.clazz = clazz;
        this.helmetType = helmet;
        this.chestplateType = chestplate;
        this.leggingsType = leggings;
        this.bootsType = boots;
    }

    public void initializeSuitParts() {
        this.helmet = new SuitType(helmetType, ArmorSlot.HELMET, this);
        this.chestplate = new SuitType(chestplateType, ArmorSlot.CHESTPLATE, this);
        this.leggings = new SuitType(leggingsType, ArmorSlot.LEGGINGS, this);
        this.boots = new SuitType(bootsType, ArmorSlot.BOOTS, this);
    }

    public String getConfigName() {
        return configName;
    }

    public String getConfigPath() {
        return helmet.getConfigPath();
    }

    public String getPermissionSuffix() {
        return permissionSuffix;
    }

    public Class<? extends Suit> getSuitClass() {
        return clazz;
    }

    public SuitType getHelmet() {
        return helmet;
    }

    public SuitType getChestplate() {
        return chestplate;
    }

    public SuitType getLeggings() {
        return leggings;
    }

    public SuitType getBoots() {
        return boots;
    }

    public boolean isEnabled() {
        return SettingsManager.getConfig().getBoolean("Suits." + configName + ".Enabled");
    }

    public SuitType getPiece(ArmorSlot slot) {
        switch (slot) {
        case HELMET:
            return getHelmet();
        case CHESTPLATE:
        default:
            return getChestplate();
        case LEGGINGS:
            return getLeggings();
        case BOOTS:
            return getBoots();
        }
    }

    public List<SuitType> getPieces() {
        return Arrays.asList(getHelmet(), getChestplate(), getLeggings(), getBoots());
    }

    public static List<SuitCategory> enabled() {
        List<SuitCategory> enabled = new ArrayList<>();
        for (SuitCategory cat : values()) {
            enabled.add(cat);
        }
        return enabled;
    }
}
