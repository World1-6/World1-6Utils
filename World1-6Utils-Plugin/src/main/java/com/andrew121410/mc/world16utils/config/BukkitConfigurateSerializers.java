package com.andrew121410.mc.world16utils.config;

import com.andrew121410.mc.world16utils.config.serializers.BoundingBoxSerializer;
import com.andrew121410.mc.world16utils.config.serializers.ItemStackSerializer;
import com.andrew121410.mc.world16utils.config.serializers.LocationSerializer;
import com.andrew121410.mc.world16utils.config.serializers.UnlinkedWorldLocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public class BukkitConfigurateSerializers {

    public TypeSerializerCollection getBukkitSerializers() {
        TypeSerializerCollection.Builder serializers = TypeSerializerCollection.builder();

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
