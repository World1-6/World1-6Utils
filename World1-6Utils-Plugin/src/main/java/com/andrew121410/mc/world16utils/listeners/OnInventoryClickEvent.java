package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.gui.simple.GUIWindow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class OnInventoryClickEvent implements Listener {

    private World16Utils plugin;

    public OnInventoryClickEvent(World16Utils plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryHolder inventoryHolder = event.getClickedInventory().getHolder();
            if (inventoryHolder instanceof GUIWindow) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) return;
                GUIWindow guiWindow = (GUIWindow) inventoryHolder;
                guiWindow.onSlotClicked(event);
            }
        }

    }
}
