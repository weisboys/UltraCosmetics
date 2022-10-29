package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmetics;

/**
 * Handles the current economy being used.
 *
 * @author RadBuilder
 * @since 2.5
 */
public class EconomyHandler {
    private EconomyHook economyHook;
    private boolean usingEconomy = false;

    public EconomyHandler(UltraCosmetics ultraCosmetics, String economy) {
        if (economy == null || economy.equalsIgnoreCase("")) {
            ultraCosmetics.getSmartLogger().write("Economy not specified in the config, disabling economy features.");
            return;
        }

        ultraCosmetics.getSmartLogger().write("");
        try {
            if (economy.equalsIgnoreCase("treasury")) {
                economyHook = new TreasuryHook();
            } else if (economy.equalsIgnoreCase("vault")) {
                economyHook = new VaultHook();
            } else if (economy.equalsIgnoreCase("playerpoints")) {
                economyHook = new PlayerPointsHook();
            } else {
                ultraCosmetics.getSmartLogger().write("Unknown economy: '" + economy + "'. Valid economies: Vault, PlayerPoints.");
                return;
            }
            ultraCosmetics.getSmartLogger().write("Hooked into " + economyHook.getName() + " for economy.");
            usingEconomy = true;
        } catch (IllegalStateException e) {
            ultraCosmetics.getSmartLogger().write(e.getMessage());
        }
        ultraCosmetics.getSmartLogger().write("");
    }

    public EconomyHook getHook() {
        return economyHook;
    }

    public String getName() {
        return economyHook.getName();
    }

    public boolean isUsingEconomy() {
        return usingEconomy;
    }
}
