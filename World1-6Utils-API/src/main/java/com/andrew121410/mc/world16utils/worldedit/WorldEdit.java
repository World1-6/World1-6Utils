package com.andrew121410.mc.world16utils.worldedit;

import com.andrew121410.mc.world16utils.utils.SimpleBoundingBox;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface WorldEdit {
    boolean moveCuboidRegion(Location one, Location two, Location where, int howMany);

    default boolean moveCuboidRegion(World world, SimpleBoundingBox simpleBoundingBox, Location where, int howMany) {
        Location[] locations = simpleBoundingBox.to(world);
        return this.moveCuboidRegion(locations[0], locations[1], where, howMany);
    }

    SimpleBoundingBox getRegion(Player player);
}
