package com.andrew121410.mc.world16utils;

import com.andrew121410.ccutils.utils.AbstractBasicSelfUpdater;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import com.andrew121410.mc.world16utils.updater.Updater;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class World16Utils extends JavaPlugin {

    public static final String DATE_OF_VERSION = "6/15/2022";
    public static final String PREFIX = "[&9World1-6Utils&r]";
    public static final String USELESS_TAG = PREFIX + "->[&bUSELESS&r]";
    public static final String DEBUG_TAG = PREFIX + "->[&eDEBUG&r]";
    public static final String EMERGENCY_TAG = PREFIX + "->&c[EMERGENCY]&r";

    private static World16Utils instance;

    private ClassWrappers classWrappers;
    private ChatResponseManager chatResponseManager;

    @Override
    public void onLoad() {
        UpdateManager.init();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.classWrappers = new ClassWrappers(this);
        this.chatResponseManager = new ChatResponseManager(this);
        registerListeners();
        registerCommand();

        // Register updater also check for updates.
        UpdateManager.registerUpdater(this, new Updater(this));
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
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                String pluginName = args[1];

                AbstractBasicSelfUpdater updater = UpdateManager.getUpdater(pluginName);
                if (updater == null) {
                    sender.sendMessage("There is no updater for " + pluginName + ".");
                    return true;
                }

                sender.sendMessage("Checking for updates for " + pluginName + "...");
                getServer().getScheduler().runTaskAsynchronously(this, () -> {
                    if (updater.shouldUpdate()) {
                        sender.sendMessage("An update is available!");
                        sender.sendMessage("Downloading update...");
                        String message = updater.update();
                        sender.sendMessage(message);
                    } else {
                        sender.sendMessage("There is no update available for " + pluginName + ".");
                    }
                });
            }
            return true;
        });

        getCommand("world1-6utils").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) return null;

            if (args.length == 1) {
                return Arrays.asList("update");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                return TabUtils.getContainsString(args[1], UpdateManager.getPluginNamesFromUpdaters());
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
