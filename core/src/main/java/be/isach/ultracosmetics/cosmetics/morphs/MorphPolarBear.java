package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 * Represents an instance of a polar bear morph summoned by a player.
 *
 * @author RadBuilder
 * @since 07-03-2017
 */
public class MorphPolarBear extends MorphLeftClickCooldown implements Updatable {
    private long coolDown = 0;
    private boolean active;
    private Location location;
    private Vector vector;

    public MorphPolarBear(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics, 15);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        coolDown = System.currentTimeMillis() + 15000;
        vector = getPlayer().getLocation().getDirection().normalize().multiply(0.3);
        vector.setY(0);
        location = getPlayer().getLocation().subtract(0, 1, 0).add(vector);
        active = true;
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> active = false, 40);
    }

    @Override
    public void onUpdate() {
        if (!canUseSkill) return;
        if (!active) {
            location = null;
            vector = null;
            return;
        }
        if (location.getBlock().getType().isSolid()) {
            location.add(0, 1, 0);
        }

        if (BlockUtils.isAir(location.clone().subtract(0, 1, 0).getBlock().getType())) {
            if (!location.clone().getBlock().getType().toString().contains("SLAB")) location.add(0, -1, 0);
        }

        for (int i = 0; i < 3; i++) {
            UltraCosmeticsData.get().getVersionManager().getEntityUtil().sendBlizzard(getPlayer(), location, ent -> false, vector);
        }

        location.add(vector);
    }

    @Override
    protected void onClear() {
        if (!canUseSkill) return;
        active = false;
        if (getOwner() != null && getPlayer() != null) {
            UltraCosmeticsData.get().getVersionManager().getEntityUtil().clearBlizzard(getPlayer());
        }
    }
}
