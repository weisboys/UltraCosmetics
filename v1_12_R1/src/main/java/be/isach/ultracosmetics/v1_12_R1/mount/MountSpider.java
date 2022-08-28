package be.isach.ultracosmetics.v1_12_R1.mount;

import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.v1_12_R1.customentities.RideableSpider;
import net.minecraft.server.v1_12_R1.EntityLiving;

/**
 * @author RadBuilder
 */
public class MountSpider extends MountCustomEntity {
    public MountSpider(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public EntityLiving getNewEntity() {
        return new RideableSpider(((CraftWorld) getPlayer().getWorld()).getHandle());
    }
}
