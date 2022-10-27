package be.isach.ultracosmetics.economy;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.util.SmartLogger;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator.Type;

public class TreasuryHook implements EconomyHook {
    private final EconomyProvider economy;
    private final SmartLogger log = UltraCosmeticsData.get().getPlugin().getSmartLogger();
    private final EconomyTransactionInitiator<String> initiator = EconomyTransactionInitiator.createInitiator(Type.PLUGIN, "UltraCosmetics");

    public TreasuryHook() {
        Optional<Service<EconomyProvider>> optProvider = ServiceRegistry.INSTANCE.serviceFor(EconomyProvider.class);
        if (!optProvider.isPresent()) {
            throw new IllegalStateException("Could not find provider for Treasury economy.");
        }
        economy = optProvider.get().get();
    }

    @Override
    public void withdraw(Player player, int intAmount, Runnable onSuccess, Runnable onFailure) {
        BigDecimal amount = new BigDecimal(intAmount);
        getAccount(player.getUniqueId(), account -> {
            account.canAfford(amount, economy.getPrimaryCurrency(), new EconomySubscriber<>() {

                @Override
                public void succeed(@NotNull Boolean t) {
                    if (!t) {
                        onFailure.run();
                        return;
                    }
                    account.withdrawBalance(amount, initiator, economy.getPrimaryCurrency(), new EconomySubscriber<>() {
                        @Override
                        public void succeed(BigDecimal newBalance) {
                            onSuccess.run();
                        }

                        @Override
                        public void fail(EconomyException exception) {
                            onFailure.run();
                            log.write(LogLevel.WARNING, "Failed to take player money: " + exception.getMessage());
                        }
                    });
                }

                @Override
                public void fail(@NotNull EconomyException exception) {
                    onFailure.run();
                    log.write(LogLevel.WARNING + "Failed to check if player can afford cost: " + exception.getMessage());
                }

            });
        });

    }

    @Override
    public void deposit(Player player, int amount) {

        getAccount(player.getUniqueId(), account -> {
            account.depositBalance(new BigDecimal(amount), initiator, economy.getPrimaryCurrency(), new EconomySubscriber<>() {
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
                EconomySubscriber<PlayerAccount> accountSubscriber = new EconomySubscriber<>() {

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
        return "Treasury";
    }

}
