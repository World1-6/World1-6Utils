package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class OnInventoryCloseEvent implements Listener {

    public World16Utils plugin;

    public OnInventoryCloseEvent(World16Utils plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof GUIWindow) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof GUIWindow) return;
                GUIWindow guiWindow = (GUIWindow) inventoryHolder;
                guiWindow.onClose(event);
            }, 2L);
        }
    }
}
