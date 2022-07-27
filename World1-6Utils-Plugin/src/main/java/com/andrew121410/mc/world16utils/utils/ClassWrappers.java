package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.packet.IPackets;
import com.andrew121410.mc.world16utils.packet.Packet_V1_18_R2;
import com.andrew121410.mc.world16utils.packet.Packet_V1_19_R1;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_7210;
import org.bukkit.Bukkit;

public class ClassWrappers {

    private final IPackets packets;

    //Extra
    private final WorldEdit worldEdit;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_18_R2" -> {
                this.packets = new Packet_V1_18_R2();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_7210() : null;
            }
            case "v1_19_R1" -> {
                this.packets = new Packet_V1_19_R1();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_7210() : null;
            }
            default ->
                    throw new IllegalArgumentException("Unable to detect NMS version(" + version + ") for ClassWrappers");
        }
    }

    public IPackets getPackets() {
        return packets;
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
