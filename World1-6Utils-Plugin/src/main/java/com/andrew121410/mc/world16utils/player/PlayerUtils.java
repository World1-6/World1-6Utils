package com.andrew121410.mc.world16utils.player;

import com.andrew121410.mc.world16utils.World16Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerUtils {

    public static final Map<UUID, PlayerTextures> playerTexturesMap = new HashMap<>();

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static Block getBlockPlayerIsLookingAt(Player player, int range) {
        return player.getTargetBlock(null, range);
    }

    private static ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(player.getName());

        // Player heads don't work with bedrock players
        if (Bukkit.getPluginManager().getPlugin("floodgate") != null) {
            if (FloodgateApi.getInstance().isFloodgateId(player.getUniqueId())) {
                itemStack.setItemMeta(skullMeta);
                return itemStack;
            }
        }

        PlayerProfile playerProfile = player.getPlayerProfile();
        if (!playerProfile.isComplete() && !playerTexturesMap.containsKey(player.getUniqueId())) {
            try {
                playerProfile = playerProfile.update().get();

                // Just in case
                if (playerProfile == null || !playerProfile.isComplete()) {
                    itemStack.setItemMeta(skullMeta);
                    return itemStack;
                }

                playerTexturesMap.putIfAbsent(player.getUniqueId(), playerProfile.getTextures());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            PlayerTextures playerTextures = playerTexturesMap.getOrDefault(player.getUniqueId(), null);
            playerProfile.setTextures(playerTextures);
        }
        skullMeta.setOwnerProfile(playerProfile);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public static void getPlayerHead(OfflinePlayer player, Consumer<ItemStack> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(World16Utils.getInstance(), () -> {
            ItemStack itemStack = getPlayerHead(player);
            Bukkit.getScheduler().runTask(World16Utils.getInstance(), () -> consumer.accept(itemStack));
        });
    }

    public static void getPlayerHeads(List<OfflinePlayer> players, Consumer<Map<OfflinePlayer, ItemStack>> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(World16Utils.getInstance(), () -> {
            Map<OfflinePlayer, ItemStack> itemStackMap = players.stream().collect(Collectors.toMap(player -> player, PlayerUtils::getPlayerHead));
            Bukkit.getScheduler().runTask(World16Utils.getInstance(), () -> consumer.accept(itemStackMap));
        });
    }
}
