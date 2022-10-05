package com.andrew121410.mc.world16utils.updater;

import com.andrew121410.ccutils.utils.AbstractBasicSelfUpdater;
import com.andrew121410.mc.world16utils.World16Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManager {

    private static final Map<String, AbstractBasicSelfUpdater> updaters = new HashMap<>();

    public static void init() {
    }

    public static void registerUpdater(JavaPlugin javaPlugin, AbstractBasicSelfUpdater updater, boolean silent) {
        updaters.putIfAbsent(javaPlugin.getName(), updater);

        if (!silent) {
            javaPlugin.getServer().getScheduler().runTaskAsynchronously(javaPlugin, () -> {
                if (updater.shouldUpdate()) {
                    javaPlugin.getLogger().info(javaPlugin.getName() + " has an update available!");
                    javaPlugin.getLogger().info("You can update it by running: /world1-6utils update " + javaPlugin.getName());
                }
            });
        }
    }

    public static void registerUpdater(JavaPlugin javaPlugin, AbstractBasicSelfUpdater updater) {
        UpdateManager.registerUpdater(javaPlugin, updater, false);
    }

    public static void update(CommandSender sender, String pluginName) {
        AbstractBasicSelfUpdater updater = UpdateManager.getUpdater(pluginName);
        if (updater == null) {
            sender.sendMessage(Translate.color("&eThere is no updater for " + pluginName + "."));
            return;
        }

        sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <yellow>Checking for updates..."));
        World16Utils.getInstance().getServer().getScheduler().runTaskAsynchronously(World16Utils.getInstance(), () -> {
            if (updater.shouldUpdate()) {
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <red>Updating..."));
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <green>" + updater.update()));
            } else {
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <gray>There is no update available."));
            }
        });
    }

    public static void updateAll(CommandSender sender) {
        updaters.forEach((pluginName, updater) -> update(sender, pluginName));
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