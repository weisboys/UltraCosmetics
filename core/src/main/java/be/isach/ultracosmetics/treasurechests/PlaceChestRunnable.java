package be.isach.ultracosmetics.treasurechests;

import be.isach.ultracosmetics.util.Particles;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XSound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceChestRunnable extends BukkitRunnable {
    private final TreasureChest chest;
    private final Block chestBlock;
    private final int direction;

    public PlaceChestRunnable(TreasureChest chest, Block chestBlock, int direction) {
        this.chest = chest;
        this.chestBlock = chestBlock;
        this.direction = direction;
    }

    @Override
    public void run() {
        chest.addChest(chestBlock, chest.getDesign().getChestType().getType());
        XSound.BLOCK_ANVIL_LAND.play(chest.getPlayer(), 1.4f, 1.5f);
        Particles.SMOKE_LARGE.display(chestBlock.getLocation(), 5);
        Particles.LAVA.display(chestBlock.getLocation(), 5);
        XBlock.setDirection(chestBlock, ChestParticleRunnable.getDirection(direction).getOppositeFace());
    }

}
