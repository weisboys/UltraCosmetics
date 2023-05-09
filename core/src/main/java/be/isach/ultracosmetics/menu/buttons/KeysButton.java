package be.isach.ultracosmetics.menu.buttons;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.menu.ClickData;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.inventory.ItemStack;

public class KeysButton extends TreasureButton {
    private final String itemName = MessageManager.getMessage("Treasure-Keys");
    private final String yourKeysMessage = MessageManager.getMessage("Your-Keys");

    public KeysButton(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics);
    }

    @Override
    public ItemStack getDisplayItem(UltraPlayer ultraPlayer) {
        return ItemFactory.create(XMaterial.TRIPWIRE_HOOK, itemName, "",
                yourKeysMessage.replace("%keys%", String.valueOf(ultraPlayer.getKeys())), buyKeyMessage);
    }

    @Override
    public void onClick(ClickData clickData) {
        UltraPlayer clicker = clickData.getClicker();
        if (!canBuyKeys) {
            XSound.BLOCK_ANVIL_LAND.play(clicker.getBukkitPlayer().getLocation(), 0.2f, 1.2f);
            return;
        }
        clicker.getBukkitPlayer().closeInventory();
        clicker.openKeyPurchaseMenu();
    }
}
