package com.andrew121410.mc.world16utils.config.serializers;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SoundSerializer implements TypeSerializer<Sound> {

    @Override
    public Sound deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }
        final String sound = SerializerUtils.nonVirtualNode(node, "sound").getString();
        return Registry.SOUNDS.get(NamespacedKey.minecraft(sound));
    }

    @Override
    public void serialize(Type type, @Nullable Sound obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.node("sound").set(obj.getKey().getKey());
    }
}
