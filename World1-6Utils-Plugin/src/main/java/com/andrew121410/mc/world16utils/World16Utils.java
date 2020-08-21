package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.utils.ClassWrappers;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16Utils extends JavaPlugin {

    private ClassWrappers classWrappers;

    @Override
    public void onEnable() {
        this.classWrappers = new ClassWrappers(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ClassWrappers getClassWrappers() {
        return classWrappers;
    }
}
