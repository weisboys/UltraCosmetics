package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.IronGolem;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

/**
 * Represents an instance of an iron golem pet summoned by a player.
 *
 * @author RadBuilder
 * @since 07-02-2017
 */
public class PetIronGolem extends Pet {
    public PetIronGolem(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics, XMaterial.RED_DYE);
    }

    @Override
    public boolean customize(String customization) {
        ItemStack stack = this.parseCustomItem(customization);
        if (stack == null) return false;
        ((IronGolem) entity).getEquipment().setItemInMainHand(stack);
        return true;
    }
}
