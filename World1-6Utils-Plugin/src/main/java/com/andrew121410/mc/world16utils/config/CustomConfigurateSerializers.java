package com.andrew121410.mc.world16utils.config;

import com.andrew121410.mc.world16utils.config.serializers.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public class CustomConfigurateSerializers {

    public TypeSerializerCollection getBukkitSerializers() {
        TypeSerializerCollection.Builder serializers = TypeSerializerCollection.builder();

        serializers.registerExact(Sound.class, new SoundSerializer());
        serializers.registerExact(Location.class, new LocationSerializer());
        serializers.registerExact(BoundingBox.class, new BoundingBoxSerializer());
        serializers.registerExact(ItemStack.class, new ItemStackSerializer());

        return serializers.build();
    }

    public TypeSerializerCollection getWorld16UtilsSerializers() {
        TypeSerializerCollection.Builder serializers = TypeSerializerCollection.builder();
        serializers.registerExact(UnlinkedWorldLocation.class, new UnlinkedWorldLocationSerializer());
        return serializers.build();
    }
}
