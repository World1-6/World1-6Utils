package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatClickCallbackManager;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.commands.World16UtilsCommand;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenController;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.listeners.OnPlayerQuitEvent;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;


public final class World16Utils extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(UnlinkedWorldLocation.class, "UnlinkedWorldLocation");
    }

    public static final String DATE_OF_VERSION = BlossomOutput.DATE_OF_BUILD;
    public static final String PREFIX = "[&9World1-6Utils&r]";
    private static World16Utils instance;

    private ClassWrappers classWrappers;

    private ChatResponseManager chatResponseManager;
    private ChatClickCallbackManager chatClickCallbackManager;
    private SignScreenController signScreenController;

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
        this.signScreenController = new SignScreenController(this);
        registerListeners();
        registerCommands();

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

    private void registerCommands() {
        new World16UtilsCommand(this);
    }

    @Deprecated
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

    public SignScreenController getSignScreenController() {
        return signScreenController;
    }
}
