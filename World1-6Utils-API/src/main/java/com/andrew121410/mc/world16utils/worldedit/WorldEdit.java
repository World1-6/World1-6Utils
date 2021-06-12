package com.andrew121410.mc.world16utils.worldedit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public interface WorldEdit {
    boolean moveCuboidRegion(Location one, Location two, Location where, int howMany);

    BoundingBox getRegion(Player player);
}
