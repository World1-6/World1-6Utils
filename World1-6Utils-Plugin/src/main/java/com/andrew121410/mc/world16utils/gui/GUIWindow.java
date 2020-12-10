package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GUIWindow implements InventoryHolder {

    private boolean isFirst;
    private Inventory inventory;

    public GUIWindow() {
        this.isFirst = true;
    }

    public abstract String getName();

    public abstract int getSlotCount();

    public abstract void onCreate(Player player);

    public abstract void populateInventory(Player player);

    public abstract boolean onSlotClicked(InventoryClickEvent event);

    public void open(Player player) {
        if (this.isFirst) {
            this.onCreate(player);
            this.isFirst = false;
        }
        inventory = Bukkit.createInventory(this, getSlotCount(), Translate.color(getName()));
        this.populateInventory(player);
        player.openInventory(this.inventory);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
