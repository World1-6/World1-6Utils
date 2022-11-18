package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16utils.World16Utils;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatClickCallbackManager {

    private final World16Utils plugin;
    private final Map<String, Consumer<Player>> consumerMap;

    public ChatClickCallbackManager(World16Utils plugin) {
        this.plugin = plugin;
        this.consumerMap = new HashMap<>();
    }

    public ClickEvent create(Consumer<Player> consumer) {
        String key = UUID.randomUUID().toString();
        this.consumerMap.put(key, consumer);
        return ClickEvent.runCommand("/world1-6utils callclickevent " + key);
    }

    public Consumer<Player> get(String key) {
        return this.consumerMap.get(key);
    }

    public void remove(String key) {
        this.consumerMap.remove(key);
    }
}