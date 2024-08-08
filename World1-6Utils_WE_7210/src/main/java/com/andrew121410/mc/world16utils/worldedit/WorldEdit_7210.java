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

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        int minX = getCoordinate(min, "x");
        int minY = getCoordinate(min, "y");
        int minZ = getCoordinate(min, "z");
        int maxX = getCoordinate(max, "x");
        int maxY = getCoordinate(max, "y");
        int maxZ = getCoordinate(max, "z");

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private int getCoordinate(BlockVector3 vector, String coordinate) {
        try {
            if (this.useOldMethods) {
                switch (coordinate) {
                    case "x":
                        return (int) vector.getClass().getMethod("getX").invoke(vector);
                    case "y":
                        return (int) vector.getClass().getMethod("getY").invoke(vector);
                    case "z":
                        return (int) vector.getClass().getMethod("getZ").invoke(vector);
                }
            } else {
                switch (coordinate) {
                    case "x":
                        return vector.x();
                    case "y":
                        return vector.y();
                    case "z":
                        return vector.z();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean canUseOldMethods() {
        try {
            Class<?> blockVector3Class = Class.forName("com.sk89q.worldedit.math.BlockVector3");

            // Check if getX, getY, getZ methods exist
            blockVector3Class.getMethod("getX");
            blockVector3Class.getMethod("getY");
            blockVector3Class.getMethod("getZ");

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
        return true;
    }
}
