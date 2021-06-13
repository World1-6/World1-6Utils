package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16Utils extends JavaPlugin {

    public static final String DATE_OF_VERSION = "6/12/2021";
    public static final String PREFIX = "[&9World1-6Utils&r]";
    public static final String USELESS_TAG = PREFIX + "->[&bUSELESS&r]";
    public static final String DEBUG_TAG = PREFIX + "->[&eDEBUG&r]";
    public static final String EMERGENCY_TAG = PREFIX + "->&c[EMERGENCY]&r";

    private static World16Utils instance;

    private ClassWrappers classWrappers;
    private ChatResponseManager chatResponseManager;

    @Override
    public void onEnable() {
        instance = this;
        this.classWrappers = new ClassWrappers(this);
        this.chatResponseManager = new ChatResponseManager(this);
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {
        new OnAsyncPlayerChatEvent(this, this.chatResponseManager);
        new OnInventoryClickEvent(this);
        new OnInventoryCloseEvent(this);
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
