package com.andrew121410.mc.world16utils.config.serializers;

import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class UnlinkedWorldLocationSerializer implements TypeSerializer<UnlinkedWorldLocation> {

    @Override
    public UnlinkedWorldLocation deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }
        String worldName = SerializerUtils.nonVirtualNode(node, "world").getString();
        double x = SerializerUtils.nonVirtualNode(node, "x").getDouble();
        double y = SerializerUtils.nonVirtualNode(node, "y").getDouble();
        double z = SerializerUtils.nonVirtualNode(node, "z").getDouble();
        float yaw = (float) SerializerUtils.nonVirtualNode(node, "yaw").getDouble();
        float pitch = (float) SerializerUtils.nonVirtualNode(node, "pitch").getDouble();
        return new UnlinkedWorldLocation(worldName, x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable UnlinkedWorldLocation obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.node("world").set(obj.getWorldName());
        node.node("x").set(obj.getX());
        node.node("y").set(obj.getY());
        node.node("z").set(obj.getZ());
        node.node("yaw").set(obj.getYaw());
        node.node("pitch").set(obj.getPitch());
    }
}
