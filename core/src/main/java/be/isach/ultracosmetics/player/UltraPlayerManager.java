package be.isach.ultracosmetics.player;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.listeners.ClientBrandListener;
import be.isach.ultracosmetics.util.SmartLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for UltraPlayers.
 *
 * @author iSach
 * @since 12-16-2015
 */
public class UltraPlayerManager {

    private final Map<UUID, UltraPlayer> playerCache;
    private final UltraCosmetics ultraCosmetics;

    public UltraPlayerManager(UltraCosmetics ultraCosmetics) {
        this.playerCache = new ConcurrentHashMap<>();
        this.ultraCosmetics = ultraCosmetics;
    }

    public void createUltraPlayer(UUID uuid) {
        // This should overwrite any existing UltraPlayer
        UltraPlayer old = playerCache.put(uuid, new UltraPlayer(uuid, ultraCosmetics));
        if (old != null) {
            ultraCosmetics.getSmartLogger().write(SmartLogger.LogLevel.WARNING, "Removed stale UltraPlayer, potential memory leak?");
        }
    }

    public UltraPlayer getUltraPlayer(Player player) {
        if (player == null) return null;

        return playerCache.computeIfAbsent(player.getUniqueId(), u -> new UltraPlayer(u, ultraCosmetics));
    }

    public boolean remove(Player player) {
        return playerCache.remove(player.getUniqueId()) != null;
    }

    public Collection<UltraPlayer> getUltraPlayers() {
        return playerCache.values();
    }

    /**
     * Initialize players.
     */
    public void initPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UltraPlayer up = getUltraPlayer(p);
            if (SettingsManager.getConfig().getBoolean("Menu-Item.Enabled") && SettingsManager.isAllowedWorld(p.getWorld())) {
                up.giveMenuItem();
            }
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(ultraCosmetics, "minecraft:brand", new ClientBrandListener(ultraCosmetics));
    }

    public void dispose() {
        Collection<UltraPlayer> set = playerCache.values();
        for (UltraPlayer cp : set) {
            cp.dispose();
        }

        playerCache.clear();
    }
}
