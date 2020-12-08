package com.andrew121410.mc.world16utils.gui.simple;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SimpleGUIItem {

    private int slot;
    private ItemStack itemStack;
    private Consumer<InventoryClickEvent> consumer;

    public SimpleGUIItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.consumer = consumer;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Consumer<InventoryClickEvent> getConsumer() {
        return consumer;
    }
}
