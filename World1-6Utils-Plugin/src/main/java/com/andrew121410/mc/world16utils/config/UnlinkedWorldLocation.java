package com.andrew121410.mc.world16utils.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A normal Location when deserialized will not work if the world is not loaded.
 * <p>
 * Before you use the Location you must check if the world is loaded with isWorldLoaded()
 */

@SerializableAs("UnlinkedWorldLocation")
public class UnlinkedWorldLocation extends Location implements ConfigurationSerializable {

    private UUID worldUUID;
    private String worldName;

    public UnlinkedWorldLocation(UUID worldUUID, String worldName, double x, double y, double z, float yaw, float pitch) {
        super(null, x, y, z, yaw, pitch); // World is null, because it might not be loaded.
        this.worldUUID = worldUUID;
        this.worldName = worldName;
    }

    public UnlinkedWorldLocation(UUID worldUUID, double x, double y, double z, float yaw, float pitch) {
        this(worldUUID, null, x, y, z, yaw, pitch);
    }

    public UnlinkedWorldLocation(UUID worldUUID, double x, double y, double z) {
        this(worldUUID, null, x, y, z, 0, 0);
    }

    public UnlinkedWorldLocation(UUID worldUUID, String worldName, double x, double y, double z) {
        this(worldUUID, worldName, x, y, z, 0, 0);
    }

    public UnlinkedWorldLocation(Location location) {
        this(location.getWorld().getUID(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public boolean isWorldLoaded() {
        return org.bukkit.Bukkit.getWorld(this.worldUUID) != null;
    }

    @Override
    public World getWorld() {
        return org.bukkit.Bukkit.getWorld(this.worldUUID);
    }

    public UUID getWorldUUID() {
        return this.worldUUID;
    }

    // This is so dumb.
    public void ensure() {
        if (this.worldUUID == null && this.worldName != null) {
            World world = org.bukkit.Bukkit.getWorld(this.worldName);
            if (world != null) {
                this.worldUUID = world.getUID();
            }
        } else if (this.worldUUID != null && this.worldName == null) {
            World world = org.bukkit.Bukkit.getWorld(this.worldUUID);
            if (world != null) {
                this.worldName = world.getName();
            }
        } else if (this.worldUUID != null && this.worldName != null) {
            // World name could be different now?
            World world = org.bukkit.Bukkit.getWorld(this.worldUUID);
            if (world != null && !world.getName().equals(this.worldName)) {
                this.worldName = world.getName();
            }
        } else {
            throw new IllegalArgumentException("WorldUUID and WorldName are null.");
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("worldUUID", String.valueOf(this.worldUUID));
        map.put("worldName", this.worldName);
        map.put("x", this.getX());
        map.put("y", this.getY());
        map.put("z", this.getZ());
        map.put("yaw", this.getYaw());
        map.put("pitch", this.getPitch());
        return map;
    }

    public static UnlinkedWorldLocation deserialize(Map<String, Object> map) {
        String worldUUIDString = (String) map.get("worldUUID");
        if (worldUUIDString == null) {
            // Old format could be an uuid or could be a world name.
            worldUUIDString = (String) map.get("world");
        }

        // Could be null.
        String worldString = (String) map.get("worldName");

        // Old format
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(worldUUIDString);
        } catch (Exception e) {
            // What was provided was "probably" a world name, instead of a UUID.
            World world = org.bukkit.Bukkit.getWorld(worldUUIDString);
            if (world == null) {
                throw new IllegalArgumentException("Invalid world: " + worldUUIDString);
            }

            worldUUID = world.getUID();
            worldString = world.getName();
        }

        double x = (Double) map.getOrDefault("x", 0);
        double y = (Double) map.getOrDefault("y", 0);
        double z = (Double) map.getOrDefault("z", 0);

        // java.lang.Double cannot be cast to class java.lang.Float
        double fakeYaw = (Double) map.getOrDefault("yaw", 0);
        double fakePitch = (Double) map.getOrDefault("pitch", 0);

        return new UnlinkedWorldLocation(worldUUID, worldString, x, y, z, (float) fakeYaw, (float) fakePitch);
    }
}
