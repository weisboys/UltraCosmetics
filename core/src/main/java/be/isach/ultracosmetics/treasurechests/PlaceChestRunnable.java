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
    private final XSound.SoundPlayer sound;

    public PlaceChestRunnable(TreasureChest chest, Block chestBlock, int direction) {
        this.chest = chest;
        this.chestBlock = chestBlock;
        this.direction = direction;
        sound = XSound.BLOCK_ANVIL_LAND.record().withVolume(1.4f).withPitch(1.5f).soundPlayer().forPlayers(chest.getPlayer());
    }

    @Override
    public void run() {
        chest.addChest(chestBlock, chest.getDesign().getChestType().getType());
        sound.play();
        Particles.SMOKE_LARGE.display(chestBlock.getLocation(), 5);
        Particles.LAVA.display(chestBlock.getLocation(), 5);
        XBlock.setDirection(chestBlock, ChestParticleRunnable.getDirection(direction).getOppositeFace());
    }

}
