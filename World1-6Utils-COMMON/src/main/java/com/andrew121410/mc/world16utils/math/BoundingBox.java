package com.andrew121410.mc.world16utils.math;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("BoundingBox")
public class BoundingBox implements ConfigurationSerializable {

    private Location locationOne;
    private Location locationTwo;

    public BoundingBox(Location locationOne, Location locationTwo) {
        this.locationOne = locationOne.clone();
        this.locationTwo = locationTwo.clone();
    }

    public BoundingBox clone() {
        return new BoundingBox(this.locationOne, this.locationTwo);
    }

    public void expand(int x, int y, int z) {
        this.locationOne.setX(this.locationOne.getX() - x);
        this.locationOne.setY(this.locationOne.getY() - y);
        this.locationOne.setZ(this.locationOne.getZ() - z);
        this.locationTwo.setX(this.locationTwo.getX() + x);
        this.locationTwo.setY(this.locationTwo.getY() + y);
        this.locationTwo.setZ(this.locationTwo.getZ() + z);
    }

    public Location getLocationOne() {
        return locationOne;
    }

    public Location getLocationTwo() {
        return locationTwo;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("LocationOne", this.locationOne);
        map.put("LocationTwo", this.locationTwo);
        return map;
    }

    public static BoundingBox deserialize(Map<String, Object> map) {
        return new BoundingBox((Location) map.get("LocationOne"), (Location) map.get("LocationTwo"));
    }
}
