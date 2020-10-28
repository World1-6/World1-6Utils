package com.andrew121410.mc.world16utils.utils;

import com.cryptomorin.xseries.XMaterial;

public class UniverseBlockUtils {

    public static boolean isFarmLand(XMaterial xMaterial) {
        return xMaterial.parseMaterial() == XMaterial.FARMLAND.parseMaterial();
    }
}
