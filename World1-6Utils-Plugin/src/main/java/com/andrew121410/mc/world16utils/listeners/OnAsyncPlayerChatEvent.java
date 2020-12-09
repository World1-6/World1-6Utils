package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncPlayerChatEvent implements Listener {

    private World16Utils plugin;
    private ChatResponseManager chatResponseManager;

    public OnAsyncPlayerChatEvent(World16Utils plugin, ChatResponseManager chatResponseManager) {
        this.plugin = plugin;
        this.chatResponseManager = chatResponseManager;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (chatResponseManager.isWaiting(player.getUniqueId())) {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                chatResponseManager.remove(player.getUniqueId());
                player.sendMessage(Translate.color("&eSuccessfully canceled."));
                return;
            }
            chatResponseManager.get(player).accept(player, event.getMessage());
            chatResponseManager.remove(player.getUniqueId());
        }
    }
}
