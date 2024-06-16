package be.isach.ultracosmetics.v1_21_R1;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.morphs.Morph;
import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.cosmetics.pets.Pet;
import be.isach.ultracosmetics.util.SmartLogger;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;
import be.isach.ultracosmetics.v1_21_R1.customentities.CustomEntities;
import be.isach.ultracosmetics.v1_21_R1.customentities.CustomEntityFirework;
import be.isach.ultracosmetics.v1_21_R1.customentities.CustomMinecart;
import be.isach.ultracosmetics.v1_21_R1.morphs.MorphElderGuardian;
import be.isach.ultracosmetics.v1_21_R1.mount.MountSlime;
import be.isach.ultracosmetics.v1_21_R1.mount.MountSpider;
import be.isach.ultracosmetics.v1_21_R1.pets.PetPumpling;
import be.isach.ultracosmetics.version.IModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author RadBuilder
 */
public class VersionModule implements IModule {
    @Override
    public boolean enable() {
        try {
            CustomEntities.registerEntities();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            SmartLogger logger = UltraCosmeticsData.get().getPlugin().getSmartLogger();
            logger.write(LogLevel.ERROR, "Failed to initialize NMS module.");
            if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
                logger.write(LogLevel.ERROR, "It looks like you have Citizens installed, please make sure you're running build 2817 or higher.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void disable() {
    }

    @Override
    public Class<? extends Mount> getSpiderClass() {
        return MountSpider.class;
    }

    @Override
    public Class<? extends Mount> getSlimeClass() {
        return MountSlime.class;
    }

    @Override
    public Class<? extends Pet> getPumplingClass() {
        return PetPumpling.class;
    }

    @Override
    public Class<? extends Morph> getElderGuardianClass() {
        return MorphElderGuardian.class;
    }

    @Override
    public org.bukkit.entity.Entity spawnCustomMinecart(Location location) {
        return CustomEntities.spawnEntity(new CustomMinecart(EntityType.MINECART, ((CraftWorld) location.getWorld()).getHandle()), location);
    }

    @Override
    public void removeCustomEntity(org.bukkit.entity.Entity entity) {
        CustomEntities.removeCustomEntity(((CraftEntity) entity).getHandle());
    }

    @Override
    public void spawnFirework(Location location, FireworkEffect effect, Player... players) {
        spawnFirework_(location, effect, players);
    }

    public static void spawnFirework_(Location location, FireworkEffect effect, Player... players) {
        CustomEntityFirework firework = new CustomEntityFirework(((CraftWorld) location.getWorld()).getHandle(), players);
        FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
        meta.addEffect(effect);
        ((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
        ((Entity) firework).setPos(location.getX(), location.getY(), location.getZ());

        if ((((CraftWorld) location.getWorld()).getHandle()).addFreshEntity(firework)) {
            ((Entity) firework).setInvisible(true);
        }
    }
}
