package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEditIntegration;

public class ClassWrappers {

    private WorldEdit worldEdit = null;

    public ClassWrappers(World16Utils plugin) {
        this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEditIntegration() : null;
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
