package be.isach.ultracosmetics.cosmetics.deatheffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.DeathEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import org.bukkit.entity.Player;

public class DeathEffectLightning extends DeathEffect {

    private final boolean silent = SettingsManager.getConfig().getString(getOptionPath("Silent"));
    public DeathEffectLightning(UltraPlayer owner, DeathEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void displayParticles(Player player) {
        player.getWorld().spigot().strikeLightningEffect(player.getLocation(), silent);
    }
}
