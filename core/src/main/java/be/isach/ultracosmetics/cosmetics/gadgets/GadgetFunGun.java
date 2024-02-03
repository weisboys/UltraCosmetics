package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a fungun gadget summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class GadgetFunGun extends Gadget {

    private final List<Projectile> projectiles = new ArrayList<>();
    private final XSound.SoundPlayer sound;

    public GadgetFunGun(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        this.sound = XSound.ENTITY_CAT_PURREOW.record().withVolume(1.4f).withPitch(1.5f).soundPlayer().forPlayers(getPlayer());
    }

    @Override
    protected void onRightClick() {
        for (int i = 0; i < 5; i++) {
            projectiles.add(getPlayer().launchProjectile(Snowball.class));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!projectiles.contains(projectile)) return;

        Location location = projectile.getLocation();

        for (Projectile snowball : projectiles) {
            snowball.remove();
        }

        Particles.LAVA.display(1.3f, 1f, 1.3f, location, 16);
        Particles.HEART.display(0.8f, 0.8f, 0.8f, location, 20);
        sound.play();
    }
}
