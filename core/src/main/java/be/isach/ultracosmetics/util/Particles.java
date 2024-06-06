package be.isach.ultracosmetics.util;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * <b>ParticleEffect Library</b>
 * <p>
 * This library was created by @DarkBlade12 and allows you to display all Minecraft particle effects on a Bukkit server
 * <p>
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <p>
 * Special thanks:
 * <ul>
 * <li>@microgeek (original idea, names and packet parameters)
 * <li>@ShadyPotato (1.8 names, ids and packet parameters)
 * <li>@RingOfStorms (particle behavior)
 * <li>@Cybermaxke (particle behavior)
 * <li>@JamieSinn (hosting a jenkins server and documentation for particleeffect)
 * </ul>
 * <p>
 * <i>It would be nice if you provide credit to me if you use this class in a published project</i>
 *
 * @author DarkBlade12
 * @version 1.7
 */
@SuppressWarnings("DanglingJavadoc")
public enum Particles {
    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    POOF(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by exploding ghast fireballs and wither skulls:
     * <ul>
     * <li>It looks like a gray ball which is fading away
     * <li>The speed value slightly influences the size of this particle effect
     * </ul>
     */
    EXPLOSION(-1),
    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a crowd of gray balls which are fading away
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    EXPLOSION_EMITTER(-1),
    /**
     * A particle effect which is displayed by launching fireworks:
     * <ul>
     * <li>It looks like a white star which is sparkling
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FIREWORK(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by swimming entities and arrows in water:
     * <ul>
     * <li>It looks like a bubble
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    BUBBLE(-1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
    /**
     * A particle effect which is displayed by swimming entities and shaking wolves:
     * <ul>
     * <li>It looks like a blue drop
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SPLASH(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed on water when fishing:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FISHING(7, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by water:
     * <ul>
     * <li>It looks like a tiny blue square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    UNDERWATER(-1, ParticleProperty.REQUIRES_WATER),
    /**
     * A particle effect which is displayed when landing a critical hit and by arrows:
     * <ul>
     * <li>It looks like a light brown cross
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CRIT(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed when landing a hit with an enchanted weapon:
     * <ul>
     * <li>It looks like a cyan star
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    ENCHANTED_HIT(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by primed tnt, torches, droppers, dispensers, end portals, brewing stands and monster spawners:
     * <ul>
     * <li>It looks like a little gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SMOKE(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by fire, minecarts with furnace and blazes:
     * <ul>
     * <li>It looks like a large gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    LARGE_SMOKE(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed when splash potions or bottles o' enchanting hit something:
     * <ul>
     * <li>It looks like a white swirl
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    EFFECT(-1),
    /**
     * A particle effect which is displayed when instant splash potions hit something:
     * <ul>
     * <li>It looks like a white cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    INSTANT_EFFECT(-1),
    /**
     * A particle effect which is displayed by entities with active potion effects:
     * <ul>
     * <li>It looks like a colored swirl
     * <li>The speed value causes the particle to be colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the speed
     * </ul>
     */
    ENTITY_EFFECT(-1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed by entities with active potion effects applied through a beacon:
     * <ul>
     * <li>It looks like a transparent colored swirl
     * <li>The speed value causes the particle to be always colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the speed
     * </ul>
     */
    ENTITY_EFFECT_AMBIENT(-1, ParticleProperty.COLORABLE, ParticleProperty.TRANSPARENT),
    /**
     * A particle effect which is displayed by witches:
     * <ul>
     * <li>It looks like a purple cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * <li>Only the motion on the y-axis can be controlled, the motion on the x- and z-axis are multiplied by 0.1 when setting the values to 0
     * </ul>
     */
    WITCH(-1),
    /**
     * A particle effect which is displayed by BLOCKS beneath a water source:
     * <ul>
     * <li>It looks like a blue drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIPPING_WATER(-1),
    /**
     * A particle effect which is displayed by BLOCKS beneath a lava source:
     * <ul>
     * <li>It looks like an orange drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIPPING_LAVA(-1),
    /**
     * A particle effect which is displayed when attacking a villager in a village:
     * <ul>
     * <li>It looks like a cracked gray heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    ANGRY_VILLAGER(-1),
    /**
     * A particle effect which is displayed when using bone meal and trading with a villager in a village:
     * <ul>
     * <li>It looks like a green star
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HAPPY_VILLAGER(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by mycelium:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    MYCELIUM(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by note BLOCKS:
     * <ul>
     * <li>It looks like a colored note
     * <li>The speed value causes the particle to be colored green when set to 0
     * </ul>
     */
    NOTE(-1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is displayed by nether portals, endermen, ender pearls, eyes of ender, ender chests and dragon eggs:
     * <ul>
     * <li>It looks like a purple cloud
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    PORTAL(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by enchantment tables which are nearby bookshelves:
     * <ul>
     * <li>It looks like a cryptic white letter
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    ENCHANT(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by torches, active furnaces, magma cubes and monster spawners:
     * <ul>
     * <li>It looks like a tiny flame
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FLAME(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by lava:
     * <ul>
     * <li>It looks like a spark
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    LAVA(-1),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a transparent gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    // FOOTSTEP("footstep", 28, -1), Doesn't support 1.13 Particle
    /**
     * A particle effect which is displayed when a mob dies:
     * <ul>
     * <li>It looks like a large white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CLOUD(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by redstone ore, powered redstone, redstone torches and redstone repeaters:
     * <ul>
     * <li>It looks like a tiny colored cloud
     * <li>The speed value causes the particle to be colored red when set to 0
     * </ul>
     */
    DUST(-1, ParticleProperty.COLORABLE),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a tiny white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SNOW_SHOVEL(-1, ParticleProperty.DIRECTIONAL),
    /**
     * A particle effect which is displayed by slimes:
     * <ul>
     * <li>It looks like a tiny part of the slimeball icon
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    ITEM_SLIME(-1),
    /**
     * A particle effect which is displayed when breeding and taming animals:
     * <ul>
     * <li>It looks like a red heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HEART(-1),
    /**
     * A particle effect which is displayed when breaking a tool or eggs hit a block:
     * <ul>
     * <li>It looks like a little piece with an item texture
     * </ul>
     */
    ITEM(-1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when breaking BLOCKS or sprinting:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    BLOCK_CRACK(-1, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when falling:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * </ul>
     */
    BLOCK(7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed when rain hits the ground:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    RAIN(8),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It has no visual effect
     * </ul>
     */
    // ITEM_TAKE("take", 40, 8), Doesn't support 1.13 Particle
    /**
     * A particle effect which is displayed by elder guardians:
     * <ul>
     * <li>It looks like the shape of the elder guardian
     * <li>The speed value has no influence on this particle effect
     * <li>The offset values have no influence on this particle effect
     * </ul>
     */
    ELDER_GUARDIAN(8),
    /**
     * A particle effect which is displayed by ender dragons:
     * <ul>
     * <li>It looks like a purple cloud
     * </ul>
     */
    DRAGON_BREATH(9),
    /**
     * A particle effect which is displayed by squids:
     * <ul>
     * <li>It looks like a black cloud
     * </ul>
     */
    SQUID_INK(13),
    /**
     * A particle effect which is displayed by soul torches:
     * <ul>
     * <li>It looks like a cyan flame
     * </ul>
     */
    SOUL_FIRE_FLAME(16),
    /**
     * A particle effect which is displayed by powder snow:
     * <ul>
     * <li>It looks like a small, white cloud
     * </ul>
     */
    SNOWFLAKE(17),
    /**
     * A particle effect which is displayed by sculk sensors:
     * <ul>
     * <li>It looks like a cloud, which changes its color from blue to red
     * </ul>
     */
    DUST_COLOR_TRANSITION(17, ParticleProperty.REQUIRES_DATA),
    /**
     * A particle effect which is displayed by glow squids:
     * <ul>
     * <li>It looks like a teal star
     * </ul>
     */
    GLOW(17),
    /**
     * A particle effect which is displayed by glow squids:
     * <ul>
     * <li>It looks like an aquamarine cloud
     * </ul>
     */
    GLOW_SQUID_INK(17),
    /**
     * A particle effect which is displayed by copper blocks:
     * <ul>
     * <li>It looks like a teal star
     * </ul>
     */
    SCRAPE(17),
    /**
     * A particle effect which is displayed by copper blocks:
     * <ul>
     * <li>It looks like a white star
     * </ul>
     */
    WAX_OFF(17),
    /**
     * A particle effect which is displayed by copper blocks:
     * <ul>
     * <li>It looks like an orange star
     * </ul>
     */
    WAX_ON(17),
    /**
     * A particle effect which is displayed by skulk catalysts:
     * <ul>
     * <li>It looks like a cyan soul
     * </ul>
     */
    SCULK_SOUL(19);

    public static final int DEF_RADIUS = 128; // for convenience functions by iSach
    public static final int AMBIENT_ALPHA = 38;
    private final int requiredVersion;
    private final List<ParticleProperty> properties;

    /**
     * Construct a new particle effect
     *
     * @param requiredVersion Version which is required (1.x)
     * @param properties      Properties of this particle effect
     */
    private Particles(int requiredVersion, ParticleProperty... properties) {
        this.requiredVersion = requiredVersion;
        this.properties = Arrays.asList(properties);
    }

    /**
     * Determine if this particle effect has a specific property
     *
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return properties.contains(property);
    }

    /**
     * Determine if this particle effect is supported by your current server version
     *
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        return requiredVersion == -1 || ParticlePacket.getVersion() >= requiredVersion;
    }

    /**
     * Determine if water is at a certain location
     *
     * @param location Location to check
     * @return Whether water is at this location or not
     */
    private static boolean isWater(Location location) {
        Material material = location.getBlock().getType();
        return material.name().endsWith("WATER"); // matches WATER and STATIONARY_WATER
    }

    /**
     * Determine if the distance between @param location and one of the players exceeds 256
     *
     * @param location Location to check
     * @return Whether the distance exceeds 256 or not
     */
    private static boolean isLongDistance(Location location, List<Player> players) {
        String world = location.getWorld().getName();
        for (Player player : players) {
            Location playerLocation = player.getLocation();
            if (!world.equals(playerLocation.getWorld().getName()) || playerLocation.distanceSquared(location) < 65536) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Determine if the data type for a particle effect is correct
     *
     * @param effect Particle effect
     * @param data   Particle data
     * @return Whether the data type is correct or not
     */
    private static boolean isDataCorrect(Particles effect, ParticleData data) {
        return ((effect == BLOCK_CRACK || effect == BLOCK) && data instanceof BlockData) || (effect == ITEM && data instanceof ItemData);
    }

    /**
     * Determine if the color type for a particle effect is correct
     *
     * @param effect Particle effect
     * @param color  Particle color
     * @return Whether the color type is correct or not
     */
    private static boolean isColorCorrect(Particles effect, ParticleColor color) {
        return ((effect == ENTITY_EFFECT || effect == ENTITY_EFFECT_AMBIENT || effect == DUST) && color instanceof OrdinaryColor) || (effect == NOTE && color instanceof NoteColor);
    }

    /**
     * Displays a particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the center location
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, null).sendTo(center, range);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the center location
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), null).sendTo(center, players);
    }

    /**
     * Displays a particle effect which is only visible for players in the default radius
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect requires water and none is at the center location
     * @see #display(float, float, float, float, int, Location, List)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(offsetX, offsetY, offsetZ, speed, amount, center, DEF_RADIUS);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for all players within a certain range in the world of @param center
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @param range     Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires water and none is at the center location
     * @see ParticlePacket#ParticlePacket(Particles, Vector, float, boolean, ParticleData)
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(Vector direction, float speed, Location center, double range) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, range > 256, null).sendTo(center, range);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for the specified players
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires water and none is at the center location
     * @see ParticlePacket#ParticlePacket(Particles, Vector, float, boolean, ParticleData)
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), null).sendTo(center, players);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for players in the default radius
     *
     * @param direction Direction of the particle
     * @param speed     Display speed of the particle
     * @param center    Center location of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect requires additional data
     * @throws IllegalArgumentException If the particle effect is not directional or if it requires water and none is at the center location
     * @see #display(Vector, float, Location, List)
     */
    public void display(Vector direction, float speed, Location center) throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(direction, speed, center, DEF_RADIUS);
    }

    /**
     * Displays a single particle which is colored and only visible for all players within a certain range in the world of @param center
     *
     * @param color  Color of the particle
     * @param center Center location of the effect
     * @param range  Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is incorrect
     * @see ParticlePacket#ParticlePacket(Particles, ParticleColor, boolean)
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleColor color, Location center, double range) throws ParticleVersionException, ParticleColorException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }
        if (!isColorCorrect(this, color)) {
            throw new ParticleColorException("The particle color type is incorrect");
        }
        new ParticlePacket(this, color, range > 256).sendTo(center, range);
    }

    /**
     * Displays a single particle which is colored and only visible for the specified players
     *
     * @param color   Color of the particle
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is incorrect
     * @see ParticlePacket#ParticlePacket(Particles, ParticleColor, boolean)
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleColor color, Location center, List<Player> players) throws ParticleVersionException, ParticleColorException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }
        if (!isColorCorrect(this, color)) {
            throw new ParticleColorException("The particle color type is incorrect");
        }
        new ParticlePacket(this, color, isLongDistance(center, players)).sendTo(center, players);
    }

    /**
     * Displays a single particle which is colored and only visible for players in the default radius
     *
     * @param color  Color of the particle
     * @param center Center location of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleColorException   If the particle effect is not colorable or the color type is incorrect
     * @see #display(ParticleColor, Location, List)
     */
    public void display(ParticleColor color, Location center) throws ParticleVersionException, ParticleColorException {
        display(color, center, DEF_RADIUS);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for all players within a certain range in the world of @param center
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, data).sendTo(center, range);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for the specified players
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data).sendTo(center, players);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for players in the default radius
     *
     * @param data    Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param center  Center location of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see #display(ParticleData, float, float, float, float, int, Location, List)
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center) throws ParticleVersionException, ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center, DEF_RADIUS);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for all players within a certain range in the world of @param center
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @param range     Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, double)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, direction, speed, range > 256, data).sendTo(center, range);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for the specified players
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @param players   Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see ParticlePacket
     * @see ParticlePacket#sendTo(Location, List)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), data).sendTo(center, players);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for players in the default radius
     *
     * @param data      Data of the effect
     * @param direction Direction of the particle
     * @param speed     Display speed of the particles
     * @param center    Center location of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @throws ParticleDataException    If the particle effect does not require additional data or if the data type is incorrect
     * @see #display(ParticleData, Vector, float, Location, List)
     */
    public void display(ParticleData data, Vector direction, float speed, Location center) throws ParticleVersionException, ParticleDataException {
        display(data, direction, speed, center, DEF_RADIUS);
    }

    // Start convenience functions originally by iSach
    public void drawParticleLine(Location from, Location to, int particles, Color color) {
        drawParticleLine(from, to, particles, new OrdinaryColor(color));
    }

    public void drawParticleLine(Location from, Location to, int particles, int r, int g, int b) {
        drawParticleLine(from, to, particles, new OrdinaryColor(r, g, b));
    }

    public void drawParticleLine(Location from, Location to, int particles, OrdinaryColor color) {
        Location location = from.clone();
        Location target = to.clone();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        int step = 0;
        for (int i = 0; i < particles; i++) {
            if (step >= (double) particles) step = 0;
            step++;
            loc.add(v);
            if (this == Particles.DUST) {
                display(color, loc, DEF_RADIUS);
            } else {
                display(0, 0, 0, 0, 1, loc, DEF_RADIUS);
            }
        }
    }

    public void playHelix(final Location loc, final float i) {
        BukkitRunnable runnable = new BukkitRunnable() {
            double radius = 0;
            double step;
            double y = loc.getY();
            Location location = loc.clone().add(0, 3, 0);

            @Override
            public void run() {
                double inc = (2 * Math.PI) / 50;
                double angle = step * inc + i;
                Vector v = new Vector();
                v.setX(Math.cos(angle) * radius);
                v.setZ(Math.sin(angle) * radius);
                if (Particles.this == Particles.DUST) {
                    display(0, 0, 255, location, 1);
                } else {
                    display(location);
                }
                location.subtract(v);
                location.subtract(0, 0.1d, 0);
                if (location.getY() <= y) {
                    cancel();
                }
                step += 4;
                radius += 1 / 50f;
            }
        };
        runnable.runTaskTimer(UltraCosmeticsData.get().getPlugin(), 0, 1);
    }

    public void display(Location location, int amount, float speed) {
        display(0, 0, 0, speed, amount, location, DEF_RADIUS);
    }

    public void display(Location location, int amount) {
        display(0, 0, 0, 0, amount, location, DEF_RADIUS);
    }

    public void display(Location location) {
        display(location, 1);
    }

    public void display(double x, double y, double z, Location location, int amount) {
        display((float) x, (float) y, (float) z, 0f, amount, location, DEF_RADIUS);
    }

    public void display(int red, int green, int blue, Location location, int amount) {
        for (int i = 0; i < amount; i++) {
            display(new Particles.OrdinaryColor(red, green, blue), location, DEF_RADIUS);
        }
    }

    public void display(int red, int green, int blue, Location location) {
        display(red, green, blue, location, 1);
    }
    // End convenience functions

    /**
     * Represents the property of a particle effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public enum ParticleProperty {
        /**
         * The particle effect requires water to be displayed
         */
        REQUIRES_WATER,
        /**
         * The particle effect requires block or item data to be displayed
         */
        REQUIRES_DATA,
        /**
         * The particle effect uses the offsets as direction values
         */
        DIRECTIONAL,
        /**
         * The particle effect uses the offsets as color values
         */
        COLORABLE,
        /**
         * Marker that the alpha value of the particle should be set
         */
        TRANSPARENT
    }

    /**
     * Represents the particle data for effects like {@link Particles#ITEM}, {@link Particles#BLOCK_CRACK} and {@link Particles#BLOCK}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public abstract static class ParticleData {
        private final Material material;
        private final byte data;
        private final int[] packetData;

        /**
         * Construct a new particle data
         *
         * @param material Material of the item/block
         * @param data     Data value of the item/block
         */
        @SuppressWarnings("deprecation")
        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
            this.packetData = new int[] {material.getId(), data};
        }

        /**
         * Returns the material of this data
         *
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }

        /**
         * Returns the data value of this data
         *
         * @return The data value
         */
        public byte getData() {
            return data;
        }

        /**
         * Returns the data as an int array for packet construction
         *
         * @return The data for the packet
         */
        public int[] getPacketData() {
            return packetData;
        }

        /**
         * Returns the data as a string for pre 1.8 versions
         *
         * @return The data string for the packet
         */
        public String getPacketDataString() {
            return "_" + packetData[0] + "_" + packetData[1];
        }
    }

    /**
     * Represents the item data for the {@link Particles#ITEM} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class ItemData extends ParticleData {
        /**
         * Construct a new item data
         *
         * @param material Material of the item
         * @param data     Data value of the item
         * @see ParticleData#ParticleData(Material, byte)
         */
        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    /**
     * Represents the block data for the {@link Particles#BLOCK_CRACK} and {@link Particles#BLOCK} effects
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class BlockData extends ParticleData {
        /**
         * Construct a new block data
         *
         * @param material Material of the block
         * @param data     Data value of the block
         * @throws IllegalArgumentException If the material is not a block
         * @see ParticleData#ParticleData(Material, byte)
         */
        public BlockData(Material material, byte data) throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }
    }

