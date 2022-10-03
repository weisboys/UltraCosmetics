package be.isach.ultracosmetics.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.goal.CustomPathfinder;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

public class PetPathfinder extends CustomPathfinder {
    private final Player target;
    private final EntityBrain brain;
    private final double speed;
    private final boolean useEyeLocation;

    public PetPathfinder(Mob mob, Player target) {
        super(mob);
        this.target = target;
        this.brain = BukkitBrain.getBrain(mob);
        double speed = 1.15;
        boolean useEyeLocation = false;
        if (mob.getType() == EntityType.VEX) {
            speed = 0.75;
            useEyeLocation = true;
        }
        this.speed = speed;
        this.useEyeLocation = useEyeLocation;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT, PathfinderFlag.JUMPING };
    }

    @Override
    public void start() {

    }

    @Override
    public void tick() {
        Location loc = useEyeLocation ? target.getEyeLocation() : target.getLocation();
        if (entity.getWorld() != loc.getWorld() || entity.getLocation().distanceSquared(loc) > 10 * 10) {
            entity.teleport(loc);
            brain.getController().moveTo(loc, speed);
            return;
        }

        // Slime or magma cube
        // This doesn't work, look at modifying Slime move controller
        if (entity instanceof Slime) {
            Location deltaLoc = loc.subtract(entity.getLocation());
            brain.getBody().setYaw((float) Math.atan2(deltaLoc.getX(), deltaLoc.getZ()));
        }

        if (loc.distanceSquared(entity.getLocation()) > 3 * 3) {
            brain.getController().moveTo(loc, speed);
        }
    }

}
