package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
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
    public Block getDoorBaseBlock(Block block) {
        if (!isDoor(block)) return null;
        Door door = (Door) block.getBlockData();
        if (door.getHalf() == Bisected.Half.TOP) return block.getRelative(BlockFace.DOWN);
        return block;
    }

    @Override
    public Block ifDoorThenGetBlockUnderTheDoorIfNotThanReturn(Block block) {
        Block doorBaseBlock = getDoorBaseBlock(block);
        if (doorBaseBlock != null) return doorBaseBlock.getRelative(0, -1, 0);
        return block;
    }

    @Override
    public boolean isOpenable(Block block) {
        return block.getBlockData() instanceof Openable;
    }

    @Override
    public boolean doOpenable(Block block, boolean value) {
        if (!isOpenable(block)) return false;
        Openable openable = (Openable) block.getBlockData();
        openable.setOpen(value);
        block.setBlockData(openable);
        return true;
    }
}
