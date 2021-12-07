package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_18_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_18_R1;
import com.andrew121410.mc.world16utils.entity.EntityUtils;
import com.andrew121410.mc.world16utils.entity.EntityUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.entity.EntityUtils_V1_18_R1;
import com.andrew121410.mc.world16utils.packet.IPackets;
import com.andrew121410.mc.world16utils.packet.Packet_V1_17_R1;
import com.andrew121410.mc.world16utils.packet.Packet_V1_18_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_17_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_18_R1;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_723;
import org.bukkit.Bukkit;

public class ClassWrappers {

    private final BlockUtils blockUtils;
    private final SmoothTeleport smoothTeleport;
    private final EnchantmentUtils enchantmentUtils;
    private final EntityUtils entityUtils;
    private final IPackets packets;

    //Extra
    private final WorldEdit worldEdit;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_17_R1" -> {
                this.blockUtils = new BlockUtils_V1_17_R1();
                this.smoothTeleport = new SmoothTeleport_V1_17_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_17_R1();
                this.entityUtils = new EntityUtils_V1_17_R1();
                this.packets = new Packet_V1_17_R1();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_723() : null;
            }
            case "v1_18_R1" -> {
                this.blockUtils = new BlockUtils_V1_18_R1();
                this.smoothTeleport = new SmoothTeleport_V1_18_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_18_R1();
                this.entityUtils = new EntityUtils_V1_18_R1();
                this.packets = new Packet_V1_18_R1();
                //Extra
                this.worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null ? new WorldEdit_723() : null;
            }
            default -> throw new IllegalArgumentException("Unable to detect NMS version(" + version + ") for ClassWrappers");
        }
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public SmoothTeleport getSmoothTeleport() {
        return smoothTeleport;
    }

    public EnchantmentUtils getEnchantmentUtils() {
        return enchantmentUtils;
    }

    public EntityUtils getEntityUtils() {
        return entityUtils;
    }

    public IPackets getPackets() {
        return packets;
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
