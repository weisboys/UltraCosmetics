package be.isach.ultracosmetics.run;

import be.isach.ultracosmetics.player.UltraPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LoadUltraPlayerTask extends BukkitRunnable {
    private final UUID uuid;
    private final UltraPlayerManager ultraPlayerManager;

    public LoadUltraPlayerTask(UUID uuid, UltraPlayerManager ultraPlayerManager) {
        this.uuid = uuid;
        this.ultraPlayerManager = ultraPlayerManager;
    }

    @Override
    public void run() {
        if (Bukkit.getPlayer(uuid) == null || ultraPlayerManager.hasUltraPlayer(uuid)) {
            return;
        }
        ultraPlayerManager.createUltraPlayer(uuid);
    }
}
