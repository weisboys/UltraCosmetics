package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an instance of a glacial steed mount.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class MountGlacialSteed extends MountAbstractHorse {

    public MountGlacialSteed(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, type);
    }

    @Override
    public void setupEntity() {
        super.setupEntity();
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.FROST_WALKER, 2);
        ((Horse) entity).getEquipment().setBoots(boots, true);
    }

    @Override
    public void onUpdate() {
        Particles.SNOW_SHOVEL.display(0.4f, 0.2f, 0.4f, entity.getLocation().clone().add(0, 1, 0), 5);
    }

    @Override
    protected Horse.Color getColor() {
        return Horse.Color.WHITE;
    }
}
