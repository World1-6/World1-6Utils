package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16utils.World16Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ChatResponseManager {

    private World16Utils plugin;
    private boolean running;

    private Map<UUID, Response> consumerMap;

    public ChatResponseManager(World16Utils plugin) {
        this.plugin = plugin;
        this.consumerMap = new HashMap<>();
    }

    public boolean create(Player player, String title, String subtitle, BiConsumer<Player, String> consumer) {
        if (this.consumerMap.containsKey(player.getUniqueId())) return false;
        this.consumerMap.put(player.getUniqueId(), new Response(title, subtitle, consumer));
        loop();
        return true;
    }

    public boolean isWaiting(UUID uuid) {
        return this.consumerMap.containsKey(uuid);
    }

    public BiConsumer<Player, String> get(Player player) {
        if (!this.consumerMap.containsKey(player.getUniqueId())) return null;
        return this.consumerMap.get(player.getUniqueId()).getConsumer();
    }

    public void remove(UUID uuid) {
        this.consumerMap.remove(uuid);
    }

    private void loop() {
        if (this.running) return;
        this.running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (consumerMap.size() == 0) {
                    running = false;
                    this.cancel();
                }
                Iterator<Map.Entry<UUID, Response>> iterator = consumerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Response> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    Response response = entry.getValue();
                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player == null) {
                        iterator.remove();
                        return;
                    }
                    player.sendTitle(response.getTitle() != null ? response.getTitle() : Translate.color("&bType in response")
                            , response.getSubtitle() != null ? response.getSubtitle() : ""
                            , 0, 61, 0);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Translate.color("&eType &ccancel &eto stop!")));
                }
            }
        }.runTaskTimer(this.plugin, 0L, 60L);
    }
}

class Response {
    private String title;
    private String subtitle;
    private BiConsumer<Player, String> consumer;

    public Response(String title, String subtitle, BiConsumer<Player, String> consumer) {
        this.title = title;
        this.subtitle = subtitle;
        this.consumer = consumer;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public BiConsumer<Player, String> getConsumer() {
        return consumer;
    }
}
