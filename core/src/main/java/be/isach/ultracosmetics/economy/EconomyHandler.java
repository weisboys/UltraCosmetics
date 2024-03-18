package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.util.Discount;
import be.isach.ultracosmetics.util.SmartLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the current economy being used.
 *
 * @author RadBuilder
 * @since 2.5
 */
@SuppressWarnings("Convert2MethodRef")
public class EconomyHandler {
    private static final Map<String, EconomyHookLoader> economies = new HashMap<>();

    static {
        // Do NOT use method references, this requires loading the class, which will fail if the plugin is missing
        economies.put("treasury", (uc, currency) -> new TreasuryHook(uc, currency));
        economies.put("vault", (uc, currency) -> new VaultHook());
        economies.put("playerpoints", (uc, currency) -> new PlayerPointsHook());
        economies.put("peconomy", (uc, currency) -> new PEconomyHook(uc, currency));
        // We can't directly reference CoinsEngine when compiling with Java 8, so we have to use reflection.
        economies.put("coinsengine", (uc, currency) -> loadByReflection(uc, currency, "java17.CoinsEngineHook"));
    }

    private EconomyHook economyHook;
    private boolean usingEconomy = false;
    private final List<Discount> discounts = new ArrayList<>();

    public EconomyHandler(UltraCosmetics ultraCosmetics) {
        String economy = ultraCosmetics.getConfig().getString("Economy", "").toLowerCase();
        if (economy.isEmpty()) {
            ultraCosmetics.getSmartLogger().write("Economy not specified in the config, disabling economy features.");
            return;
        }
        String currency = ultraCosmetics.getConfig().getString("Economy-Currency", "");
        if (currency.isEmpty()) currency = null;

        ultraCosmetics.getSmartLogger().write("");
        EconomyHookLoader hookLoader = economies.get(economy);
        if (hookLoader == null) {
            ultraCosmetics.getSmartLogger().write(SmartLogger.LogLevel.ERROR, "Unknown economy: '" + economy + "'. Valid economies: " + String.join(", ", economies.keySet()));
            return;
        }
        try {
            economyHook = hookLoader.load(ultraCosmetics, currency);
            ultraCosmetics.getSmartLogger().write("Hooked into " + economyHook.getName() + " for economy.");
            usingEconomy = true;
        } catch (IllegalStateException | IllegalArgumentException | UnsupportedClassVersionError e) {
            ultraCosmetics.getSmartLogger().write(SmartLogger.LogLevel.ERROR, e.getMessage());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        if (!usingEconomy) {
            ultraCosmetics.getSmartLogger().write(SmartLogger.LogLevel.WARNING, "Economy features will be disabled.");
            return;
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

    private static EconomyHook loadByReflection(UltraCosmetics ultraCosmetics, String currency, String className) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName("be.isach.ultracosmetics.economy." + className);
        try {
            return (EconomyHook) clazz.getConstructor(UltraCosmetics.class, String.class).newInstance(ultraCosmetics, currency);
        } catch (InvocationTargetException e) {
            // Unwrap the exception if it's an IllegalArgumentException, those are expected
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw e;
        }
    }
}
