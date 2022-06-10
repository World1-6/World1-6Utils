package com.andrew121410.mc.world16utils.blocks;

import com.andrew121410.mc.world16utils.utils.Utils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
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

import java.util.ArrayList;
import java.util.List;

public class UniversalBlockUtils {

    public static boolean isStairs(Block block) {
        return block.getBlockData() instanceof Stairs;
    }

    public static boolean isDoor(Block block) {
        return Tag.DOORS.isTagged(block.getType()); //or Material.getClass().isAssignableFrom(Door.class)
    }

    public static Block ifDoorThenGetBlockUnderTheDoorIfNotThanReturn(Block block) {
        Block doorBaseBlock = getDoorBaseBlock(block);
        if (doorBaseBlock != null) return doorBaseBlock.getRelative(0, -1, 0);
        return block;
    }

    public static Block getDoorBaseBlock(Block block) {
        if (!isDoor(block)) return null;
        Door door = (Door) block.getBlockData();
        if (door.getHalf() == Bisected.Half.TOP) return block.getRelative(BlockFace.DOWN);
        return block;
    }

    public static boolean isOpenable(Block block) {
        return block.getBlockData() instanceof Openable;
    }

    public static boolean doOpenable(Block block, boolean value) {
        if (!isOpenable(block)) return false;
        Openable openable = (Openable) block.getBlockData();
        openable.setOpen(value);
        block.setBlockData(openable);
        return true;
    }

    public static Sign isSign(Block block) {
        BlockState blockState = block.getState();
        if (blockState instanceof Sign) {
            return (Sign) blockState;
        }
        return null;
    }

    public static String signCenterText(String text) {
        return Utils.centerText(text, 16);
    }

    public static boolean isFarmLand(XMaterial xMaterial) {
        return xMaterial.parseMaterial() == XMaterial.FARMLAND.parseMaterial();
    }

    public static List<Block> getNearbyBlocks(Location location, int radius, boolean doY) {
        List<Block> blocks = new ArrayList<>();

        if (doY) {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                    for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        } else {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
                }
            }
        }

        return blocks;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        return getNearbyBlocks(location, radius, true);
    }

    public static void editSign(Player player, Sign sign) {
        //https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/27a27cdb840880f785b654bbcacb0b4c7c77fad9
        player.openSign(sign);
    }
}
