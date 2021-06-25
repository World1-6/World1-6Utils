package com.andrew121410.mc.world16utils.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;


@SerializableAs("SimpleBoundingBox")
public class SimpleBoundingBox implements Cloneable, ConfigurationSerializable {

    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    public SimpleBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static SimpleBoundingBox from(BoundingBox boundingBox) {
        return new SimpleBoundingBox(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
    }

    public static SimpleBoundingBox from(Location one, Location two) {
        return new SimpleBoundingBox(one.getBlockX(), one.getBlockY(), one.getBlockZ(), two.getBlockX(), two.getBlockY(), two.getBlockZ());
    }

    public static SimpleBoundingBox from(Vector one, Vector two) {
        return new SimpleBoundingBox(one.getBlockX(), one.getBlockY(), one.getBlockZ(), two.getBlockX(), two.getBlockY(), two.getBlockZ());
    }

    public Location[] to(World world) {
        return new Location[]{new Location(world, getMinX(), getMinY(), getMinZ()), new Location(world, getMaxX(), getMaxY(), getMaxZ())};
    }

    public BoundingBox to() {
        return new BoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public SimpleBoundingBox shift(double shiftX, double shiftY, double shiftZ) {
        if (shiftX == 0.0D && shiftY == 0.0D && shiftZ == 0.0D) return this;
        return this.resize(this.minX + shiftX, this.minY + shiftY, this.minZ + shiftZ,
                this.maxX + shiftX, this.maxY + shiftY, this.maxZ + shiftZ);
    }

    public SimpleBoundingBox shift(Location shift) {
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }

    public SimpleBoundingBox shift(Vector shift) {
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }

    public SimpleBoundingBox resize(double x1, double y1, double z1, double x2, double y2, double z2) {
        NumberConversions.checkFinite(x1, "x1 not finite");
        NumberConversions.checkFinite(y1, "y1 not finite");
        NumberConversions.checkFinite(z1, "z1 not finite");
        NumberConversions.checkFinite(x2, "x2 not finite");
        NumberConversions.checkFinite(y2, "y2 not finite");
        NumberConversions.checkFinite(z2, "z2 not finite");

        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
        return this;
    }

    public SimpleBoundingBox expand(double negativeX, double negativeY, double negativeZ, double positiveX, double positiveY, double positiveZ) {
        if (negativeX == 0.0D && negativeY == 0.0D && negativeZ == 0.0D && positiveX == 0.0D && positiveY == 0.0D && positiveZ == 0.0D) {
            return this;
        }
        double newMinX = this.minX - negativeX;
        double newMinY = this.minY - negativeY;
        double newMinZ = this.minZ - negativeZ;
        double newMaxX = this.maxX + positiveX;
        double newMaxY = this.maxY + positiveY;
        double newMaxZ = this.maxZ + positiveZ;

        // limit shrinking:
        if (newMinX > newMaxX) {
            double centerX = this.getCenterX();
            if (newMaxX >= centerX) {
                newMinX = newMaxX;
            } else if (newMinX <= centerX) {
                newMaxX = newMinX;
            } else {
                newMinX = centerX;
                newMaxX = centerX;
            }
        }
        if (newMinY > newMaxY) {
            double centerY = this.getCenterY();
            if (newMaxY >= centerY) {
                newMinY = newMaxY;
            } else if (newMinY <= centerY) {
                newMaxY = newMinY;
            } else {
                newMinY = centerY;
                newMaxY = centerY;
            }
        }
        if (newMinZ > newMaxZ) {
            double centerZ = this.getCenterZ();
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ;
            } else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ;
            } else {
                newMinZ = centerZ;
                newMaxZ = centerZ;
            }
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public SimpleBoundingBox expand(double expansion) {
        return this.expand(expansion, expansion, expansion, expansion, expansion, expansion);
    }

    public double getCenterX() {
        return (this.minX + this.getWidthX() * 0.5D);
    }

    public double getWidthX() {
        return (this.maxX - this.minX);
    }

    public double getCenterY() {
        return (this.minY + this.getHeight() * 0.5D);
    }

    public double getHeight() {
        return (this.maxY - this.minY);
    }

    public double getCenterZ() {
        return (this.minZ + this.getWidthZ() * 0.5D);
    }

    public double getWidthZ() {
        return (this.maxZ - this.minZ);
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    @Override
    public SimpleBoundingBox clone() {
        try {
            return (SimpleBoundingBox) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("minX", minX);
        result.put("minY", minY);
        result.put("minZ", minZ);
        result.put("maxX", maxX);
        result.put("maxY", maxY);
        result.put("maxZ", maxZ);
        return result;
    }

    public static SimpleBoundingBox deserialize(Map<String, Object> args) {
        double minX = 0.0D;
        double minY = 0.0D;
        double minZ = 0.0D;
        double maxX = 0.0D;
        double maxY = 0.0D;
        double maxZ = 0.0D;

        if (args.containsKey("minX")) {
            minX = ((Number) args.get("minX")).doubleValue();
        }
        if (args.containsKey("minY")) {
            minY = ((Number) args.get("minY")).doubleValue();
        }
        if (args.containsKey("minZ")) {
            minZ = ((Number) args.get("minZ")).doubleValue();
        }
        if (args.containsKey("maxX")) {
            maxX = ((Number) args.get("maxX")).doubleValue();
        }
        if (args.containsKey("maxY")) {
            maxY = ((Number) args.get("maxY")).doubleValue();
        }
        if (args.containsKey("maxZ")) {
            maxZ = ((Number) args.get("maxZ")).doubleValue();
        }

        return new SimpleBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}