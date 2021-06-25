package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Block;

public interface BlockUtils {
    boolean isStairs(Block block);

    boolean isDoor(Block block);

    Block getDoorBaseBlock(Block block);

    default Block ifDoorThenGetBlockUnderTheDoorIfNotThanReturn(Block block) {
        Block doorBaseBlock = getDoorBaseBlock(block);
        if (doorBaseBlock != null) return doorBaseBlock.getRelative(0, -1, 0);
        return block;
    }

    boolean isOpenable(Block block);

    boolean doOpenable(Block block, boolean open);
}
