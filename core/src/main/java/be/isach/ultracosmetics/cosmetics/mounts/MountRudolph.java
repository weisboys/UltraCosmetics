package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.PlayerUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 * Created by sacha on 1/03/17.
 */
public class MountRudolph extends MountHorse {

    private ArmorStand left, right;

    public MountRudolph(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, type);
    }

    @Override
    public void setupEntity() {
        super.setupEntity();
        left = spawnArmorStand(false);
        right = spawnArmorStand(true);
        moveAntlers();
    }

    @SuppressWarnings("deprecation")
    private ArmorStand spawnArmorStand(boolean right) {
        ArmorStand armorStand = getEntity().getWorld().spawn(getEyeLocation(), ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setArms(true);
        armorStand.setVisible(false);
        if (!right) {
            armorStand.setRightArmPose(new EulerAngle(Math.PI, Math.PI / 4, -Math.PI / 4));
        } else {
            armorStand.setRightArmPose(new EulerAngle(Math.PI, -Math.PI / 4, Math.PI / 4));
        }
        armorStand.setItemInHand(new ItemStack(Material.DEAD_BUSH));
        armorStand.setMetadata("C_AD_ArmorStand", new FixedMetadataValue(getUltraCosmetics(), getPlayer().getUniqueId().toString()));
        getUltraCosmetics().getArmorStandManager().makeUcStand(armorStand);
        return armorStand;
    }

    @Override
    public void onUpdate() {
        if (left != null && right != null) moveAntlers();
    }

    private void moveAntlers() {
        Location location = getEyeLocation();

        double radians = Math.toRadians(location.getYaw());
        Vector leftVector = new Vector(Math.cos(radians), 0, Math.sin(radians)).multiply(0.5);
        Vector rightVector = leftVector.clone().multiply(0.4);
        leftVector.multiply(1.6);

        location.add(PlayerUtils.getHorizontalDirection(getPlayer(), 0.75)).subtract(0, 1.7, 0);

        left.teleport(location.clone().add(leftVector));
        right.teleport(location.clone().add(rightVector));

        Location noseLocation = getEyeLocation();
        double y = noseLocation.getY();
        noseLocation.add(noseLocation.getDirection().multiply(1.15));
        noseLocation.setY(y + 0.127);
        Particles.REDSTONE.display(255, 0, 0, noseLocation);
        // Improves update time for antlers, but not a critical feature
        if (!UltraCosmeticsData.get().getServerVersion().isNmsSupported()) return;
        new Thread(() -> {
            for (Player player : getPlayer().getWorld().getPlayers()) {
                if (noseLocation.distanceSquared(player.getLocation()) > 32 * 32) continue;
                UltraCosmeticsData.get().getVersionManager().getEntityUtil().sendTeleportPacket(player, right);
                UltraCosmeticsData.get().getVersionManager().getEntityUtil().sendTeleportPacket(player, left);
            }
        }).start();
    }

    @Override
    public void onClear() {
        super.onClear();

        if (left != null) left.remove();
        if (right != null) right.remove();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Horse.Variant getVariant() {
        return Horse.Variant.MULE;
    }

    @Override
    protected Color getColor() {
        return null;
    }

    private Location getEyeLocation() {
        return ((LivingEntity) entity).getEyeLocation();
    }
}
