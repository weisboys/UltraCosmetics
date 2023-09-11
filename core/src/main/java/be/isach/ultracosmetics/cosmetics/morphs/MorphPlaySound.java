package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import com.cryptomorin.xseries.XSound;
import org.bukkit.event.player.PlayerInteractEvent;

public class MorphPlaySound extends MorphLeftClickCooldown {
    protected XSound sound;

    public MorphPlaySound(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics, XSound sound) {
        super(owner, type, ultraCosmetics, 0.5);
        this.sound = sound;
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        sound.play(event.getPlayer().getLocation());
    }
}
