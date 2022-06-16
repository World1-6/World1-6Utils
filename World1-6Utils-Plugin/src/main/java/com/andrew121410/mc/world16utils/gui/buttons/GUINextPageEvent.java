package com.andrew121410.mc.world16utils.gui.buttons;

import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUINextPageEvent extends GUIClickEvent {

    private final boolean afterPageCreation;

    public GUINextPageEvent(GUIWindow guiWindow, InventoryClickEvent event, boolean afterPageCreation) {
        super(guiWindow, event);
        this.afterPageCreation = afterPageCreation;
    }

    public GUINextPageEvent(GUIClickEvent guiClickEvent, boolean afterPageCreation) {
        this(guiClickEvent.getGuiWindow(), guiClickEvent.getEvent(), afterPageCreation);
    }

    public boolean isAfterPageCreation() {
        return afterPageCreation;
    }
}
