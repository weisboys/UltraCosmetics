package be.isach.ultracosmetics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * 1.9 offhand listeners.
 *
 * @author iSach
 * @since 05-16-2016
 */
public class OffhandListener implements Listener {

    private final UnmovableItemListener unmovableItemListener;

    public OffhandListener(UnmovableItemListener unmovableItemListener) {
        this.unmovableItemListener = unmovableItemListener;
    }

    @EventHandler
    public void onPlayerSwapoffHand(PlayerSwapHandItemsEvent event) {
        unmovableItemListener.forEachProvider(event.getPlayer(), p -> {
            if (p.itemMatches(event.getMainHandItem()) || p.itemMatches(event.getOffHandItem())) {
                event.setCancelled(true);
            }
        });
    }
}
