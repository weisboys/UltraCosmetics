package be.isach.ultracosmetics.util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Sacha on 17/10/15.
 */
public class PlayerUtils {

    public static Vector getHorizontalDirection(Player player, double mult) {
        Vector vector = new Vector();
        double rotX = Math.toRadians(player.getLocation().getYaw());
        vector.setX(-Math.sin(rotX));
        vector.setZ(Math.cos(rotX));
        return vector.multiply(mult);
    }

}
