package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.math.BoundingBox;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16Utils extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(BoundingBox.class, "BoundingBox");
    }

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
