package com.andrew121410.mc.world16utils.entity;

import com.andrew121410.mc.world16utils.math.BoundingBox;
import org.bukkit.entity.Entity;

import java.util.Collection;

public interface EntityUtils {

    Collection<Entity> getNearbyEntities(BoundingBox boundingBox);
}
