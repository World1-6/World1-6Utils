package com.andrew121410.mc.world16utils.entity;

import com.andrew121410.mc.world16utils.math.BoundingBox;
import net.minecraft.server.v1_16_R3.AxisAlignedBB;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityUtils_V1_16_R3 implements EntityUtils {

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        WorldServer world = ((CraftWorld) boundingBox.getLocationOne().getWorld()).getHandle();
        AxisAlignedBB bb = new AxisAlignedBB(boundingBox.getLocationOne().getX(), boundingBox.getLocationOne().getY(), boundingBox.getLocationOne().getZ(), boundingBox.getLocationTwo().getX(), boundingBox.getLocationTwo().getY(), boundingBox.getLocationTwo().getZ());
        List<net.minecraft.server.v1_16_R3.Entity> entityList = world.getEntities(null, bb, null);
        List<Entity> bukkitEntityList = new ArrayList<>(entityList.size());
        for (net.minecraft.server.v1_16_R3.Entity entity : entityList) {
            Entity bukkitEntity = entity.getBukkitEntity();
            bukkitEntityList.add(bukkitEntity);
        }
        return bukkitEntityList;
    }
}
