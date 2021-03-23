package com.andrew121410.mc.world16utils.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WorldEdit_614 implements WorldEdit {

    private WorldEditPlugin worldEditPlugin;

    public WorldEdit_614() {
        this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    @Override
    public boolean moveCuboidRegion(Location one, Location two, Location where, int howMany) {
        World world = new BukkitWorld(one.getWorld());
        Vector vector1 = BukkitUtil.toVector(one);
        Vector vector2 = BukkitUtil.toVector(two);
        CuboidRegion cuboidRegion = new CuboidRegion(vector1, vector2);
        EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(world, -1);
        Vector vectorDIR = BukkitUtil.toVector(where);
        try {
            editSession.moveRegion(cuboidRegion, vectorDIR, howMany, false, null);
        } catch (MaxChangedBlocksException e) {
            return false;
        }
        return true;
    }
}
