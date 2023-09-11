package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import me.libraryaddict.disguise.disguisetypes.watchers.CreeperWatcher;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author iSach
 * @since 08-26-2015
 */
public class MorphCreeper extends Morph implements PlayerAffectingCosmetic, Updatable {
    private int charge = 0;

    public MorphCreeper(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        if (!canUseSkill) return;
        CreeperWatcher creeperWatcher = (CreeperWatcher) disguise.getWatcher();
        if (getPlayer().isSneaking()) {
            creeperWatcher.setIgnited(true);
            if (charge + 4 <= 100) charge += 4;
            XSound.ENTITY_CREEPER_PRIMED.play(getPlayer(), 1.4f, 1.5f);
        } else {
            if (creeperWatcher.isIgnited()) {
                // Reset disguise
                onEquip();
            }
            if (charge == 100) {
                Particles.EXPLOSION_HUGE.display(getPlayer().getLocation());
                XSound.ENTITY_GENERIC_EXPLODE.play(getPlayer(), 1.4f, 1.5f);

                Player player = getPlayer();
                for (Entity ent : player.getNearbyEntities(3, 3, 3)) {
                    if (!canAffect(ent, player)) continue;
                    double dX = getPlayer().getLocation().getX() - ent.getLocation().getX();
                    double dY = getPlayer().getLocation().getY() - ent.getLocation().getY();
                    double dZ = getPlayer().getLocation().getZ() - ent.getLocation().getZ();
                    double yaw = Math.atan2(dZ, dX);
                    double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
                    double x = Math.sin(pitch) * Math.cos(yaw);
                    double y = Math.sin(pitch) * Math.sin(yaw);
                    double z = Math.cos(pitch);

                    Vector vector = new Vector(x, z, y);
                    MathUtils.applyVelocity(ent, vector.multiply(1.3D).add(new Vector(0, 1.4D, 0)));
                }
                ActionBar.clearActionBar(getPlayer());
                charge = 0;
                return;
            }
            if (charge > 0) charge -= 4;
        }
        if (charge > 0 && charge < 100) {
            if (charge < 5) {
                ActionBar.clearActionBar(getPlayer());
            } else {
                ActionBar.sendActionBar(getPlayer(), MessageManager.getLegacyMessage("Morphs.Creeper.charging",
                        Placeholder.unparsed("chargelevel", String.valueOf(charge)))
                );
            }
        } else if (charge == 100) {
            ActionBar.sendActionBar(getPlayer(), MessageManager.getLegacyMessage("Morphs.Creeper.release-to-explode"));
        }
    }
}
