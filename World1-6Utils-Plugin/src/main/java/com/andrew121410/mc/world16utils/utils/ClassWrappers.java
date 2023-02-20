package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_7210;
import org.bukkit.Bukkit;

public class ClassWrappers {

    //Extra
    private final WorldEdit worldEdit;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_19_R2" -> {
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_7210() : null;
            }
            default ->
                    throw new IllegalArgumentException("Unable to detect NMS version(" + version + ") for ClassWrappers");
        }
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
