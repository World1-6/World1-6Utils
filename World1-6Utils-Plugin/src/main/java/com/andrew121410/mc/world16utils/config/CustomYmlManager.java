package com.andrew121410.mc.world16utils.config;

import com.andrew121410.mc.world16utils.chat.LanguageLocale;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomYmlManager {

    private JavaPlugin plugin;

    private FileConfiguration fileConfiguration;
    private File file;

    private String fileName;
    private boolean debug;

    public CustomYmlManager(JavaPlugin plugin, boolean debug) {
        this.plugin = plugin;
        this.debug = debug;
    }

    public void setup(String fileName) {
        this.fileName = fileName;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), this.fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(LanguageLocale.color(Utils.USELESS_TAG + " The {nameoffile} has been created.").replace("{nameoffile}", this.fileName));
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(LanguageLocale
                                .color(Utils.USELESS_TAG + " The {nameoffile} could not make for some reason.").replace("{nameoffile}", this.fileName));
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(file);
            if (this.debug) {
                Bukkit.getServer().getConsoleSender().sendMessage(LanguageLocale.color(Utils.USELESS_TAG + " &aThe {name} has been saved.").replace("{name}", this.fileName));
            }
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(LanguageLocale.color(Utils.USELESS_TAG + " &cThe {name} has been NOT SAVED..").replace("{name}", this.fileName));
        }
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (this.debug) {
            Bukkit.getServer().getConsoleSender().sendMessage(LanguageLocale.color(Utils.USELESS_TAG + " &6The {nameoffile} has been reloaded.").replace("{nameoffile}", this.fileName));
        }
    }
}
