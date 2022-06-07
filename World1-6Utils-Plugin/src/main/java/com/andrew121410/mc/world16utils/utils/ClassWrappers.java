package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_18_R2;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_19_R1;
import com.andrew121410.mc.world16utils.packet.IPackets;
import com.andrew121410.mc.world16utils.packet.Packet_V1_18_R2;
import com.andrew121410.mc.world16utils.packet.Packet_V1_19_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_18_R2;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_19_R1;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_723;
import org.bukkit.Bukkit;

public class ClassWrappers {

    private final BlockUtils blockUtils;
    private final SmoothTeleport smoothTeleport;
    private final IPackets packets;

    //Extra
    private final WorldEdit worldEdit;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_18_R2" -> {
                this.blockUtils = new BlockUtils_V1_18_R2();
                this.smoothTeleport = new SmoothTeleport_V1_18_R2();
                this.packets = new Packet_V1_18_R2();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_723() : null;
            }
            case "v1_19_R1" -> {
                this.blockUtils = new BlockUtils_V1_19_R1();
                this.smoothTeleport = new SmoothTeleport_V1_19_R1();
                this.packets = new Packet_V1_19_R1();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_723() : null;
            }
            default ->
                    throw new IllegalArgumentException("Unable to detect NMS version(" + version + ") for ClassWrappers");
        }
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public SmoothTeleport getSmoothTeleport() {
        return smoothTeleport;
    }

    public IPackets getPackets() {
        return packets;
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
