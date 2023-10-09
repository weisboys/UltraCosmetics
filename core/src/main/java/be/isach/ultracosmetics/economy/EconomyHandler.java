package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.util.Discount;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the current economy being used.
 *
 * @author RadBuilder
 * @since 2.5
 */
public class EconomyHandler {
    private EconomyHook economyHook;
    private boolean usingEconomy = false;
    private final List<Discount> discounts = new ArrayList<>();

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

        ConfigurationSection section = SettingsManager.getConfig().getConfigurationSection("Discount-Groups");
        for (String key : section.getKeys(false)) {
            if (!section.isDouble(key)) continue;
            discounts.add(new Discount(key, section.getDouble(key)));
        }
        Collections.sort(discounts);
    }

    public EconomyHook getHook() {
        return economyHook;
    }

    public void withdrawWithDiscount(Player player, int amount, Runnable onSuccess, Runnable onFailure) {
        economyHook.withdraw(player, calculateDiscountPrice(player, amount), onSuccess, onFailure);
    }

    public int calculateDiscountPrice(Player player, int amount) {
        for (Discount discount : discounts) {
            if (discount.hasPermission(player)) {
                return (int) (amount * discount.getDiscount());
            }
        }
        return amount;
    }

    public boolean isUsingEconomy() {
        return usingEconomy;
    }
}
