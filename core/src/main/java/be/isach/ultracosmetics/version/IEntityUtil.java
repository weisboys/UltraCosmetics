package be.isach.ultracosmetics.version;

import be.isach.ultracosmetics.treasurechests.TreasureChestDesign;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.util.Vector;

import java.util.function.Function;

/**
 * Created by Sacha on 14/03/16.
 */
public interface IEntityUtil {

    void resetWitherSize(Wither wither);

    void sendBlizzard(final Player player, Location loc, Function<Entity,Boolean> canAffectFunc, Vector v);

    void clearBlizzard(final Player player);

    void sendDestroyPacket(Player player, Entity entity);

    void move(Creature creature, Location location);

    void moveDragon(Player player, Entity entity);

    void setStepHeight(Entity entity);

    void moveShip(Player player, Entity entity, Vector vector);

    void playChestAnimation(Block b, boolean open, TreasureChestDesign design);

    void sendTeleportPacket(Player player, Entity entity);
}
