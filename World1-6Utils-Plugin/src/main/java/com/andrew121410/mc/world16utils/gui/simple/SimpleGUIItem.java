package com.andrew121410.mc.world16utils.gui.simple;

import com.andrew121410.mc.world16utils.gui.GUIItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SimpleGUIItem extends GUIItem {

    private Consumer<InventoryClickEvent> consumer;

    public SimpleGUIItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        super(slot, itemStack);
        this.consumer = consumer;
    }

    public Consumer<InventoryClickEvent> getConsumer() {
        return consumer;
    }
}
