package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleGUIWindow extends GUIWindow {

    private String name;
    private int slots;
    private Map<Integer, GUIButton> guiItemMap;

    public SimpleGUIWindow(String name, int slots, List<GUIButton> guiButtons) {
        this.name = name;
        this.slots = slots;
        this.guiItemMap = guiButtons.stream().collect(Collectors.toMap(GUIButton::getSlot, k -> k));
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
        GUIButton guiButton = this.guiItemMap.get(event.getSlot());
        if (guiButton != null) guiButton.onClick(new GUIClickEvent(this, event));
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
