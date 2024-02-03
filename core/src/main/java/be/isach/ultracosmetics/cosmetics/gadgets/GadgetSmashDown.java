package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.BlockUtils;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.StructureRollback;
import be.isach.ultracosmetics.version.VersionManager;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a smashdown gadget summoned by a player.
 *
 * @author iSach
 * @since 08-08-2015
 */
public class GadgetSmashDown extends Gadget implements PlayerAffectingCosmetic, Updatable {

    private boolean active = false;
    private List<FallingBlock> fallingBlocks = new ArrayList<>();
    private int i = 1;
    private boolean playEffect;
    private final XSound.SoundPlayer useSound;
    private final XSound.SoundPlayer smashSound;
    private final XSound.SoundPlayer landSound;

    public GadgetSmashDown(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        useSound = XSound.ENTITY_FIREWORK_ROCKET_LAUNCH.record().withVolume(2.0f).publicSound(true).soundPlayer().forPlayers(getPlayer());
        smashSound = XSound.ENTITY_GENERIC_EXPLODE.record().withVolume(2.0f).publicSound(true).soundPlayer().forPlayers(getPlayer());
        landSound = XSound.BLOCK_ANVIL_BREAK.record().withVolume(0.05f).publicSound(true).soundPlayer().forPlayers(getPlayer());
    }

    @Override
    protected void onRightClick() {
        useSound.play();
        getPlayer().setVelocity(new Vector(0, 3, 0));
        final BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (getOwner() != null && getPlayer() != null && isEquipped()) {
                    Particles.CLOUD.display(getPlayer().getLocation());
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(getUltraCosmetics(), 0, 1);
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            if (getOwner() != null && getPlayer() != null && isEquipped()) {
                task.cancel();
                getOwner().applyVelocity(new Vector(0, -3, 0));
                active = true;
            }
        }, 25);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (active && event.getEntity() == getPlayer()) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUpdate() {
        if (active && getPlayer().isOnGround()) {
            this.playEffect = true;
            Bukkit.getScheduler().runTaskLaterAsynchronously(getUltraCosmetics(), () -> active = false, 5);
            return;
        }

        if (!playEffect) {
            return;
        }

        Location loc = getPlayer().getLocation();
        smashSound.play();

        if (i == 5) {
            playEffect = false;
            active = false;
            i = 1;
            return;
        }
        if (getOwner().getCurrentGadget() != this) {
            playEffect = false;
            active = false;
            return;
        }
        for (Block b : BlockUtils.getBlocksInRadius(loc.clone().add(0, -1, 0), i, true)) {
            if (b.getLocation().getBlockY() == loc.getBlockY() - 1) {
                if (!BlockUtils.isBadMaterial(b.getType())
                        && !StructureRollback.isBlockRollingBack(b)
                        && b.getType().isSolid()
                        && BlockUtils.isAir(b.getRelative(BlockFace.UP).getType())) {
                    Bukkit.getScheduler().runTask(getUltraCosmetics(), () -> {
                        FallingBlock fb = BlockUtils.spawnFallingBlock(b.getLocation().clone().add(0, 1.1f, 0), b);

                        fb.setVelocity(new Vector(0, 0.3f, 0));
                        fb.setDropItem(false);
                        fallingBlocks.add(fb);
                        Player player = getPlayer();
                        fb.getNearbyEntities(1, 1, 1).stream().filter(ent -> ent != player
                                        && !(ent instanceof FallingBlock) && canAffect(ent, player))
                                .forEach(ent -> MathUtils.applyVelocity(ent, new Vector(0, 0.5, 0)));
                    });
                }
            }
        }
        i++;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockChangeState(EntityChangeBlockEvent event) {
        if (!fallingBlocks.remove(event.getEntity())) {
            return;
        }
        event.setCancelled(true);
        FallingBlock fb = (FallingBlock) event.getEntity();
        if (VersionManager.IS_VERSION_1_13) {
            BlockData data = fb.getBlockData();
            fb.getWorld().spawnParticle(Particle.BLOCK_CRACK, fb.getLocation(), 50, 0, 0, 0, 0.4d, data);
        } else {
            Particles.BLOCK_CRACK.display(new Particles.BlockData(fb.getMaterial(), event.getBlock().getData()), 0f, 0f, 0f, 0.4f, 50, fb.getLocation(), 128);
        }
        landSound.play();
        event.getEntity().remove();
    }

    @Override
    public void onClear() {
        for (FallingBlock block : fallingBlocks) {
            block.remove();
        }
    }
}
