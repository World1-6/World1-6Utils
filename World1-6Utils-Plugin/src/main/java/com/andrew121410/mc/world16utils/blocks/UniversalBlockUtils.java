package com.andrew121410.mc.world16utils.blocks;

import com.cryptomorin.xseries.XMaterial;

public class UniversalBlockUtils {

    public static boolean isFarmLand(XMaterial xMaterial) {
        return xMaterial.parseMaterial() == XMaterial.FARMLAND.parseMaterial();
    }
}
