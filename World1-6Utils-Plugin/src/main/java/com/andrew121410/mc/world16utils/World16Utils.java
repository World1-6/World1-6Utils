package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.updater.Updater;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class World16Utils extends JavaPlugin {

    public static final String DATE_OF_VERSION = "4/11/2022";
    public static final String PREFIX = "[&9World1-6Utils&r]";
    public static final String USELESS_TAG = PREFIX + "->[&bUSELESS&r]";
    public static final String DEBUG_TAG = PREFIX + "->[&eDEBUG&r]";
    public static final String EMERGENCY_TAG = PREFIX + "->&c[EMERGENCY]&r";

    private static World16Utils instance;

    private Updater updater;
    private ClassWrappers classWrappers;
    private ChatResponseManager chatResponseManager;

    @Override
    public void onEnable() {
        instance = this;
        this.updater = new Updater(this);
        this.classWrappers = new ClassWrappers(this);
        this.chatResponseManager = new ChatResponseManager(this);
        registerListeners();
        registerCommand();

        // Check for updates
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            if (updater.shouldUpdate(false)) {
                getLogger().info("Looks like there is an update available for World1-6Utils!");
                getLogger().info("You can update to it by using the command: /world1-6utils update");
            }
        });
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        new OnAsyncPlayerChatEvent(this, this.chatResponseManager);
        new OnInventoryClickEvent(this);
        new OnInventoryCloseEvent(this);
    }

    private void registerCommand() {
        getCommand("world1-6utils").setExecutor((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("/world1-6utils update");
            } else if (args[0].equalsIgnoreCase("update")) {
                sender.sendMessage("Checking for updates...");
                getServer().getScheduler().runTaskAsynchronously(this, () -> {
                    if (updater.shouldUpdate(true)) {
                        sender.sendMessage("Updating World1-6Utils...");
                        String message = updater.update();
                        sender.sendMessage(message);
                    } else {
                        getLogger().info("World1-6Utils is up to date!");
                    }
                });
            }
            return true;
        });

        getCommand("world1-6utils").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) return null;

            if (args.length == 0) {
                return Arrays.asList("update");
            }
            return null;
        });
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
}
