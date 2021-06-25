package com.andrew121410.mc.world16utils.blocks;

import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

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

    Sign isSign(Block block);

    void edit(Player player, Sign sign);

    static String signCenterText(String text) {
        return Utils.centerText(text, 16);
    }
}
