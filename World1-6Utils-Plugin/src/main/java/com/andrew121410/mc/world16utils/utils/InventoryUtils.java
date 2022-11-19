package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.chat.Translate;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtils {
    public static ItemStack createItem(Material material, int amount, String displayName, String... loreString) {
        List<Component> loreList = new ArrayList<>();
        for (String s : loreString) loreList.add(Translate.colorc(s));
        Component[] loreArray = new Component[loreList.size()];
        loreArray = loreList.toArray(loreArray);
        return createItem(material, amount, Translate.colorc(displayName), loreArray);
    }

    public static ItemStack createItem(Material material, int amount, Component component, Component... lores) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(component);
        List<Component> lore = new ArrayList<>(Arrays.asList(lores));
        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createItem(Material material, int amount, String displayName) {
        return createItem(material, amount, Translate.colorc(displayName));
    }

    public static ItemStack createItem(Material material, int amount, Component component) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(component);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static List<List<ItemStack>> splitInventoryIntoBaseAndExtraContents(List<ItemStack> inventoryContents) {
        List<List<ItemStack>> listList = new ArrayList<>();
        List<ItemStack> baseContents = new ArrayList<>();
        List<ItemStack> extraContents = new ArrayList<>();
        for (int i = 0; i < inventoryContents.size(); i++) {
            ItemStack itemStack = inventoryContents.get(i);
            if (i < 36) {
                baseContents.add(itemStack);
            } else {
                extraContents.add(itemStack);
            }
        }
        listList.add(baseContents);
        listList.add(extraContents);
        return listList;
    }

    public static ItemStack[] listToArray(List<ItemStack> itemStackList) {
        ItemStack[] itemStacks = new ItemStack[itemStackList.size()];
        for (int i = 0; i < itemStackList.size(); i++) {
            itemStacks[i] = itemStackList.get(i);
        }
        return itemStacks;
    }

    public static ItemStack getItemInItemStackArrayIfExist(ItemStack[] itemStacks, int index) {
        if (index >= 0 && index < itemStacks.length) {
            return itemStacks[index];
        }
        return null;
    }
}
