package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_16_R3;
import com.andrew121410.mc.world16utils.player.SmoothTeleport;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_12_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_16_R3;
import com.andrew121410.mc.world16utils.sign.SignUtils;
import com.andrew121410.mc.world16utils.sign.SignUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.sign.SignUtils_V1_16_R3;
import org.bukkit.Bukkit;

public class ClassWrappers {

    public BlockUtils blockUtils;
    private SignUtils signUtils;
    private SmoothTeleport smoothTeleport;
    private EnchantmentUtils enchantmentUtils;

    public ClassWrappers(World16Utils plugin) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_16_R3":
                this.blockUtils = new BlockUtils_V1_16_R3();
                this.signUtils = new SignUtils_V1_16_R3(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_16_R3();
                this.enchantmentUtils = new EnchantmentUtils_V1_16_R3();
                break;
            case "v1_12_R1":
                this.blockUtils = new BlockUtils_V1_12_R1();
                this.signUtils = new SignUtils_V1_12_R1(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_12_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_12_R1();
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
}