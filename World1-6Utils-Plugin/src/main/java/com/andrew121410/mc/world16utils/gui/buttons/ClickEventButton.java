package com.andrew121410.mc.world16utils.gui.buttons;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ClickEventButton extends GUIButton {

    public Consumer<InventoryClickEvent> consumer;

    public ClickEventButton(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        super(slot, itemStack);
        this.consumer = consumer;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        this.consumer.accept(event);
    }
}
