package com.andrew121410.mc.world16utils.gui.advance;

import com.andrew121410.mc.world16utils.gui.GUIItem;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AdvanceGUIWindow extends GUIWindow {

    private String name;
    private int slots;
    private Map<Integer, GUIItem> guiItemMap;

    public AdvanceGUIWindow() {
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

    public void update(Map<Integer, GUIItem> guiItemMap) {
        update(guiItemMap, null, null);
    }

    public void update(Map<Integer, GUIItem> guiItemMap, String name) {
        update(guiItemMap, name, null);
    }

    public void update(Map<Integer, GUIItem> guiItemMap, int slots) {
        update(guiItemMap, null, slots);
    }

    public void update(Map<Integer, GUIItem> guiItemMap, String name, Integer slots) {
        if (guiItemMap != null) this.guiItemMap = guiItemMap;
        else this.guiItemMap = new HashMap<>();
        if (name != null) this.name = name;
        if (slots != null) this.slots = slots;
    }

    public void update(List<GUIItem> list) {
        update(list, null, null);
    }

    public void update(List<GUIItem> list, String name) {
        update(list, name, null);
    }

    public void update(List<GUIItem> list, int slots) {
        update(list, null, slots);
    }

    public void update(List<GUIItem> list, String name, Integer slots) {
        this.guiItemMap = new HashMap<>();
        if (list != null)
            for (GUIItem simpleGUIItem : list) this.guiItemMap.putIfAbsent(simpleGUIItem.getSlot(), simpleGUIItem);
        if (name != null) this.name = name;
        if (slots != null) this.slots = slots;
    }
}
