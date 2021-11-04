package com.andrew121410.mc.world16utils.blocks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class UniversalBlockUtils {

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
}
