package net.Zrips.CMILib.Container;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class CMILocation extends Location {

    public CMILocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }

    public CMILocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public CMILocation(Location loc) {
        super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public CMILocation(String world, double x, double y, double z, float yaw, float pitch) {
        super(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public CMILocation(String world, double x, double y, double z) {
        super(Bukkit.getWorld(world), x, y, z);
    }
}
