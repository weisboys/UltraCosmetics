package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.util.SmartLogger;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TreasuryHook implements EconomyHook {
    private final UltraCosmetics ultraCosmetics;
    private final EconomyProvider economy;
    private final SmartLogger log;
    private final EconomyTransactionInitiator<String> initiator = EconomyTransactionInitiator.createInitiator(Type.PLUGIN, "UltraCosmetics");
    private final Currency currency;

    public TreasuryHook(UltraCosmetics ultraCosmetics, String currencyName) {
        this.ultraCosmetics = ultraCosmetics;
        this.log = ultraCosmetics.getSmartLogger();
        Optional<Service<EconomyProvider>> optProvider = ServiceRegistry.INSTANCE.serviceFor(EconomyProvider.class);
        if (!optProvider.isPresent()) {
            throw new IllegalStateException("Could not find provider for Treasury economy.");
        }
        economy = optProvider.get().get();
        if (currencyName == null) {
            currency = economy.getPrimaryCurrency();
        } else {
            Optional<Currency> optCurrency = economy.findCurrency(currencyName);
            if (!optCurrency.isPresent()) {
                throw new IllegalArgumentException("Couldn't find specified Treasury currency '" + currencyName + "'");
            }
            currency = optCurrency.get();
        }
    }

    private void mainThread(Runnable runnable) {
        Bukkit.getScheduler().runTask(ultraCosmetics, runnable);
    }

    @Override
    public void withdraw(Player player, int intAmount, Runnable onSuccess, Runnable onFailure) {
        BigDecimal amount = new BigDecimal(intAmount);
        getAccount(player.getUniqueId(), account -> {
            account.canAfford(amount, currency, new EconomySubscriber<Boolean>() {

                @Override
                public void succeed(@NotNull Boolean t) {
                    if (!t) {
                        mainThread(onFailure);
                        return;
                    }
                    account.withdrawBalance(amount, initiator, currency, new EconomySubscriber<BigDecimal>() {
                        @Override
                        public void succeed(BigDecimal newBalance) {
                            mainThread(onSuccess);
                        }

                        @Override
                        public void fail(EconomyException exception) {
                            mainThread(onFailure);
                            log.write(LogLevel.WARNING, "Failed to take player money: " + exception.getMessage());
                        }
                    });
                }

                @Override
                public void fail(@NotNull EconomyException exception) {
                    mainThread(onFailure);
                    log.write(LogLevel.WARNING + "Failed to check if player can afford cost: " + exception.getMessage());
                }

            });
        });

    }

    @Override
    public void deposit(Player player, int amount) {

        getAccount(player.getUniqueId(), account -> {
            account.depositBalance(new BigDecimal(amount), initiator, currency, new EconomySubscriber<BigDecimal>() {
                @Override
                public void succeed(BigDecimal newBalance) {
                }

                @Override
                public void fail(EconomyException exception) {
                    log.write(LogLevel.WARNING, "Failed to deposit money for player: " + exception.getMessage());
                }
            });
        });
    }

    private void getAccount(UUID uuid, Consumer<PlayerAccount> onSuccess) {
        economy.hasPlayerAccount(uuid, new EconomySubscriber<Boolean>() {

            @Override
            public void succeed(Boolean t) {
                @SuppressWarnings("unused")
                EconomySubscriber<PlayerAccount> accountSubscriber = new EconomySubscriber<PlayerAccount>() {

                    @Override
                    public void succeed(@NotNull PlayerAccount t) {
                        onSuccess.accept(t);
                    }

                    @Override
                    public void fail(@NotNull EconomyException exception) {
                        log.write(LogLevel.WARNING, "Failed to get player account: " + exception.getMessage());
                    }

                };
                if (t) {
                    economy.retrievePlayerAccount(uuid, accountSubscriber);
                } else {
                    economy.createPlayerAccount(uuid, accountSubscriber);
                }
            }

            @Override
            public void fail(EconomyException exception) {
                log.write(LogLevel.WARNING, "Failed to check if player has account: " + exception.getMessage());
            }

        });
    }

    @Override
    public String getName() {
        return "Treasury:" + currency.getIdentifier();
    }

}
