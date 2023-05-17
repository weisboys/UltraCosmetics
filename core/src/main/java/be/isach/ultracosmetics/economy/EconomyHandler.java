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

    public EconomyHandler(UltraCosmetics ultraCosmetics) {
        String economy = ultraCosmetics.getConfig().getString("Economy", "");
        if (economy.isEmpty()) {
            ultraCosmetics.getSmartLogger().write("Economy not specified in the config, disabling economy features.");
            return;
        }
        String currency = ultraCosmetics.getConfig().getString("Economy-Currency", "");
        if (currency.isEmpty()) currency = null;

        ultraCosmetics.getSmartLogger().write("");
        try {
            if (economy.equalsIgnoreCase("treasury")) {
                economyHook = new TreasuryHook(ultraCosmetics, currency);
            } else if (economy.equalsIgnoreCase("vault")) {
                economyHook = new VaultHook();
            } else if (economy.equalsIgnoreCase("playerpoints")) {
                economyHook = new PlayerPointsHook();
            } else if (economy.equalsIgnoreCase("peconomy")) {
                economyHook = new PEconomyHook(ultraCosmetics, currency);
            } else {
                ultraCosmetics.getSmartLogger().write("Unknown economy: '" + economy + "'. Valid economies: Vault, PlayerPoints.");
                return;
            }
            ultraCosmetics.getSmartLogger().write("Hooked into " + economyHook.getName() + " for economy.");
            usingEconomy = true;
        } catch (IllegalStateException | IllegalArgumentException e) {
            ultraCosmetics.getSmartLogger().write(e.getMessage());
        }
        ultraCosmetics.getSmartLogger().write("");
    }

    public EconomyHook getHook() {
        return economyHook;
    }

    public boolean isUsingEconomy() {
        return usingEconomy;
    }
}
