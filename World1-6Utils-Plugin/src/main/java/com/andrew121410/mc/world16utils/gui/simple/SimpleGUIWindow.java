package com.andrew121410.mc.world16utils.gui.simple;

import com.andrew121410.mc.world16utils.gui.GUIItem;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class SimpleGUIWindow extends GUIWindow {

    private String name;
    private int slots;
    private Map<Integer, GUIItem> guiItemMap;

    public SimpleGUIWindow(String name, int slots, Map<Integer, GUIItem> guiItemMap) {
        this.name = name;
        this.slots = slots;
        this.guiItemMap = guiItemMap;
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
    public void onCreate(Player player) {

    }

    @Override
    public void populateInventory(Player player) {
        this.guiItemMap.forEach((k, v) -> this.getInventory().setItem(k, v.getItemStack()));
    }

    @Override
    public boolean onSlotClicked(InventoryClickEvent event) {
        if (event == null) return false;
        if (event.getCurrentItem() == null) return false;
        GUIItem guiItem = this.guiItemMap.get(event.getSlot());
        if (guiItem != null) {
            if (guiItem.getConsumer() != null) {
                guiItem.getConsumer().accept(event);
                return true;
            }
        }
        return false;
    }
}
