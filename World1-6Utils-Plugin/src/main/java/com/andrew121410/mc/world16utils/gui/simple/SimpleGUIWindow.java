package com.andrew121410.mc.world16utils.gui.simple;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleGUIWindow extends GUIWindow {

    private String name;
    private int slots;
    private Map<Integer, SimpleGUIItem> guiItemMap;

    public SimpleGUIWindow(String name, int slots, Map<Integer, SimpleGUIItem> guiItemMap) {
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
    public List<ItemStack> populateInventory() {
        return this.guiItemMap.values().stream().map(SimpleGUIItem::getItemStack).collect(Collectors.toList());
    }

    @Override
    public boolean onSlotClicked(InventoryClickEvent event) {
        if (this.guiItemMap.containsKey(event.getSlot())) {
            this.guiItemMap.get(event.getSlot()).getConsumer().accept(event);
            return true;
        }
        return false;
    }
}
