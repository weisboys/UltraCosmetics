package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ServerVersion;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * Represents an instance of a wither pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetWither extends Pet {

    public PetWither(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        // Do not call super.onUpdate(), wither does not drop items.
        // No bossbar API on 1.8.8
        if (UltraCosmeticsData.get().getServerVersion() != ServerVersion.v1_8) {
            // This runs onUpdate because if players walk in range, the bossbar reappears
            // Must call .getBossBar() every time rather than using a variable because
            // creating a bossbar variable makes 1.8 servers unhappy
            String setting = SettingsManager.getConfig().getString("Pets.Wither.Bossbar", "in range");
            if (setting.equalsIgnoreCase("owner")) {
                ((Wither) entity).getBossBar().getPlayers().stream().filter(p -> p != getPlayer()).forEach(p -> ((Wither) entity).getBossBar().removePlayer(p));
            } else if (setting.equalsIgnoreCase("none")) {
                ((Wither) entity).getBossBar().getPlayers().forEach(p -> ((Wither) entity).getBossBar().removePlayer(p));
            }
        }

        if (!SettingsManager.getConfig().getBoolean("Pets-Are-Babies")) return;
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().resetWitherSize((Wither) getEntity());
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == entity) {
            event.setCancelled(true);
        }
    }
}
