package be.isach.ultracosmetics.cosmetics.suits;

import be.isach.ultracosmetics.cosmetics.Category;

import org.bukkit.ChatColor;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Armor slot enum.
 *
 * @author iSach
 * @since 12-20-2015
 */
public enum ArmorSlot {
    HELMET(EquipmentSlot.HEAD, Category.SUITS_HELMET),
    CHESTPLATE(EquipmentSlot.CHEST, Category.SUITS_CHESTPLATE),
    LEGGINGS(EquipmentSlot.LEGS, Category.SUITS_LEGGINGS),
    BOOTS(EquipmentSlot.FEET, Category.SUITS_BOOTS);

    private final EquipmentSlot bukkitSlot;
    private final Category category;

    private ArmorSlot(EquipmentSlot bukkitSlot, Category category) {
        this.bukkitSlot = bukkitSlot;
        this.category = category;
    }

    public EquipmentSlot toBukkit() {
        return bukkitSlot;
    }

    public Category getSuitsCategory() {
        return category;
    }

    public static ArmorSlot getByName(String s) {
        for (ArmorSlot a : ArmorSlot.values()) {
            if (a.toString().equalsIgnoreCase(ChatColor.stripColor(s))) return a;
        }
        return ArmorSlot.CHESTPLATE;
    }

    public static ArmorSlot getByCategory(Category cat) {
        for (ArmorSlot slot : values()) {
            if (slot.category == cat) {
                return slot;
            }
        }
        return null;
    }
}
