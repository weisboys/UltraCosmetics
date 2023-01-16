package be.isach.ultracosmetics.treasurechests;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.player.UltraPlayerManager;
import be.isach.ultracosmetics.util.BlockUtils;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceBlocksRunnable extends BukkitRunnable {
    private final TreasureChest chest;
    private final TreasureChestDesign design;
    private final boolean large;
    private final UltraCosmetics uc = UltraCosmeticsData.get().getPlugin();
    private int i = 6;
    private ChestParticleRunnable particleRunnable = null;
    public PlaceBlocksRunnable(TreasureChest chest) {
        this.chest = chest;
        this.design = chest.getDesign();
        this.large = chest.isLarge();
    }

    @Override
    public void run() {
        Player player = chest.getPlayer();
        UltraPlayerManager pm = uc.getPlayerManager();
        if (player == null || (pm.getUltraPlayer(player).getCurrentTreasureChest() != chest)) {
            cancel();
            return;
        }
        if (i == 0) {
            particleRunnable = new ChestParticleRunnable(chest);
            particleRunnable.runTaskTimer(uc, 0L, 50L);
            cancel();
            return;
        }
        Block lampBlock;
        if (i == 6) {
            lampBlock = chest.getCenter().add(0.0D, -1.0D, 0.0D).getBlock();
            doChestStage(player.getLocation().subtract(0, 1, 0), ChestBlockPattern.CENTER_BLOCK, design.getCenter());
        } else if (i == 5) {
            doChestStage(chest.getCenter().add(0.0D, -1.0D, 0.0D), ChestBlockPattern.AROUND_CENTER, design.getBlocks2());
            if (!large) i--;
        } else if (i == 4) {
            doChestStage(chest.getCenter().add(0.0D, -1.0D, 0.0D), ChestBlockPattern.LARGE_AROUND_AROUND, design.getBlocks2());
        } else if (i == 3) {
            doChestStage(chest.getCenter().add(0.0D, -1.0D, 0.0D), large ? ChestBlockPattern.LARGE_CORNERS : ChestBlockPattern.CORNERS, design.getBlocks3());
        } else if (i == 2) {
            doChestStage(chest.getCenter().add(0.0D, -1.0D, 0.0D), large ? ChestBlockPattern.LARGE_BELOW_CHEST : ChestBlockPattern.BELOW_CHEST, design.getBelowChests());
        } else if (i == 1) {
            doChestStage(chest.getCenter(), large ? ChestBlockPattern.LARGE_CORNERS : ChestBlockPattern.CORNERS, design.getBarriers());
        }
        i--;
    }

    private void doChestStage(Location center, ChestBlockPattern pattern, XMaterial newData) {
        if (newData == null) return;
        pattern.loop(center, loc -> {
            Block b = loc.getBlock();
            chest.addRestoreBlock(b);
            BlockUtils.treasureBlocks.add(b);
            XBlock.setType(b, newData);
        });
    }

    public void propogateCancel() {
        cancel();
        if (particleRunnable != null) {
            particleRunnable.propogateCancel();
        }
    }
}
