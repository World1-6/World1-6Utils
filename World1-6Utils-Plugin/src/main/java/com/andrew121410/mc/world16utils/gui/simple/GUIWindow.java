package com.andrew121410.mc.world16utils.gui.simple;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class GUIWindow implements InventoryHolder {

    private Inventory inventory;

    public GUIWindow() {
    }

    public abstract String getName();

    public abstract int getSlotCount();

    public abstract List<ItemStack> populateInventory();

    public abstract boolean onSlotClicked(InventoryClickEvent event);

    public void open(Player player) {
        inventory = Bukkit.createInventory(this, getSlotCount(), getName());
        this.populateInventory();
        player.openInventory(this.inventory);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
