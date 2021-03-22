package com.andrew121410.mc.world16utils.gui.buttons.defaults;

import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import org.bukkit.inventory.ItemStack;

public class NoEventButton extends GUIButton {

    public NoEventButton(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(GUIClickEvent guiClickEvent) {

    }
}
