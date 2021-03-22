package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Stairs;

public class BlockUtils_V1_16_R3 implements BlockUtils {
    @Override
    public boolean isStairs(Block block) {
        return block.getBlockData() instanceof Stairs;
    }

    @Override
    public boolean isDoor(Block block) {
        return Tag.DOORS.isTagged(block.getType()); //or Material.getClass().isAssignableFrom(Door.class)
    }

    @Override
    public boolean isOpenable(Block block) {
        return block.getBlockData() instanceof Openable;
    }

    @Override
    public boolean doOpenable(Block block, boolean value) {
        Openable openable = (Openable) block.getBlockData();
        openable.setOpen(value);
        block.setBlockData(openable);
        return false;
    }
}
