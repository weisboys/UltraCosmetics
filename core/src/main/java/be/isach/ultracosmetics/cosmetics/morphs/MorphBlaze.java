package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.XSound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;

/**
 * Represents an instance of a blaze morph summoned by a player.
 *
 * @author iSach
 * @since 08-26-2015
 */
public class MorphBlaze extends MorphNoFall implements Updatable {

    public MorphBlaze(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        if (canUseSkill && getPlayer().isSneaking()) {
            Particles.FLAME.display(getPlayer().getLocation());
            Particles.LAVA.display(getPlayer().getLocation());
            XSound.BLOCK_FIRE_EXTINGUISH.play(getPlayer(), 0.1f, 1.5f);
            getPlayer().setVelocity(getPlayer().getEyeLocation().getDirection().multiply(1));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (canUseSkill && event.getPlayer() == getPlayer() && getOwner().getCurrentMorph() == this && event.getReason().contains("Flying")) {
            event.setCancelled(true);
        }
    }
}
