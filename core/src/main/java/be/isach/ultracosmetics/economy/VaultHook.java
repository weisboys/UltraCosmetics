package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmetics;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

/**
 * Vault economy hook.
 *
 * @author RadBuilder
 * @since 2.5
 */
public class VaultHook implements EconomyHook {
    private Economy economy = null;
    private boolean economyEnabled;

    public VaultHook(UltraCosmetics ultraCosmetics) {
        if (!ultraCosmetics.getServer().getPluginManager().isPluginEnabled("Vault")) {
            economyEnabled = false;
            return;
        }
        RegisteredServiceProvider<Economy> economyProvider = ultraCosmetics.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            ultraCosmetics.getSmartLogger().write("");
            ultraCosmetics.getSmartLogger().write("Hooked into Vault for economy: " + economy.getName());
            ultraCosmetics.getSmartLogger().write("");
            economyEnabled = true;
        } else {
            ultraCosmetics.getSmartLogger().write("");
            ultraCosmetics.getSmartLogger().write("Found Vault but no economy, please check whether your economy plugin supports Vault.");
            ultraCosmetics.getSmartLogger().write("");
            economyEnabled = false;
        }
    }

    @Override
    public void withdraw(Player player, int amount) {
        economy.withdrawPlayer(player, amount);
    }

    @Override
    public void deposit(Player player, int amount) {
        economy.depositPlayer(player, amount);
    }

    @Override
    public double balance(Player player) {
        return economy.getBalance(player);
    }

    @Override
    public String getName() {
        return "Vault:" + economy.getName();
    }

    @Override
    public boolean economyEnabled() {
        return economyEnabled;
    }
}
