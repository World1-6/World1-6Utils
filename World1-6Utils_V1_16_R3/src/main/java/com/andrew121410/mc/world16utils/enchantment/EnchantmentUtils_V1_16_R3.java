package com.andrew121410.mc.world16utils.enchantment;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtils_V1_16_R3 implements EnchantmentUtils {
    @Override
    public Enchantment getByName(String name) {
        return Enchantment.getByKey(NamespacedKey.minecraft(name));
    }
}
