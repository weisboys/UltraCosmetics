package be.isach.ultracosmetics.treasurechests;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.Color;

public class ChestParticleRunnable extends BukkitRunnable {
    private static final Vector Y_AXIS = new Vector(0, 1, 0);
    private final TreasureChest chest;
    private final UltraCosmetics uc;
    private final boolean large;
    private final int totalChests;
    private int i;
    private PlaceChestRunnable chestRunnable = null;

    public ChestParticleRunnable(TreasureChest chest) {
        this.chest = chest;
        this.uc = UltraCosmeticsData.get().getPlugin();
        large = chest.isLarge();
        if (!large && chest.getChestsLeft() > 4) {
            totalChests = 4;
        } else {
            totalChests = chest.getChestsLeft();
        }
        i = totalChests - 1;
    }

    @Override
    public void run() {
        if (i < 0) {
            cancel();
            return;
        }
        Player player = chest.getPlayer();
        if (player == null || uc.getPlayerManager().getUltraPlayer(player).getCurrentTreasureChest() != chest) {
            cancel();
            return;
        }
        int animationTime = 0;
        XParticle particleEffect = chest.getParticleEffect();
        Location chestLocation = getChestLocation();
        if (particleEffect != null) {
            playHelix(particleEffect, chestLocation, 0.0F);
            playHelix(particleEffect, chestLocation, 3.5F);
            animationTime = 30;
        }
        chestRunnable = new PlaceChestRunnable(chest, chestLocation.getBlock(), i--);
        chestRunnable.runTaskLater(uc, animationTime);
    }

    private Location getChestLocation() {
        Location chestLocation = chest.getCenter().clone().add(0.5, 0, 0.5);
        double horizontalOffset = 0;
        // If the chests need to be evenly distributed
        if (totalChests > 4 && totalChests < 9) {
            if (i < 4) {
                horizontalOffset = 1;
            } else {
                horizontalOffset = -1;
            }
        } else if (totalChests > 8) {
            if (i > 7) {
                horizontalOffset = 1;
            } else if (i > 3) {
                horizontalOffset = -1;
            }
        }

        BlockFace face = getDirection(i);
        Vector v = new Vector(face.getModX(), face.getModY(), face.getModZ());
        Vector perpendicular = v.clone().crossProduct(Y_AXIS).multiply(horizontalOffset);
        v.multiply(large ? 3 : 2).add(perpendicular);

        return chestLocation.add(v);
    }

    public static BlockFace getDirection(int direction) {
        return switch (direction % 4) {
            case 0 -> BlockFace.WEST;
            case 1 -> BlockFace.EAST;
            case 2 -> BlockFace.NORTH;
            default -> BlockFace.SOUTH;
        };
    }

    public void propogateCancel() {
        cancel();
        if (chestRunnable != null) {
            chestRunnable.cancel();
        }
    }

    private void playHelix(final XParticle particle, final Location loc, final float offset) {
        final ParticleDisplay display = ParticleDisplay.of(particle);
        if (particle == XParticle.DUST) {
            display.withColor(Color.RED);
        }
        BukkitRunnable runnable = new BukkitRunnable() {
            double radius = 0;
            double step = 0;
            final double y = loc.getY();
            final double inc = (2 * Math.PI) / 50;
            final Location location = loc.clone().add(0, 3, 0);

            @Override
            public void run() {
                double angle = step * inc + offset;
                Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle)).multiply(radius);
                display.spawn(location);
                location.subtract(v).subtract(0, 0.1d, 0);
                if (location.getY() <= y) {
                    cancel();
                }
                step += 4;
                radius += 1 / 50f;
            }
        };
        runnable.runTaskTimer(UltraCosmeticsData.get().getPlugin(), 0, 1);
    }
}
