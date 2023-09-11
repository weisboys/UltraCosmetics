package be.isach.ultracosmetics.cosmetics.morphs;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.type.MorphType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.util.MathUtils;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a mooshroom morph summoned by a player.
 *
 * @author RadBuilder
 * @since 07-03-2017
 */
public class MorphMooshroom extends Morph implements PlayerAffectingCosmetic {
    public MorphMooshroom(UltraPlayer owner, MorphType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = getPlayer();
        if (!canUseSkill || event.getPlayer() != player || !getOwner().getAndSetCooldown(cosmeticType, 10, 3)) return;
        for (Entity ent : player.getNearbyEntities(3, 3, 3)) {
            if (canAffect(ent, player)) {
                MathUtils.applyVelocity(ent, ent.getLocation().toVector().subtract(player.getLocation().toVector()).setY(1));
            }
        }
        final List<Entity> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Location itemLoc = player.getLocation().add(Math.random() * 5.0D - 2.5D, Math.random() * 3.0D, Math.random() * 5.0D - 2.5D);
            items.add(ItemFactory.spawnUnpickableItem(XMaterial.MUSHROOM_STEW.parseItem(), itemLoc, MathUtils.getRandomVector()));
        }
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            for (Entity soup : items) {
                soup.remove();
            }
            items.clear();
        }, 50);
        XSound.ENTITY_SHEEP_SHEAR.play(player, 0.4f, (float) Math.random() + 1f);
    }
}
