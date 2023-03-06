package com.andrew121410.mc.world16utils.config.serializers;

import org.bukkit.util.BoundingBox;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class BoundingBoxSerializer implements TypeSerializer<BoundingBox> {

    @Override
    public BoundingBox deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }
        final double minX = SerializerUtils.nonVirtualNode(node, "minX").getDouble();
        final double minY = SerializerUtils.nonVirtualNode(node, "minY").getDouble();
        final double minZ = SerializerUtils.nonVirtualNode(node, "minZ").getDouble();
        final double maxX = SerializerUtils.nonVirtualNode(node, "maxX").getDouble();
        final double maxY = SerializerUtils.nonVirtualNode(node, "maxY").getDouble();
        final double maxZ = SerializerUtils.nonVirtualNode(node, "maxZ").getDouble();
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void serialize(Type type, @Nullable BoundingBox obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.node("minX").set(obj.getMinX());
        node.node("minY").set(obj.getMinY());
        node.node("minZ").set(obj.getMinZ());
        node.node("maxX").set(obj.getMaxX());
        node.node("maxY").set(obj.getMaxY());
        node.node("maxZ").set(obj.getMaxZ());
    }
}
