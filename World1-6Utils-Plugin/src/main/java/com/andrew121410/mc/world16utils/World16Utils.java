package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatClickCallbackManager;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.listeners.OnPlayerQuitEvent;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class World16Utils extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(UnlinkedWorldLocation.class, "UnlinkedWorldLocation");
    }

    public static final String DATE_OF_VERSION = "7/26/2022";
    public static final String PREFIX = "[&9World1-6Utils&r]";
    private static World16Utils instance;

    private ClassWrappers classWrappers;

    private ChatResponseManager chatResponseManager;
    private ChatClickCallbackManager chatClickCallbackManager;

    @Override
    public void onLoad() {
        UpdateManager.init();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.classWrappers = new ClassWrappers(this);
        this.chatResponseManager = new ChatResponseManager(this);
        this.chatClickCallbackManager = new ChatClickCallbackManager(this);
        registerListeners();
        registerCommand();

        // Register updater for this plugin, checks for updates, and registers it so that you can use "/world1-6utils update" to update this plugin
        UpdateManager.registerUpdater(this, new Updater(this));
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        new OnAsyncPlayerChatEvent(this, this.chatResponseManager);
        new OnInventoryClickEvent(this);
        new OnInventoryCloseEvent(this);
        new OnPlayerQuitEvent(this);
    }

    private void registerCommand() {
        getCommand("world1-6utils").setExecutor((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("/world1-6utils update");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                String pluginName = args[1];

                if (pluginName.equalsIgnoreCase("all")) {
                    UpdateManager.updateAll(sender);
                    return true;
                }

                UpdateManager.update(sender, pluginName);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("callclickevent")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                Map<String, Consumer<Player>> map = this.chatClickCallbackManager.getMap().get(player.getUniqueId());
                if (map == null) return true;

                String key = args[1];
                Consumer<Player> consumer = map.get(key);
                if (consumer == null) return true;
                consumer.accept((Player) sender);
                map.remove(key);
            }
            return true;
        });

        getCommand("world1-6utils").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) return null;

            if (args.length == 1) {
                return Arrays.asList("update");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                List<String> strings = UpdateManager.getPluginNamesFromUpdaters();
                strings.add("all");
                return TabUtils.getContainsString(args[1], strings);
            }
            return null;
        });
    }

    public static boolean isOnPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static World16Utils getInstance() {
        return instance;
    }

    public ClassWrappers getClassWrappers() {
        return classWrappers;
    }

    public ChatResponseManager getChatResponseManager() {
        return chatResponseManager;
    }

    public ChatClickCallbackManager getChatClickCallbackManager() {
        return chatClickCallbackManager;
    }
}
