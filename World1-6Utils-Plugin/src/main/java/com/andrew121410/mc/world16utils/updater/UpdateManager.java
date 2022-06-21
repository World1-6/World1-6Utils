package com.andrew121410.mc.world16utils.updater;

import com.andrew121410.ccutils.utils.AbstractBasicSelfUpdater;
import com.andrew121410.mc.world16utils.World16Utils;
import net.frankheijden.serverutils.bukkit.managers.BukkitPluginManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManager {

    private static final Map<String, UpdateEntry> updaters = new HashMap<>();

    public static void init() {
    }

    public static void registerUpdater(JavaPlugin javaPlugin, AbstractBasicSelfUpdater updater, boolean shouldReload) {
        updaters.putIfAbsent(javaPlugin.getName(), new UpdateEntry(updater, shouldReload));

        javaPlugin.getServer().getScheduler().runTaskAsynchronously(javaPlugin, () -> {
            if (updater.shouldUpdate()) {
                javaPlugin.getLogger().info(javaPlugin.getName() + " has an update available!");
                javaPlugin.getLogger().info("You can update it by running: /world1-6utils update " + javaPlugin.getName());
            }
        });
    }

    public static void update(CommandSender sender, String pluginName) {
        UpdateEntry updateEntry = UpdateManager.getUpdateEntry(pluginName);
        if (updateEntry == null) {
            sender.sendMessage("There is no updater for " + pluginName + ".");
            return;
        }
        AbstractBasicSelfUpdater updater = updateEntry.getUpdater();

        sender.sendMessage("Checking for updates for " + pluginName + "...");
        World16Utils.getInstance().getServer().getScheduler().runTaskAsynchronously(World16Utils.getInstance(), () -> {
            if (updater.shouldUpdate()) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                File file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());

                sender.sendMessage("An update is available!");

                // Unload the plugin first if shouldReload is true and the server has ServerUtils plugin
                if (updateEntry.isShouldReload() && hasServerUtilsPlugin()) {
                    BukkitPluginManager.get().disablePlugin(plugin);
                    sender.sendMessage("Unloaded " + pluginName + " from the server.");
                }

                sender.sendMessage("Downloading update...");
                String message = updater.update();
                sender.sendMessage(message);

                // Load the plugin again if shouldReload is true and the server has ServerUtils plugin
                if (updateEntry.isShouldReload() && hasServerUtilsPlugin()) {
                    BukkitPluginManager.get().loadPlugin(file.getName());
                    sender.sendMessage("Loaded " + pluginName + "!");
                }

            } else {
                sender.sendMessage("There is no update available for " + pluginName + ".");
            }
        });
    }

    public static UpdateEntry getUpdateEntry(JavaPlugin javaPlugin) {
        return getUpdateEntry(javaPlugin.getName());
    }

    public static UpdateEntry getUpdateEntry(String pluginName) {
        return updaters.get(pluginName);
    }

    public static List<String> getPluginNamesFromUpdaters() {
        return new ArrayList<>(updaters.keySet());
    }

    public static boolean hasServerUtilsPlugin() {
        return World16Utils.getInstance().getServer().getPluginManager().getPlugin("ServerUtils") != null;
    }
}

class UpdateEntry {
    private final AbstractBasicSelfUpdater updater;
    private final boolean shouldReload;

    UpdateEntry(AbstractBasicSelfUpdater updater, boolean shouldReload) {
        this.updater = updater;
        this.shouldReload = shouldReload;
    }

    public AbstractBasicSelfUpdater getUpdater() {
        return updater;
    }

    public boolean isShouldReload() {
        return shouldReload;
    }
}