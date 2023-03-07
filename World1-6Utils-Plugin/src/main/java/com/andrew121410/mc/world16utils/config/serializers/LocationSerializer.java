package com.andrew121410.mc.world16utils.config.serializers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class LocationSerializer implements TypeSerializer<Location> {

    public LocationSerializer() {
    }

    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }
        final String worldName = SerializerUtils.nonVirtualNode(node, "world").getString();
        final double x = SerializerUtils.nonVirtualNode(node, "x").getDouble();
        final double y = SerializerUtils.nonVirtualNode(node, "y").getDouble();
        final double z = SerializerUtils.nonVirtualNode(node, "z").getDouble();
        final float yaw = (float) SerializerUtils.nonVirtualNode(node, "yaw").getDouble();
        final float pitch = (float) SerializerUtils.nonVirtualNode(node, "pitch").getDouble();
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable Location location, ConfigurationNode node) throws SerializationException {
        if (location == null) {
            node.raw(null);
            return;
        }

        if (location.getWorld() != null) {
            node.node("world").set(location.getWorld().getName());
        }
        node.node("x").set(location.getX());
        node.node("y").set(location.getY());
        node.node("z").set(location.getZ());
        node.node("yaw").set(location.getYaw());
        node.node("pitch").set(location.getPitch());
    }
}
