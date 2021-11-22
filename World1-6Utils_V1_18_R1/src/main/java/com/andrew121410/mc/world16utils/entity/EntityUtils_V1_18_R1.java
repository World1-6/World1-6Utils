package com.andrew121410.mc.world16utils.entity;

import com.google.common.base.Predicates;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityUtils_V1_18_R1 implements EntityUtils {

    @Override
    public Collection<Entity> getNearbyEntities(World world, BoundingBox boundingBox) {
        ServerLevel serverLevel = ((CraftWorld) world).getHandle();
        AABB bb = new AABB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
        List<net.minecraft.world.entity.Entity> entityList = serverLevel.getEntities((net.minecraft.world.entity.Entity) null, bb, Predicates.alwaysTrue());
        List<Entity> bukkitEntityList = new ArrayList<>(entityList.size());
        for (net.minecraft.world.entity.Entity entity : entityList) {
            Entity bukkitEntity = entity.getBukkitEntity();
            bukkitEntityList.add(bukkitEntity);
        }
        return bukkitEntityList;
    }
}
