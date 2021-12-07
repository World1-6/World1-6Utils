package com.andrew121410.mc.world16utils.blocks;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;

public class BlockUtils_V1_18_R1 implements BlockUtils {
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

    @Override
    public Sign isSign(Block block) {
        BlockState blockState = block.getState();
        if (blockState instanceof Sign) {
            return (Sign) blockState;
        }
        return null;
    }

    @Override
    public void edit(Player player, Sign sign) {
        //https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/27a27cdb840880f785b654bbcacb0b4c7c77fad9
        player.openSign(sign);
    }
}
