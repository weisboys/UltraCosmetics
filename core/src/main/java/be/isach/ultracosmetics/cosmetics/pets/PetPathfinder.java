package be.isach.ultracosmetics.cosmetics.pets;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.goal.CustomPathfinder;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

/**
 * Not currently in use because adding custom pathfinders
 * with MobChip does not work correctly.
 */
public class PetPathfinder extends CustomPathfinder {
    private final Player target;
    private final EntityBrain brain;

    public PetPathfinder(Mob mob, Player target) {
        super(mob);
        this.target = target;
        this.brain = BukkitBrain.getBrain(mob);
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public PathfinderFlag[] getFlags() {
        return new PathfinderFlag[] { PathfinderFlag.MOVEMENT };
    }

    @Override
    public void start() {
        brain.getController().moveTo(target.getLocation().add(0, 0, 15));
    }

    @Override
    public void tick() {

    }

}
