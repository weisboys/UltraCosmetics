package be.isach.ultracosmetics.util;

import be.isach.ultracosmetics.UltraCosmeticsData;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Set;

public class BlockRollback implements Listener {
    private static final Set<BlockRollback> INSTANCES = new HashSet<>();

    public static boolean isBlockRollingBack(Block block) {
        for (BlockRollback rollback : INSTANCES) {
            if (rollback.containsBlock(block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockRollingBackInArea(Area area) {
        for (BlockRollback rollback : INSTANCES) {
            for (Block block : rollback.states.keySet()) {
                if (area.contains(block)) return true;
            }
        }
        return false;
    }

    private final HashMap<Block, BlockState> states = new LinkedHashMap<>();

    public BlockRollback() {
        Bukkit.getPluginManager().registerEvents(this, UltraCosmeticsData.get().getPlugin());
        INSTANCES.add(this);
    }

    public boolean containsBlock(Block block) {
        return states.containsKey(block);
    }

    public void setToRestore(Block block, XMaterial newType) {
        setToRestore(block, newType, true);
    }

    public void setToRestore(Block block, Material newType) {
        setToRestore(block, newType, true);
    }

    public void setToRestore(Block block, XMaterial newType, boolean applyPhysics) {
        states.put(block, block.getState());
        XBlock.setType(block, newType, applyPhysics);
    }

    public void setToRestore(Block block, Material newType, boolean applyPhysics) {
        states.put(block, block.getState());
        block.setType(newType, applyPhysics);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (states.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeAll(states.keySet());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeAll(states.keySet());
    }

    public void rollback() {
        ListIterator<BlockState> iter = new ArrayList<>(states.values()).listIterator(states.size());
        while (iter.hasPrevious()) {
            iter.previous().update(true);
        }
        states.clear();
    }

    public void cleanup() {
        rollback();
        HandlerList.unregisterAll(this);
        INSTANCES.remove(this);
    }
}
