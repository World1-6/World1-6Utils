package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ClickEventButton extends GUIButton {

    public Consumer<GUIClickEvent> consumer;

    public ClickEventButton(int slot, ItemStack itemStack, Consumer<GUIClickEvent> consumer) {
        super(slot, itemStack);
        this.consumer = consumer;
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {
        this.consumer.accept(guiClickEvent);
    }
}
