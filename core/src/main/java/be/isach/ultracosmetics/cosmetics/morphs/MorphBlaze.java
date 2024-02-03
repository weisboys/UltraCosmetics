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
    private final XSound.SoundPlayer sound;

    public MorphBlaze(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        sound = XSound.BLOCK_FIRE_EXTINGUISH.record().withVolume(0.1f).withPitch(1.5f).soundPlayer().forPlayers(getPlayer());
    }

    @Override
    public void onUpdate() {
        if (canUseSkill && getPlayer().isSneaking()) {
            Particles.FLAME.display(getPlayer().getLocation());
            Particles.LAVA.display(getPlayer().getLocation());
            sound.play();
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
