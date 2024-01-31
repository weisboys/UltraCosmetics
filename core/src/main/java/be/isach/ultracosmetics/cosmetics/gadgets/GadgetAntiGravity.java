package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Represents an instance of an antigravity gadget summoned by a player.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class GadgetAntiGravity extends Gadget implements PlayerAffectingCosmetic, Updatable {

    private static final boolean USE_LEVITATION = UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_9);
    private static final ItemStack SEA_LANTERN = XMaterial.SEA_LANTERN.parseItem();
    private ArmorStand as;
    private boolean running;

    public GadgetAntiGravity(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onRightClick() {
        as = getPlayer().getWorld().spawn(getPlayer().getLocation(), ArmorStand.class);
        as.setMetadata("NO_INTER", new FixedMetadataValue(getUltraCosmetics(), ""));
        as.setGravity(false);
        as.setSmall(true);
        running = true;
        as.setVisible(false);
        as.setHelmet(SEA_LANTERN);
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> running = false, 240);
    }

    @Override
    public void onUpdate() {
        if (as == null || !as.isValid()) return;
        if (!running) {
            as.remove();
            as = null;
            return;
        }

        as.setHeadPose(as.getHeadPose().add(0, 0.1, 0));
        Particles.PORTAL.display(3f, 3f, 3f, as.getLocation(), 150);
        Particles.SPELL_WITCH.display(.3f, .3f, .3f, as.getEyeLocation(), 5);
        for (Entity ent : as.getNearbyEntities(3, 2, 3)) {
            if (canAffect(ent, getPlayer())) {
                LivingEntity le = (LivingEntity) ent;
                if (USE_LEVITATION) {
                    le.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5, 0));
                } else {
                    MathUtils.applyVelocity(ent, new Vector(0, 0.05, 0));
                }
            }
        }
    }

    // Find a fkn alternative to this shit :^)
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (!running || !USE_LEVITATION) return;
        if (as != null && as.isValid() && event.getReason().equals("Flying is not enabled on this server")) {
            if (as.getLocation().distanceSquared(event.getPlayer().getLocation()) < 6) {
                event.setCancelled(true);
                getUltraCosmetics().getSmartLogger().write("UltraCosmetics >> Cancelling flight kick while using antigravity.");
                return;
            }
        }
    }

    @Override
    public void onClear() {
        if (as != null) {
            as.remove();
        }
        running = false;
    }
}
