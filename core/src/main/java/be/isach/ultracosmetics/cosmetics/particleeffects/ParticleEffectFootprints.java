package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleEffectFootprints extends ParticleEffect {

    public ParticleEffectFootprints(UltraPlayer owner, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);

        this.displayIfStanding = false;
    }

    @Override
    public void onUpdate() {
        Vector vectorLeft = getLeftVector(getPlayer().getLocation()).normalize().multiply(0.15);
        Vector vectorRight = getRightVector(getPlayer().getLocation()).normalize().multiply(0.15);
        Location locationLeft = getPlayer().getLocation().add(vectorLeft);
        Location locationRight = getPlayer().getLocation().add(vectorRight);
        locationLeft.setY(getPlayer().getLocation().getY());
        locationRight.setY(getPlayer().getLocation().getY());

        getType().getEffect().display(locationLeft.add(0, 0.1, 0));
        getType().getEffect().display(locationRight.add(0, 0.1, 0));
    }

    public static Vector getLeftVector(Location loc) {
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 0))));
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 0))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    public static Vector getRightVector(Location loc) {
        final float newX = (float) (loc.getX() + (-1 * Math.cos(Math.toRadians(loc.getYaw() + 0))));
        final float newZ = (float) (loc.getZ() + (-1 * Math.sin(Math.toRadians(loc.getYaw() + 0))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }
}
