package com.andrew121410.mc.world16utils.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class World16ConfigurateManager {

    private final List<TypeSerializerCollection> typeSerializerCollectionList;

    private final JavaPlugin plugin;

    public World16ConfigurateManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.typeSerializerCollectionList = new ArrayList<>();
    }

    public void registerTypeSerializerCollection(TypeSerializerCollection typeSerializerCollection) {
        this.typeSerializerCollectionList.add(typeSerializerCollection);
    }

    public YamlConfigurationLoader getYamlConfigurationLoader(String fileName) {
        final String path = plugin.getDataFolder() + File.separator + fileName;

        CustomConfigurateSerializers customConfigurateSerializers = new CustomConfigurateSerializers();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Paths.get(path))
                .defaultOptions(options -> options.serializers(builder -> {
                    builder
                            .registerAll(customConfigurateSerializers.getBukkitSerializers())
                            .registerAll(customConfigurateSerializers.getWorld16UtilsSerializers());
                    this.typeSerializerCollectionList.forEach(builder::registerAll);
                }))
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        return loader;
    }
}
