package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Slime;

import me.gamercoder215.mobchip.EntityBrain;

/**
 * Represents an instance of a slime pet summoned by a player.
 *
 * @author datatags
 * @since 18-01-2022
 */

public class PetSlime extends Pet {
    private int updateCounter = 0;

    public PetSlime(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        ((Slime) entity).setSize(1);
    }

    @Override
    public boolean customize(String customization) {
        int size;
        try {
            size = Integer.parseInt(customization);
        } catch (NumberFormatException e) {
            return false;
        }
        ((Slime) entity).setSize(size);
        return true;
    }

    @Override
    protected void move(EntityBrain brain, Location loc, double speed) {
        Location diff = loc.subtract(entity.getLocation());
        float yaw = (float) Math.toDegrees(Math.atan2(diff.getZ(), diff.getX())) - 90;
        brain.getBody().setYaw(yaw);
        brain.getBody().setHeadRotation(yaw);
        brain.getBody().setBodyRotation(yaw);
        if (++updateCounter > 39) {
            brain.getController().jump();
            brain.getController().moveTo(loc);
            updateCounter = 0;
            Bukkit.getLogger().info("Causing jump");
        }
    }
}
