package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_7210;

public class ClassWrappers {

    private WorldEdit worldEdit = null;

    public ClassWrappers(World16Utils plugin) {
//        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//
//        if (version.equals("v1_19_R2") || version.equals("v1_19_R3")) {//Extra
//            this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_7210() : null;
//        }

        this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_7210() : null;
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
