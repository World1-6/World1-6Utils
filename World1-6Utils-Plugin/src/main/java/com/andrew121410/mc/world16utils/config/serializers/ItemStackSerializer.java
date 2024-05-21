package com.andrew121410.mc.world16utils.config.serializers;

import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ItemStackSerializer implements TypeSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }

        String base64 = node.getString();

        ItemStack itemStack = null;
        try {
            itemStack = BukkitSerialization.deserializeItemStack(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemStack;
    }

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.set(BukkitSerialization.serializeItemStack(obj));
    }
}
