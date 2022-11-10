package be.isach.ultracosmetics.version;

import be.isach.ultracosmetics.cosmetics.morphs.Morph;
import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.cosmetics.pets.Pet;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IModule {
    public boolean enable();

    public void disable();

    public Class<? extends Mount> getSpiderClass();

    public Class<? extends Mount> getSlimeClass();

    Class<? extends Pet> getPumplingClass();

    Class<? extends Morph> getElderGuardianClass();

    void spawnFirework(Location location, FireworkEffect effect, Player... players);
}
