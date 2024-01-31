package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;
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
    private static final ItemStack BOOTS;

    static {
        if (UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_9)) {
            BOOTS = XMaterial.LEATHER_BOOTS.parseItem();
            BOOTS.addEnchantment(Enchantment.FROST_WALKER, 2);
        } else {
            BOOTS = null;
        }
    }

    public MountGlacialSteed(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        super.setupEntity();
        if (BOOTS != null) {
            ((Horse) entity).getEquipment().setBoots(BOOTS, true);
        }
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
