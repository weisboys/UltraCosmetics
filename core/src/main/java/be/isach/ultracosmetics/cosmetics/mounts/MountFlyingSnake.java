package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.version.ServerVersion;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a moltonsnake mount.
 *
 * @author iSach
 * @since 11-28-2015
 */
public abstract class MountFlyingSnake extends Mount {
    private static final boolean ROTATION_API = UltraCosmeticsData.get().getServerVersion().isAtLeast(ServerVersion.v1_13);
    private final ItemStack tailItem;
    private final List<ArmorStand> tail = new ArrayList<>();

    public MountFlyingSnake(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics, ItemStack tailItem) {
        super(owner, type, ultraCosmetics);
        this.tailItem = tailItem;
    }

    @Override
    public void setupEntity() {
        setupMainEntity();
        summonTailPart(25);
    }

    public abstract void setupMainEntity();

    @Override
    public void onUpdate() {
        Player player = getPlayer();
        if (player == null) return;
        Vector playerVector = player.getLocation().getDirection().multiply(0.7);
        entity.setVelocity(playerVector);
        Location loc;
        Location lastLocation = entity.getLocation().subtract(0, 1.3, 0);
        for (final ArmorStand armorStand : tail) {
            loc = armorStand.getLocation();
            Location deltaLoc = loc.clone().subtract(lastLocation);
            double yaw = -Math.atan2(deltaLoc.getX(), deltaLoc.getZ());
            double hyp = deltaLoc.getX() * deltaLoc.getX() + deltaLoc.getZ() * deltaLoc.getZ();
            double pitch = Math.atan2(Math.sqrt(hyp), deltaLoc.getY());
            armorStand.teleport(lastLocation);
            // Setting yaw by rotating the whole armor stand is less buggy
            if (ROTATION_API) {
                armorStand.setHeadPose(new EulerAngle(pitch, 0, 0));
                armorStand.setRotation((float) Math.toDegrees(yaw), 0);
            } else {
                armorStand.setHeadPose(new EulerAngle(pitch, yaw, 0));
            }
            lastLocation = loc;
        }
    }

    private void summonTailPart(int j) {
        Location location = getPlayer().getLocation();
        Vector v = getPlayer().getLocation().getDirection().multiply(-0.25);
        for (int i = 0; i < j; i++) {
            ArmorStand armorStand = getPlayer().getWorld().spawn(location.add(v), ArmorStand.class);
            tail.add(armorStand);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            if (i > 0) {
                setHelmet(armorStand, tailItem);
            }
            armorStand.setMetadata("NO_INTER", new FixedMetadataValue(getUltraCosmetics(), ""));
        }
    }

    @SuppressWarnings("deprecation")
    private void setHelmet(ArmorStand stand, ItemStack itemStack) {
        stand.setHelmet(itemStack);
    }

    @Override
    public void onClear() {
        super.onClear();
        for (Entity entity : tail) {
            entity.remove();
        }
        tail.clear();
    }
}
