package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleEffectRainbowWings extends ParticleEffect {

    boolean x = true;
    boolean o = false;

    public ParticleEffectRainbowWings(UltraPlayer owner, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    private boolean[][] shape = {
            {o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
            {o, x, x, x, x, o, o, o, o, o, o, o, x, x, x, x, o, o},
            {o, o, x, x, x, x, x, o, o, o, x, x, x, x, x, o, o, o},
            {o, o, o, x, x, x, x, x, x, x, x, x, x, x, o, o, o, o},
            {o, o, o, o, x, x, x, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, x, o, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, o, x, x, x, o, o, o, o, o, o},
            {o, o, o, o, o, x, x, o, o, o, x, x, o, o, o, o, o, o},
            {o, o, o, o, x, x, o, o, o, o, o, x, x, o, o, o, o, o}
    };

    @Override
    public void onUpdate() {
        drawParticles(getPlayer().getLocation());
    }

    private void drawParticles(Location location) {
        double space = 0.2;
        double defX = location.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = location.clone().getY() + 2;
        double angle = -((location.getYaw() + 180) / 60);
        Particles.OrdinaryColor nextColor = getColor();
        angle += (location.getYaw() < -180 ? 3.25 : 2.985);

        for (boolean[] aShape : shape) {
            for (boolean anAShape : aShape) {
                if (anAShape) {

                    Location target = location.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(location.toVector());
                    Vector v2 = getBackVector(location);
                    v = rotateAroundAxisY(v, angle);
                    v2.setY(0).multiply(-0.2);

                    location.add(v);
                    location.add(v2);
                    for (int k = 0; k < getModifiedAmount(3); k++) {
                        Particles.REDSTONE.display(
                                nextColor.getRed(),
                                nextColor.getGreen(),
                                nextColor.getBlue(),
                                location
                        );
                    }
                    location.subtract(v2);
                    location.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }

    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector getBackVector(Location loc) {
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    /**
     * Generates a color in the rainbow sequence based on the current time.
     *
     * @return a Particles.OrdinaryColor representing the color for the current time.
     */
    public static Particles.OrdinaryColor getColor() {
        long currentEpochMilliseconds = System.currentTimeMillis();
        long currentEpochSeconds = currentEpochMilliseconds / 1000L;

        double secondsPerPhase = 2;
        double phase = currentEpochSeconds % (6 * secondsPerPhase);
        phase = phase / secondsPerPhase;

        double millisecondsToAdjustBy = currentEpochMilliseconds % (secondsPerPhase * 1000);

        // There's a change of a specific number of rgb values in a single tick. This calculation is basically the
        // current tick (or milliseconds) multiplied by 255 to get the rgb value.
        int currentRotatingRgbValue = (int) (millisecondsToAdjustBy * 0.255 / secondsPerPhase);

        int red = 0;
        int green = 0;
        int blue = 0;

        // There are six phases in a rainbow when adjusting rgb values. This if-else encapsulates those six phases.
        if (phase >= 0 && phase < 1) { // Red is at 255, Green goes to 255
            red = 255;
            green = currentRotatingRgbValue;
        } else if (phase >= 1 && phase < 2) { // Red goes to 0
            red = 255 - currentRotatingRgbValue;
            green = 255;
        } else if (phase >= 2 && phase < 3) { // Blue goes to 255
            green = 255;
            blue = currentRotatingRgbValue;
        } else if (phase >= 3 && phase < 4) { // Green goes to 0
            green = 255 - currentRotatingRgbValue;
            blue = 255;
        } else if (phase >= 4 && phase < 5) { // Red goes to 255
            red = currentRotatingRgbValue;
            blue = 255;
        } else if (phase >= 5 && phase < 6) { // Blue goes to 0
            red = 255;
            blue = 255 - currentRotatingRgbValue;
        }

        Bukkit.getServer().getLogger().warning("currentSeconds: " + currentEpochSeconds);
        return new Particles.OrdinaryColor(red, green, blue);
    }
}

