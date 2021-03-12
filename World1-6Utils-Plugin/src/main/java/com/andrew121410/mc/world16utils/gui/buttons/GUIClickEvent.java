package com.andrew121410.mc.world16utils.gui.buttons;

import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClickEvent {

    private GUIWindow guiWindow;
    private InventoryClickEvent event;

    public GUIClickEvent(GUIWindow guiWindow, InventoryClickEvent event) {
        this.guiWindow = guiWindow;
        this.event = event;
    }

    public GUIWindow getGuiWindow() {
        return guiWindow;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}
