package com.andrew121410.mc.world16utils.worldedit;

import com.andrew121410.mc.world16utils.math.BoundingBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WorldEdit {
    boolean moveCuboidRegion(Location one, Location two, Location where, int howMany);

    BoundingBox getRegion(Player player);
}
