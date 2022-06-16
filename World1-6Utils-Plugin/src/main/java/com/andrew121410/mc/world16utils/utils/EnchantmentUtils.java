package com.andrew121410.mc.world16utils.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentUtils {
    public static Enchantment getByName(String name) {
        return Enchantment.getByKey(NamespacedKey.minecraft(name));
    }

    public static List<String> getVanillaEnchantmentNames() {
        List<String> names = new ArrayList<>();
        for (Enchantment enchantment : Enchantment.values()) {
            names.add(enchantment.getKey().getKey());
        }
        return names;
    }
}
