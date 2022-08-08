package be.isach.ultracosmetics.listeners;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;

/**
 * Listens for any events that only exist in 1.13 and up
 */
public class Listener113 implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (event.getEntity().hasMetadata("Pet")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrowned(EntityTransformEvent event) {
        if (event.getTransformReason() == TransformReason.DROWNED && event.getEntity().hasMetadata("Pet")) {
            event.setCancelled(true);
            ((Zombie) event.getEntity()).setConversionTime(Integer.MAX_VALUE);
        }
    }
}
