package com.andrew121410.mc.world16utils.entity;

import com.andrew121410.mc.world16utils.utils.SimpleBoundingBox;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityUtils_V1_12_R1 implements EntityUtils {

    @Override
    public Collection<Entity> getNearbyEntities(World world, SimpleBoundingBox simpleBoundingBox) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        AxisAlignedBB bb = new AxisAlignedBB(simpleBoundingBox.getMinX(), simpleBoundingBox.getMinY(), simpleBoundingBox.getMinZ(), simpleBoundingBox.getMaxX(), simpleBoundingBox.getMaxY(), simpleBoundingBox.getMaxZ());
        List<net.minecraft.server.v1_12_R1.Entity> entityList = worldServer.getEntities(null, bb, null);
        List<Entity> bukkitEntityList = new ArrayList<>(entityList.size());
        for (net.minecraft.server.v1_12_R1.Entity entity : entityList) {
            Entity bukkitEntity = entity.getBukkitEntity();
            bukkitEntityList.add(bukkitEntity);
        }
        return bukkitEntityList;
    }
}
