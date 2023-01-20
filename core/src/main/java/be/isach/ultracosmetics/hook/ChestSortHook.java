package be.isach.ultracosmetics.hook;

import be.isach.ultracosmetics.util.ItemFactory;
import de.jeff_media.chestsort.api.ChestSortAPI;
import de.jeff_media.chestsort.api.ChestSortEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestSortHook implements Listener {
    private final ItemStack menuItem = ItemFactory.createMenuItem();

    public void setUnsortable(Inventory inventory) {
        ChestSortAPI.setUnsortable(inventory);
    }

    @EventHandler
    public void onChestSort(ChestSortEvent event) {
        event.setUnmovable(menuItem);
    }
}
