package com.andrew121410.mc.world16utils.worldedit;

import com.andrew121410.mc.world16utils.utils.SimpleBoundingBox;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldEdit_723 implements WorldEdit {

    private WorldEditPlugin worldEditPlugin;

    public WorldEdit_723() {
        this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    @Override
    public boolean moveCuboidRegion(Location one, Location two, Location where, int howMany) {
        World world = BukkitAdapter.adapt(one.getWorld());
        BlockVector3 blockVector31 = BukkitAdapter.asBlockVector(one);
        BlockVector3 blockVector32 = BukkitAdapter.asBlockVector(two);
        CuboidRegion cuboidRegion = new CuboidRegion(blockVector31, blockVector32);
        try (EditSession editSession = worldEditPlugin.getWorldEdit().newEditSession(world)) {
            editSession.moveCuboidRegion(cuboidRegion, BukkitAdapter.asBlockVector(where), howMany, false, null);
        } catch (MaxChangedBlocksException e) {
            return false;
        }
        return true;
    }

    @Override
    public SimpleBoundingBox getRegion(Player player) {
        Region region;
        try {
            region = this.worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (IncompleteRegionException e) {
            return null;
        }
        return new SimpleBoundingBox(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ(), region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
    }
}
