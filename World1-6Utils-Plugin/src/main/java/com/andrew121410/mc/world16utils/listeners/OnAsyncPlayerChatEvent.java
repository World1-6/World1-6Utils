package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OnAsyncPlayerChatEvent implements Listener {

    private World16Utils plugin;
    private ChatResponseManager chatResponseManager;

    public OnAsyncPlayerChatEvent(World16Utils plugin, ChatResponseManager chatResponseManager) {
        this.plugin = plugin;
        this.chatResponseManager = chatResponseManager;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component message = event.message();
        String messageString = PlainTextComponentSerializer.plainText().serialize(message);

        if (chatResponseManager.isWaiting(player.getUniqueId())) {
            event.setCancelled(true);
            if (messageString.equalsIgnoreCase("cancel")) {
                chatResponseManager.remove(player.getUniqueId());
                player.sendMessage(Translate.color("&eSuccessfully canceled."));
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                chatResponseManager.get(player).accept(player, messageString);
                chatResponseManager.remove(player.getUniqueId());
            });
        }
    }
}
