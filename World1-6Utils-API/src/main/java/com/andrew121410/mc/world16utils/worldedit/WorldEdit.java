package com.andrew121410.mc.world16utils.worldedit;

import com.andrew121410.mc.world16utils.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public interface WorldEdit {
    boolean moveCuboidRegion(Location one, Location two, Location where, int howMany);

    default boolean moveCuboidRegion(World world, BoundingBox boundingBox, Location where, int howMany) {
        Location[] locations = BukkitUtils.getTwoPointsLocations(world, boundingBox);
        return this.moveCuboidRegion(locations[0], locations[1], where, howMany);
    }

    BoundingBox getRegion(Player player);
}
