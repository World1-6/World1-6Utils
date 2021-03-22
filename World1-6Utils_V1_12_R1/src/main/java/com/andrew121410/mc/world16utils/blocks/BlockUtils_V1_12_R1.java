package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Block;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.Stairs;

public class BlockUtils_V1_12_R1 implements BlockUtils {

    @Override
    public boolean isStairs(Block block) {
        return block.getState().getData() instanceof Stairs;
    }

    @Override
    public boolean isDoor(Block block) {
        return block.getState().getData().getClass().isAssignableFrom(Door.class);
    }

    @Override
    public boolean isOpenable(Block block) {
        return block.getState().getData() instanceof Openable;
    }

    @Override
    public boolean doOpenable(Block block, boolean value) {
        if (!isOpenable(block)) return false;
        Openable openable = (Openable) block.getState().getData();
        openable.setOpen(value);
        block.getState().setData((MaterialData) openable);
        block.getState().update();
        return true;
    }
}
