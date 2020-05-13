package com.andrew121410.mc.world16utils.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageLocale {

    private File langFolder;

    private JavaPlugin javaPlugin;
    private FileConfiguration config;

    public static final Map<String, String> LANGUAGE_MAP = new HashMap<>();

    public LanguageLocale(JavaPlugin plugin, String languageFile) {
        this.javaPlugin = plugin;

        if (!javaPlugin.getDataFolder().exists()) {
            this.javaPlugin.getDataFolder().mkdir();
        }

        langFolder = new File(this.javaPlugin.getDataFolder().getName(), "lang");
        if (!langFolder.exists()) langFolder.mkdir();

        if (langFolder == null) throw new RuntimeException("Something fucked up [LanguageLocale]");

        File languageConfig = new File(langFolder, languageFile);

        config = YamlConfiguration.loadConfiguration(languageConfig);
    }

    public boolean loadLanguage(File file, String language) {
        for (String key : config.getKeys(false)) {
            LANGUAGE_MAP.putIfAbsent(key, config.getString(key));
        }
        return true;
    }

    public String translate(String key) {
        return LANGUAGE_MAP.get(key);
    }
}
