package com.andrew121410.mc.world16utils.enchantment;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtils_V1_12_R1 implements EnchantmentUtils {
    @Override
    public Enchantment getByName(String name) {
        return Enchantment.getByName(name);
    }
}
