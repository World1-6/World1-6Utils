package com.andrew121410.mc.world16utils.config;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class World16ConfigurateManager {

    private List<TypeSerializerCollection> typeSerializerCollectionList;

    private JavaPlugin plugin;

    public World16ConfigurateManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.typeSerializerCollectionList = new ArrayList<>();
    }

    public void registerCustomTypeSerializer(TypeSerializerCollection typeSerializerCollection) {
        this.typeSerializerCollectionList.add(typeSerializerCollection);
    }

    public YamlConfigurationLoader getYamlConfigurationLoader(String fileName) {
        final String path = plugin.getDataFolder() + File.separator + fileName;

        BukkitConfigurateSerializers bukkitConfigurateSerializers = new BukkitConfigurateSerializers();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Paths.get(path))
                .defaultOptions(options -> options.serializers(builder -> {
                    builder
                            .registerAll(bukkitConfigurateSerializers.getBukkitSerializers())
                            .registerAll(bukkitConfigurateSerializers.getWorld16UtilsSerializers());
                    this.typeSerializerCollectionList.forEach(builder::registerAll);
                }))
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        return loader;
    }

    private void test(Player player) {
        YamlConfigurationLoader loader = this.getYamlConfigurationLoader("test.yml");

        BoundingBox boundingBox = new BoundingBox(0, 0, 0, 1, 1, 1);
        try {
            CommentedConfigurationNode node = loader.load();
            node.node("TheDamnBoundingBox").set(boundingBox);

            node.node("Testing", "hi").set("Hello World");
            node.node("Testing", "yes").set(5);
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }

        // test.yml
        //TheDamnBoundingBox:
        //    minX: 0.0
        //    minY: 0.0
        //    minZ: 0.0
        //    maxX: 1.0
        //    maxY: 1.0
        //    maxZ: 1.0
        //Testing:
        //    hi: Hello World
        //    yes: 5
    }
}
