package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16utils.World16Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ChatResponseManager {

    private final World16Utils plugin;
    private boolean running;

    private final Map<UUID, Response> responseMap;

    public ChatResponseManager(World16Utils plugin) {
        this.plugin = plugin;
        this.responseMap = new HashMap<>();
    }

    public boolean create(Player player, BiConsumer<Player, String> consumer) {
        return create(player, (Component) null, null, consumer);
    }

    public boolean create(Player player, String title, String subtitle, BiConsumer<Player, String> consumer) {
        return create(player, Translate.colorc(title), Translate.colorc(subtitle), consumer);
    }

    public boolean create(Player player, Component title, Component subtitle, BiConsumer<Player, String> consumer) {
        if (this.responseMap.containsKey(player.getUniqueId())) return false;
        this.responseMap.put(player.getUniqueId(), new Response(title, subtitle, consumer));
        loop();
        return true;
    }

    public boolean isWaiting(UUID uuid) {
        return this.responseMap.containsKey(uuid);
    }

    public BiConsumer<Player, String> get(Player player) {
        if (!this.responseMap.containsKey(player.getUniqueId())) return null;
        return this.responseMap.get(player.getUniqueId()).consumer();
    }

    public void remove(UUID uuid) {
        this.responseMap.remove(uuid);
    }

    private void loop() {
        if (this.running) return;
        this.running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (responseMap.size() == 0) {
                    running = false;
                    this.cancel();
                }
                Iterator<Map.Entry<UUID, Response>> iterator = responseMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Response> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    Response response = entry.getValue();

                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player == null) {
                        iterator.remove();
                        return;
                    }

                    Component title = response.title() != null ? response.title() : Translate.colorc("&bType in response");
                    Component subtitle = response.subtitle() != null ? response.subtitle() : Component.empty();
                    player.showTitle(Title.title(title, subtitle,
                            Title.Times.times(
                                    Ticks.duration(0),
                                    Ticks.duration(61),
                                    Ticks.duration(0)
                            )));

                    player.sendActionBar(Translate.colorc("&eType &ccancel &eto stop!"));
                }
            }
        }.runTaskTimer(this.plugin, 0L, 60L);
    }
}

record Response(Component title, Component subtitle, BiConsumer<Player, String> consumer) {
}
