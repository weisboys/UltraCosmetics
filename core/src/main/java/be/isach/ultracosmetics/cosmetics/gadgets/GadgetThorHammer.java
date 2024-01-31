package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.listeners.HammerPickupListener;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Represents an instance of a thor hammer gadget summoned by a player.
 *
 * @author iSach
 * @since 08-08-2015
 */
public class GadgetThorHammer extends Gadget implements PlayerAffectingCosmetic {
    // EntityPickupItemEvent didn't exist until 1.12
    private static final boolean USE_OTHER_LISTENER = UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_12);
    private Item hammer = null;
    private HammerPickupListener listener;
    private Vector v;

    public GadgetThorHammer(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    protected void onRightClick() {
        // I think this can only happen if a player is bypassing cooldowns
        if (hammer != null) {
            hammer.remove();
        }
        Vector velocity = getPlayer().getEyeLocation().getDirection().multiply(1.4);
        hammer = ItemFactory.spawnUnpickableItem(ItemFactory.create(XMaterial.IRON_AXE, getTypeName()), getPlayer().getEyeLocation(), velocity);
        getPlayer().getInventory().setItem(slot, null);
        v = getPlayer().getEyeLocation().getDirection().multiply(1.4).add(new Vector(0, 1, 0));
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            if (hammer == null) return;
            hammer.setVelocity(getPlayer().getEyeLocation().toVector().subtract(hammer.getLocation().toVector()).multiply(0.2).add(new Vector(0, 0, 0)));
            v = null;
            Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
                if (hammer == null) return;
                pickupItem();
            }, 40);
        }, 20);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (!USE_OTHER_LISTENER) return;
        listener = new HammerPickupListener(this);
        Bukkit.getPluginManager().registerEvents(listener, getUltraCosmetics());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onItemPickup(org.bukkit.event.player.PlayerPickupItemEvent event) {
        if (hammer != event.getItem()) return;
        event.setCancelled(true);

        Player player = getPlayer();
        if (event.getPlayer() != player) {
            if (v != null && canAffect(event.getPlayer(), player)) {
                MathUtils.applyVelocity(event.getPlayer(), v);
            }
            return;
        }

        if (hammer.getTicksLived() <= 5) return;

        pickupItem();
    }

    private void pickupItem() {
        equipItem();
        hammer.remove();
        hammer = null;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDamEnt(EntityDamageByEntityEvent event) {
        if (getOwner() != null
                && getPlayer() != null
                && event.getDamager() == getPlayer()
                && getPlayer().getItemInHand().equals(getItemStack())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onClear() {
        if (hammer != null) {
            hammer.remove();
            hammer = null;
        }
        v = null;
        if (!USE_OTHER_LISTENER) return;
        HandlerList.unregisterAll(listener);
        listener = null;
    }

    public Item getHammer() {
        return hammer;
    }
}
