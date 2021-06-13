package com.andrew121410.mc.world16utils.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public class BukkitUtils {

    /** Provides two locations; the first is the min and the second is the max
     * @param world The world for the two locations
     * @param boundingBox The boundingbox is needed to make the two locations
     */

    public static Location[] getTwoPointsLocations(World world, BoundingBox boundingBox) {
        return new Location[]{new Location(world, boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ()), new Location(world, boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ())};
    }
}
