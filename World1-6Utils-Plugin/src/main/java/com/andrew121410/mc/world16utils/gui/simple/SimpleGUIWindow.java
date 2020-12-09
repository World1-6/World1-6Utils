package com.andrew121410.mc.world16utils.gui.simple;

import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class SimpleGUIWindow extends GUIWindow {

    private String name;
    private int slots;
    private Map<Integer, SimpleGUIItem> guiItemMap;

    public SimpleGUIWindow(String name, int slots) {
        this.name = name;
        this.slots = slots;
        this.guiItemMap = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSlotCount() {
        return this.slots;
    }

    @Override
    public void populateInventory() {
        this.guiItemMap.forEach((k, v) -> this.getInventory().setItem(k, v.getItemStack()));
    }

    @Override
    public boolean onSlotClicked(InventoryClickEvent event) {
        if (event == null) return false;
        if (event.getCurrentItem() == null) return false;
        SimpleGUIItem simpleGUIItem = this.guiItemMap.get(event.getSlot());
        if (simpleGUIItem != null) {
            if (simpleGUIItem.getConsumer() != null) {
                simpleGUIItem.getConsumer().accept(event);
                return true;
            }
        }
        return false;
    }

    public void update(Map<Integer, SimpleGUIItem> guiItemMap) {
        this.guiItemMap = guiItemMap;
    }
}
