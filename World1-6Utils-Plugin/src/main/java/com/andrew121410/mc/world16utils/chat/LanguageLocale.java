package com.andrew121410.mc.world16utils.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageLocale {

    private final JavaPlugin javaPlugin;
    private final FileConfiguration config;

    public static final Map<String, Map<String, String>> LANGUAGE_MAP = new HashMap<>();

    public LanguageLocale(JavaPlugin plugin, String languageFile) {
        this.javaPlugin = plugin;

        if (!javaPlugin.getDataFolder().exists()) {
            this.javaPlugin.getDataFolder().mkdir();
        }

        File langFolder = new File(this.javaPlugin.getDataFolder().getName(), "lang");
        if (!langFolder.exists()) langFolder.mkdir();

        File languageConfig = new File(langFolder, languageFile);
        this.config = YamlConfiguration.loadConfiguration(languageConfig);

        LANGUAGE_MAP.putIfAbsent(javaPlugin.getName(), new HashMap<>());
    }

    public boolean loadLanguage() {
        for (String key : config.getKeys(false)) {
            LANGUAGE_MAP.get(javaPlugin.getName()).putIfAbsent(key, config.getString(key));
        }
        return true;
    }

    public String translate(String key) {
        String real = LANGUAGE_MAP.get(javaPlugin.getName()).get(key);
        return Translate.color(real);
    }
}
