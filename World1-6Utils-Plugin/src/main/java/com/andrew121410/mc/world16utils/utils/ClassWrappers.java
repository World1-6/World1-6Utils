package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.entity.EntityUtils;
import com.andrew121410.mc.world16utils.entity.EntityUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.entity.EntityUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_16_R3;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_17_R1;
import com.andrew121410.mc.world16utils.sign.SignUtils;
import com.andrew121410.mc.world16utils.sign.SignUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.sign.SignUtils_V1_17_R1;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit;
import com.andrew121410.mc.world16utils.worldedit.WorldEdit_723;
import org.bukkit.Bukkit;

public class ClassWrappers {

    private BlockUtils blockUtils;
    private SignUtils signUtils;
    private SmoothTeleport smoothTeleport;
    private EnchantmentUtils enchantmentUtils;
    private EntityUtils entityUtils;

    //Extra
    private WorldEdit worldEdit;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_16_R3":
                this.blockUtils = new BlockUtils_V1_16_R3();
                this.signUtils = new SignUtils_V1_16_R3(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_16_R3();
                this.enchantmentUtils = new EnchantmentUtils_V1_16_R3();
                this.entityUtils = new EntityUtils_V1_16_R3();
                //Extra
                this.worldEdit = new WorldEdit_723();
                break;
            case "v1_17_R1":
                this.blockUtils = new BlockUtils_V1_17_R1();
                this.signUtils = new SignUtils_V1_17_R1(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_17_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_17_R1();
                this.entityUtils = new EntityUtils_V1_17_R1();
                //Extra
                this.worldEdit = new WorldEdit_723();
                break;
        }
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public SignUtils getSignUtils() {
        return signUtils;
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

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
