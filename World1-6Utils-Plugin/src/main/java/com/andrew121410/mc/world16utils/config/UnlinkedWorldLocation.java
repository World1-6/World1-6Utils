package com.andrew121410.mc.world16utils.config;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This should be used for when you want to load up a location with Bukkit's deserialize and the world might not be loaded.
 * If you use a normal Location it will throw an error if the world is not loaded. which is not good
 */
public class UnlinkedWorldLocation implements ConfigurationSerializable {

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public UnlinkedWorldLocation(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public UnlinkedWorldLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public UnlinkedWorldLocation(Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public Location toLocation() {
        return new Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("world", world);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("pitch", pitch);
        map.put("yaw", yaw);
        return map;
    }

    public static UnlinkedWorldLocation deserialize(Map<String, Object> map) {
        return new UnlinkedWorldLocation((String) map.get("world"), (Double) map.get("x"), (Double) map.get("y"), (Double) map.get("z"), (Float) map.get("pitch"), (Float) map.get("yaw"));
    }
}
