package com.andrew121410.mc.world16utils.gui;

import org.bukkit.inventory.ItemStack;

public class GUIItem {
    private int slot;
    private ItemStack itemStack;

    public GUIItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
