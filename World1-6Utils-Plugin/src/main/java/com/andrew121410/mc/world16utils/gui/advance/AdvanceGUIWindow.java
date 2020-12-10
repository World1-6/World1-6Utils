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
        GUIItem simpleGUIItem = this.guiItemMap.get(event.getSlot());
        if (simpleGUIItem != null) {
            if (simpleGUIItem.getConsumer() != null) {
                simpleGUIItem.getConsumer().accept(event);
                return true;
            }
        }
        return false;
    }

    public void update(Map<Integer, GUIItem> guiItemMap) {
        this.guiItemMap = guiItemMap;
    }

    public void update(Map<Integer, GUIItem> guiItemMap, int slots) {
        this.guiItemMap = guiItemMap;
        this.slots = slots;
    }

    public void update(Map<Integer, GUIItem> guiItemMap, String name, int slots) {
        this.guiItemMap = guiItemMap;
        this.name = name;
        this.slots = slots;
    }

    public void update(List<GUIItem> list) {
        this.guiItemMap = new HashMap<>();
        for (GUIItem simpleGUIItem : list) this.guiItemMap.putIfAbsent(simpleGUIItem.getSlot(), simpleGUIItem);
    }

    public void update(List<GUIItem> list, int slots) {
        this.guiItemMap = new HashMap<>();
        for (GUIItem simpleGUIItem : list) this.guiItemMap.putIfAbsent(simpleGUIItem.getSlot(), simpleGUIItem);
        this.slots = slots;
    }

    public void update(List<GUIItem> list, String name, int slots) {
        this.guiItemMap = new HashMap<>();
        for (GUIItem simpleGUIItem : list) this.guiItemMap.putIfAbsent(simpleGUIItem.getSlot(), simpleGUIItem);
        this.name = name;
        this.slots = slots;
    }
}
