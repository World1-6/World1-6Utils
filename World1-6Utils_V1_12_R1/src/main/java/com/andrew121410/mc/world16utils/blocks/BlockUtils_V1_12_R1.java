package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.block.Block;
import org.bukkit.material.Stairs;

public class BlockUtils_V1_12_R1 implements BlockUtils {

    @Override
    public boolean isStairs(Block block) {
        return block.getState().getData() instanceof Stairs;
    }
}
