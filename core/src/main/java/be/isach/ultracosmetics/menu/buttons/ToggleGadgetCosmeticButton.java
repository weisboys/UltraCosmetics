package be.isach.ultracosmetics.menu.buttons;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.menu.ClickData;
import be.isach.ultracosmetics.player.UltraPlayer;

public class ToggleGadgetCosmeticButton extends ToggleCosmeticButton {
    public ToggleGadgetCosmeticButton(UltraCosmetics ultraCosmetics, GadgetType cosmeticType) {
        super(ultraCosmetics, cosmeticType);
    }

    @Override
    protected void handleRightClick(ClickData clickData) {
        UltraPlayer ultraPlayer = clickData.getClicker();
        if (ultraCosmetics.getEconomyHandler().isUsingEconomy() && UltraCosmeticsData.get().isAmmoPurchaseEnabled() && ((GadgetType) cosmeticType).requiresAmmo()) {
            ultraCosmetics.getMenus().openAmmoPurchaseMenu((GadgetType) cosmeticType, ultraPlayer, () -> clickData.getMenu().refresh(ultraPlayer));
        }
    }

    @Override
    protected boolean handleActivate(ClickData clickData) {
        UltraPlayer ultraPlayer = clickData.getClicker();
        GadgetType gadgetType = ultraPlayer.getCurrentGadget().getType();
        if (ultraCosmetics.getEconomyHandler().isUsingEconomy() && UltraCosmeticsData.get().isAmmoPurchaseEnabled()
                && gadgetType.requiresAmmo() && ultraPlayer.getAmmo(gadgetType) < 1) {
            ultraCosmetics.getMenus().openAmmoPurchaseMenu(gadgetType, ultraPlayer, () -> clickData.getMenu().refresh(ultraPlayer));
            return false;
        }
        return super.handleActivate(clickData);
    }
}
