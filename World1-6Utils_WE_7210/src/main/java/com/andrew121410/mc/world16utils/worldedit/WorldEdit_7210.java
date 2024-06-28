package com.andrew121410.mc.world16utils.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class WorldEdit_7210 implements WorldEdit {

    private WorldEditPlugin worldEditPlugin;
    private boolean useOldMethods = true;

    public WorldEdit_7210() {
        this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        this.useOldMethods = canUseOldMethods();
    }

    @Override
    public boolean moveCuboidRegion(Location one, Location two, Location where, int howMany) {
        World world = BukkitAdapter.adapt(one.getWorld());
        BlockVector3 blockVector31 = BukkitAdapter.asBlockVector(one);
        BlockVector3 blockVector32 = BukkitAdapter.asBlockVector(two);
        CuboidRegion cuboidRegion = new CuboidRegion(blockVector31, blockVector32);
        try (EditSession editSession = worldEditPlugin.getWorldEdit().newEditSession(world)) {
            editSession.moveCuboidRegion(cuboidRegion, BukkitAdapter.asBlockVector(where), howMany, false, null);
        } catch (MaxChangedBlocksException e) {
            return false;
        }
        return true;
    }

    @Override
    public BoundingBox getRegion(Player player) {
        Region region;
        try {
            region = this.worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (IncompleteRegionException e) {
            return null;
        }

        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;
        if (this.useOldMethods) {
            // If the old methods still exist, use them
            minX = region.getMinimumPoint().getX();
            minY = region.getMinimumPoint().getY();
            minZ = region.getMinimumPoint().getZ();
            maxX = region.getMaximumPoint().getX();
            maxY = region.getMaximumPoint().getY();
            maxZ = region.getMaximumPoint().getZ();
        } else {
            // If the old methods do not exist, use the new methods
            minX = region.getMinimumPoint().x();
            minY = region.getMinimumPoint().y();
            minZ = region.getMinimumPoint().z();
            maxX = region.getMaximumPoint().x();
            maxY = region.getMaximumPoint().y();
            maxZ = region.getMaximumPoint().z();
        }

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private boolean canUseOldMethods() {
        try {
            Class<?> blockVector3Class = Class.forName("com.sk89q.worldedit.math.BlockVector3");

            // Check if getX method exists
            try {
                blockVector3Class.getMethod("getX");
            } catch (NoSuchMethodException e) {
                System.out.println("getX() method does not exist");
                return false;
            }

            // Check if getY method exists
            try {
                blockVector3Class.getMethod("getY");
            } catch (NoSuchMethodException e) {
                System.out.println("getY() method does not exist");
                return false;
            }

            // Check if getZ method exists
            try {
                blockVector3Class.getMethod("getZ");
            } catch (NoSuchMethodException e) {
                System.out.println("getZ() method does not exist");
                return false;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("BlockVector3 class not found");
            return false;
        }
        return true;
    }
}
