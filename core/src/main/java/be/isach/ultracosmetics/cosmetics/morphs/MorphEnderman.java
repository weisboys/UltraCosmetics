package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.BlockUtils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Represents an instance of an enderman morph summoned by a player.
 *
 * @author iSach
 * @since 08-26-2015
 */
public class MorphEnderman extends MorphNoFall {
    private static double COOLDOWN = 3.5;
    private final String mode = SettingsManager.getConfig().getString(getOptionPath("Mode"), "Ray trace");

    public MorphEnderman(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    protected void onEquip() {
        super.onEquip();
        getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerToggleFligh(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player != getPlayer() || player.getGameMode() == GameMode.CREATIVE || event.getPlayer().isFlying()) {
            return;
        }
        player.setFlying(false);
        event.setCancelled(true);
        if (!getOwner().canUse(cosmeticType)) {
            return;
        }
        if (mode.equalsIgnoreCase("Fast")) {
            if (!fast(player)) return;
        } else if (mode.equalsIgnoreCase("Enderpearl")) {
            player.launchProjectile(EnderPearl.class, player.getLocation().getDirection());
        } else { // Ray trace
            rayTrace(player);
        }
        getOwner().setCoolDown(cosmeticType, COOLDOWN, 0);
    }

    private void rayTrace(Player player) {
        Block b = player.getTargetBlock(null, 17);
        Location loc = b.getLocation();
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());
        player.teleport(loc);
        spawnRandomFirework(loc.add(0.5, 0, 0.5));
    }

    private boolean fast(Player player) {
        Vector v = player.getLocation().getDirection();
        v.setY(0);
        v.normalize().multiply(16);
        Location loc = player.getLocation().add(v);
        if (!BlockUtils.isAir(loc.getBlock().getType())) {
            player.teleport(loc);
            spawnRandomFirework(loc.add(0.5, 0, 0.5));
            return true;
        }
        return false;
    }

    public static FireworkEffect getRandomFireworkEffect() {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        return builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.fromRGB(0, 0, 0)).withFade(Color.fromRGB(0, 0, 0)).build();
    }

    public void spawnRandomFirework(Location location) {
        final ArrayList<Firework> fireworks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final Firework f = getPlayer().getWorld().spawn(location, Firework.class);

            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(getRandomFireworkEffect());
            f.setFireworkMeta(fm);
            fireworks.add(f);
        }
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            for (Firework f : fireworks)
                f.detonate();
        }, 2);
    }

    @Override
    public void onClear() {
        if (getPlayer().getGameMode() != GameMode.CREATIVE) {
            getPlayer().setAllowFlight(false);
        }
    }
}
