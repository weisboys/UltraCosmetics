package be.isach.ultracosmetics.cosmetics.deatheffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.DeathEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class DeathEffectFlames extends DeathEffect {

    private final boolean silent = SettingsManager.getConfig().getBoolean(getOptionPath("Silent"));

    public DeathEffectFlames(UltraPlayer owner, DeathEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void displayParticles() {
        Player player = getPlayer();
        for (int i = 0; i < 10; i++) {
            double offsetX = (Math.random() - 0.5) * 2;
            double offsetY = Math.random() * 1.5;
            double offsetZ = (Math.random() - 0.5) * 2;
            player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
            player.getWorld().playEffect(player.getLocation().clone().add(offsetX, offsetY, offsetZ), Effect.ELECTRIC_SPARK, 0);
        }
    }
}
