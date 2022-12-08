package be.isach.ultracosmetics.cosmetics.projectileeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ProjectileEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.Particles.NoteColor;

import org.bukkit.Sound;
import org.bukkit.entity.Projectile;

import com.cryptomorin.xseries.XSound;

import java.util.HashMap;
import java.util.Map;

public class ProjectileEffectNote extends ProjectileEffectBasicTrail {
    private static final Sound SOUND = XSound.BLOCK_NOTE_BLOCK_HARP.parseSound();
    private Map<Projectile,Integer> colors = new HashMap<>();

    public ProjectileEffectNote(UltraPlayer owner, ProjectileEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void showParticles(Projectile projectile) {
        int color = colors.computeIfAbsent(projectile, p -> 0);
        getType().getEffect().display(new NoteColor(color), projectile.getLocation(), Particles.DEF_RADIUS);
        // Magic function source: https://minecraft.fandom.com/wiki/Note_Block#Notes
        // I know there is an API for playing note block sounds,
        // but there doesn't seem to be a great way to play them all sequentially.
        float pitch = (float) Math.pow(2, (float) (color - 12) / 12);
        getPlayer().playSound(projectile.getLocation(), SOUND, 1, pitch);
        if (++color > 24) {
            color = 0;
        }
        colors.put(projectile, color);
    }

    @Override
    public void projectileLanded(Projectile projectile) {
        colors.remove(projectile);
    }
}
