package com.andrew121410.mc.world16utils.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

import java.util.Collection;

public interface EntityUtils {

    Collection<Entity> getNearbyEntities(World world, BoundingBox boundingBox);
}
