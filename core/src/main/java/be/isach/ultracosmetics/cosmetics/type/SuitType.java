package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.suits.Suit;
import be.isach.ultracosmetics.util.MathUtils;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.cryptomorin.xseries.XMaterial;

/**
 * Suit types.
 *
 * @author iSach
 * @since 12-20-2015
 */
public class SuitType extends CosmeticType<Suit> {

    private final ArmorSlot slot;
    private final SuitCategory category;

    /**
     * @param material The suit part material
     * @param slot     The slot this suit part should occupy
     * @param category The Suit category this part belongs to
     */
    protected SuitType(XMaterial material, ArmorSlot slot, SuitCategory category) {
        super(Category.fromSlot(slot), category.getConfigName(), material, category.getSuitClass(), false);
        this.slot = slot;
        this.category = category;
        // delay permission registration until we've loaded slot and category fields
        registerPermission();
    }

    @Override
    public String getName() {
        return MessageManager.getMessage("Suits." + getConfigName() + "." + slot.toString().toLowerCase() + "-name");
    }

    @Override
    protected String getPermissionSuffix() {
        return category.getPermissionSuffix() + "." + slot.toString().toLowerCase();
    }

    public ArmorSlot getSlot() {
        return slot;
    }

    public SuitCategory getSuitCategory() {
        return category;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack is = super.getItemStack();
        Color color = null;
        if (category == SuitCategory.RAVE) {
            int r = MathUtils.random(255);
            int g = MathUtils.random(255);
            int b = MathUtils.random(255);

            color = Color.fromRGB(r, g, b);
        } else if (category == SuitCategory.SANTA) {
            color = Color.RED;
        } else if (category == SuitCategory.FROZEN && slot != ArmorSlot.HELMET) {
            color = Color.AQUA;
        }
        if (color != null) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(color);
            is.setItemMeta(meta);
        }
        return is;
    }
}
