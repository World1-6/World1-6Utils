package com.andrew121410.mc.world16utils.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtils {
    public Enchantment getByName(String name) {
        return Enchantment.getByKey(NamespacedKey.minecraft(name));
    }
}
