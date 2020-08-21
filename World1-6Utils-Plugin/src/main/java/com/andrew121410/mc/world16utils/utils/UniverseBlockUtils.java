package com.andrew121410.mc.world16utils.utils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

public class UniverseBlockUtils {

    public boolean isFarmLand(XMaterial xMaterial) {
        return xMaterial.parseMaterial() == Material.FARMLAND;
    }
}