    /**
     * Represents the color for effects like {@link Particles#ENTITY_EFFECT}, {@link Particles#ENTITY_EFFECT_AMBIENT}, {@link Particles#DUST} and {@link Particles#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public abstract static class ParticleColor {
        /**
         * Returns the value for the offsetX field
         *
         * @return The offsetX value
         */
        public abstract float getValueX();

        /**
         * Returns the value for the offsetY field
         *
         * @return The offsetY value
         */
        public abstract float getValueY();

        /**
         * Returns the value for the offsetZ field
         *
         * @return The offsetZ value
         */
        public abstract float getValueZ();
    }

    /**
     * Represents the color for effects like {@link Particles#ENTITY_EFFECT}, {@link Particles#ENTITY_EFFECT_AMBIENT} and {@link Particles#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class OrdinaryColor extends ParticleColor {
        private final int red;
        private final int green;
        private final int blue;

        /**
         * Construct a new ordinary color
         *
         * @param red   Red value of the RGB format
         * @param green Green value of the RGB format
         * @param blue  Blue value of the RGB format
         * @throws IllegalArgumentException If one of the values is lower than 0 or higher than 255
         */
        public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
            this.red = Math.min(255, red);
            this.green = Math.min(255, green);
            this.blue = Math.min(255, blue);
        }

        /**
         * Construct a new ordinary color
         *
         * @param color Bukkit color
         */
        public OrdinaryColor(Color color) {
            this(color.getRed(), color.getGreen(), color.getBlue());
        }

        /**
         * Returns the red value of the RGB format
         *
         * @return The red value
         */
        public int getRed() {
            return red;
        }

        /**
         * Returns the green value of the RGB format
         *
         * @return The green value
         */
        public int getGreen() {
            return green;
        }

        /**
         * Returns the blue value of the RGB format
         *
         * @return The blue value
         */
        public int getBlue() {
            return blue;
        }

        /**
         * Returns the red value divided by 255
         *
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return red / 255F;
        }

        /**
         * Returns the green value divided by 255
         *
         * @return The offsetY value
         */
        @Override
        public float getValueY() {
            return green / 255F;
        }

        /**
         * Returns the blue value divided by 255
         *
         * @return The offsetZ value
         */
        @Override
        public float getValueZ() {
            return blue / 255F;
        }
    }

    /**
     * Represents the color for the {@link Particles#NOTE} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class NoteColor extends ParticleColor {
        private final int note;

        /**
         * Construct a new note color
         *
         * @param note Note id which determines color
         * @throws IllegalArgumentException If the note value is lower than 0 or higher than 24
         */
        public NoteColor(int note) throws IllegalArgumentException {
            if (note < 0) {
                throw new IllegalArgumentException("The note value is lower than 0");
            }
            if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            }
            this.note = note;
        }

        /**
         * Returns the note value divided by 24
         *
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return note / 24F;
        }

        /**
         * Returns zero because the offsetY value is unused
         *
         * @return zero
         */
        @Override
        public float getValueY() {
            return 0;
        }

        /**
         * Returns zero because the offsetZ value is unused
         *
         * @return zero
         */
        @Override
        public float getValueZ() {
            return 0;
        }

    }

    /**
     * Represents a runtime exception that is thrown either if the displayed particle effect requires data and has none or vice-versa or if the data type is incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         *
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed particle effect is not colorable or if the particle color type is incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    private static final class ParticleColorException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle color exception
         *
         * @param message Message that will be logged
         */
        public ParticleColorException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown if the displayed particle effect requires a newer version
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleVersionException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle version exception
         *
         * @param message Message that will be logged
         */
        public ParticleVersionException(String message) {
            super(message);
        }
    }

    /**
     * Represents a particle effect packet with all attributes which is used for sending packets to the players
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.5
     */
    public static final class ParticlePacket {
        private static int version;
        private static boolean initialized;
        private final Particles effect;
        private float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private final ParticleData data;
        private Object packet;

        /**
         * Construct a new particle packet
         *
         * @param effect       Particle effect
         * @param offsetX      Maximum distance particles can fly away from the center on the x-axis
         * @param offsetY      Maximum distance particles can fly away from the center on the y-axis
         * @param offsetZ      Maximum distance particles can fly away from the center on the z-axis
         * @param speed        Display speed of the particles
         * @param amount       Amount of particles
         * @param longDistance Indicates whether the maximum distance is increased from 256 to 65536
         * @param data         Data of the effect
         * @throws IllegalArgumentException If the speed or amount is lower than 0
         * @see #initialize()
         */
        public ParticlePacket(Particles effect, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance, ParticleData data) throws IllegalArgumentException {
            initialize();
            if (speed < 0) {
                throw new IllegalArgumentException("The speed is lower than 0");
            }
            if (amount < 0) {
                throw new IllegalArgumentException("The amount is lower than 0");
            }
            this.effect = effect;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
            this.data = data;
        }

        /**
         * Construct a new particle packet of a single particle flying into a determined direction
         *
         * @param effect       Particle effect
         * @param direction    Direction of the particle
         * @param speed        Display speed of the particle
         * @param longDistance Indicates whether the maximum distance is increased from 256 to 65536
         * @param data         Data of the effect
         * @throws IllegalArgumentException If the speed is lower than 0
         * @see #ParticlePacket(Particles, float, float, float, float, int, boolean, ParticleData)
         */
        public ParticlePacket(Particles effect, Vector direction, float speed, boolean longDistance, ParticleData data) throws IllegalArgumentException {
            this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, longDistance, data);
        }

        /**
         * Construct a new particle packet of a single colored particle
         *
         * @param effect       Particle effect
         * @param color        Color of the particle
         * @param longDistance Indicates whether the maximum distance is increased from 256 to 65536
         * @see #ParticlePacket(Particles, float, float, float, float, int, boolean, ParticleData)
         */
        public ParticlePacket(Particles effect, ParticleColor color, boolean longDistance) {
            this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1, 0, longDistance, null);
            if (effect == Particles.DUST && color instanceof OrdinaryColor && ((OrdinaryColor) color).getRed() == 0) {
                offsetX = Float.MIN_NORMAL;
            }
        }

        public static void initialize() {
            version = Integer.parseInt(ServerVersion.getMinecraftVersion().split("\\.")[1]);
            initialized = true;
        }

        /**
         * Returns the version of your server (1.x)
         *
         * @return The version number
         */
        public static int getVersion() {
            if (!initialized) {
                initialize();
            }
            return version;
        }

        /**
         * Initializes {@link #packet} with all set values
         *
         * @param center Center location of the effect
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         */
        private void initializePacket(Location center) throws PacketInstantiationException {
            if (packet != null) {
                return;
            }
            Particle particle;
            if (effect == ENTITY_EFFECT_AMBIENT) {
                particle = XParticle.ENTITY_EFFECT.get();
            } else {
                particle = XParticle.of(effect.toString()).get();
            }
            if (particle == null) {
                throw new IllegalArgumentException("This particle effect (" + effect + ") is not supported by your server version");
            }
            if (effect == Particles.DUST) {
                int r = (int) (offsetX * 255);
                int g = (int) (offsetY * 255);
                int b = (int) (offsetZ * 255);
                if (r == 0 && g == 0 && b == 0) { // Normal redstone particle, no color data supplied
                    r = 255;
                }
                center.getWorld().spawnParticle(particle, center, 0, new org.bukkit.Particle.DustOptions(org.bukkit.Color.fromRGB(r, g, b), 1));
            } else if (effect == Particles.ENTITY_EFFECT || effect == Particles.ENTITY_EFFECT_AMBIENT) {
                center.getWorld().spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, 1);
            } else if (effect == Particles.NOTE) {
                center.getWorld().spawnParticle(particle, center, amount, offsetX, 0, 0, 1);
            } else if (effect == Particles.ITEM && data != null) {
                center.getWorld().spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, new ItemStack(data.getMaterial()));
            } else if (effect == Particles.BLOCK_CRACK && data != null) {
                center.getWorld().spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, data.getMaterial().createBlockData());
            } else {
                center.getWorld().spawnParticle(particle, center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
            }
        }

        /**
         * Sends the packet to a single player and caches it
         *
         * @param center Center location of the effect
         * @param player Receiver of the packet
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         * @throws PacketSendingException       If sending fails due to an unknown error
         * @see #initializePacket(Location)
         */
        public void sendTo(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
            initializePacket(center);

        }

        /**
         * Sends the packet to all players in the list
         *
         * @param center  Center location of the effect
         * @param players Receivers of the packet
         * @throws IllegalArgumentException If the player list is empty
         * @see #sendTo(Location center, Player player)
         */
        public void sendTo(Location center, List<Player> players) throws IllegalArgumentException {
            initializePacket(center);
        }

        /**
         * Sends the packet to all players in a certain range
         *
         * @param center Center location of the effect
         * @param range  Range in which players will receive the packet (Maximum range for particles is usually 16, but it can differ for some types)
         * @throws IllegalArgumentException If the range is lower than 1
         * @see #sendTo(Location center, Player player)
         */
        public void sendTo(Location center, double range) throws IllegalArgumentException {
            initializePacket(center);
        }

        /**
         * Represents a runtime exception that is thrown if a bukkit version is not compatible with this library
         * <p>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
         *
         * @author DarkBlade12
         * @since 1.5
         */
        private static final class VersionIncompatibleException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new version incompatible exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public VersionIncompatibleException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        /**
         * Represents a runtime exception that is thrown if packet instantiation fails
         * <p>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketInstantiationException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet instantiation exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public PacketInstantiationException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        /**
         * Represents a runtime exception that is thrown if packet sending fails
         * <p>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketSendingException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet sending exception
             *
             * @param message Message that will be logged
             * @param cause   Cause of the exception
             */
            public PacketSendingException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }
}
