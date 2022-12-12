package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import com.cryptomorin.xseries.XSound;

public abstract class MorphFlightAbility extends Morph implements Updatable {
    private static final double COOLDOWN = 2;

    public MorphFlightAbility(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    protected void onEquip() {
        super.onEquip();
        getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (event.getPlayer() == getPlayer()
                && event.getPlayer().getGameMode() != GameMode.CREATIVE
                && !event.getPlayer().isFlying()) {
            event.getPlayer().setFlying(false);
            event.setCancelled(true);
            if (!getOwner().canUse(getType())) return;
            Vector v = event.getPlayer().getLocation().getDirection();
            v.setY(0.75);
            MathUtils.applyVelocity(getPlayer(), v);
            getSound().play(getPlayer(), 0.4f, 1.0f);
            getOwner().setCoolDown(getType(), COOLDOWN, COOLDOWN);
        }
    }

    @Override
    public void onUpdate() {
        if (UltraCosmeticsData.get().displaysCooldownInBar() && !getOwner().canUse(getType())) {
            getOwner().sendCooldownBar(getType(), COOLDOWN, COOLDOWN);
        }
    }

    @Override
    public void onClear() {
        if (getPlayer().getGameMode() != GameMode.CREATIVE) {
            getPlayer().setAllowFlight(false);
        }
    }

    public abstract XSound getSound();
}
