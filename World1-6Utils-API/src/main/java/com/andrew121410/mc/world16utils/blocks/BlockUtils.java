package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Block;

public interface BlockUtils {
    boolean isStairs(Block block);

    boolean isDoor(Block block);

    Block getDoorBaseBlock(Block block);

    Block ifDoorThenGetBlockUnderTheDoorIfNotThanReturn(Block block);

    boolean isOpenable(Block block);

    boolean doOpenable(Block block, boolean open);
}
