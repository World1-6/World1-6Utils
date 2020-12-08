package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16Utils extends JavaPlugin {

    private static World16Utils instance;

    private ClassWrappers classWrappers;

    @Override
    public void onEnable() {
        instance = this;
        this.classWrappers = new ClassWrappers(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {
        new OnInventoryClickEvent(this);
    }

    public static World16Utils getInstance() {
        return instance;
    }

    public ClassWrappers getClassWrappers() {
        return classWrappers;
    }
}
