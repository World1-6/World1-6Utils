package com.andrew121410.mc.world16utils.updater;

import com.andrew121410.ccutils.utils.AbstractBasicSelfUpdater;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManager {

    private static final Map<String, AbstractBasicSelfUpdater> updaters = new HashMap<>();

    public static void init() {
    }

    public static void registerUpdater(JavaPlugin javaPlugin, AbstractBasicSelfUpdater updater) {
        updaters.putIfAbsent(javaPlugin.getName(), updater);

        javaPlugin.getServer().getScheduler().runTaskAsynchronously(javaPlugin, () -> {
            if (updater.shouldUpdate()) {
                javaPlugin.getLogger().info(javaPlugin.getName() + " has an update available!");
                javaPlugin.getLogger().info("You can update it by running: /world1-6utils update " + javaPlugin.getName());
            }
        });
    }

    public static AbstractBasicSelfUpdater getUpdater(JavaPlugin javaPlugin) {
        return getUpdater(javaPlugin.getName());
    }

    public static AbstractBasicSelfUpdater getUpdater(String pluginName) {
        return updaters.get(pluginName);
    }

    public static List<String> getPluginNamesFromUpdaters() {
        return new ArrayList<>(updaters.keySet());
    }
}
