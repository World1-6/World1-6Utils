package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Stairs;

public class BlockUtils_V1_16_R2 implements BlockUtils {
    @Override
    public boolean isStairs(Block block) {
        return block.getBlockData() instanceof Stairs;
    }
}
