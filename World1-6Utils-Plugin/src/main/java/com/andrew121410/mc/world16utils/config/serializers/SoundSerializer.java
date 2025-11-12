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
        final String sound = node.getString();
        if (sound == null) {
            return null;
        }

        NamespacedKey key = NamespacedKey.fromString(sound);
        if (key == null) {
            throw new SerializationException("Invalid sound key: " + sound);
        }
        return Registry.SOUND_EVENT.get(key);
    }

    @Override
    public void serialize(Type type, @Nullable Sound obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        NamespacedKey key = Registry.SOUND_EVENT.getKey(obj);
        if (key == null) {
            throw new SerializationException("Could not find key for sound: " + obj);
        }

        node.set(key.toString());
    }
}
