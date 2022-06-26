package net.Zrips.CMILib.Container;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class CMILocation extends Location {

    private String worldName;

    public CMILocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.worldName = world.getName();
    }

    public CMILocation(World world, double x, double y, double z) {
        super(world, x, y, z);
        if (world != null)
            this.worldName = world.getName();
    }

    public CMILocation(Location loc) {
        super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        if (loc.getWorld() != null)
            this.worldName = loc.getWorld().getName();
    }

    public CMILocation(String world, double x, double y, double z, float yaw, float pitch) {
        super(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        this.worldName = world;
    }

    public CMILocation(String world, double x, double y, double z) {
        super(Bukkit.getWorld(world), x, y, z);
        this.worldName = world;
    }
}
