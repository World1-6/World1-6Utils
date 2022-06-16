package com.andrew121410.mc.world16utils.gui.buttons;

import org.bukkit.inventory.ItemStack;

public abstract class GUIButton {

    private int slot;
    private ItemStack itemStack;

    public GUIButton(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public abstract void onClick(GUIClickEvent guiClickEvent);
}
