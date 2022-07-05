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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PlayerUtils {

    public static final ConcurrentHashMap<UUID, PlayerTextures> PLAYER_TEXTURES_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    public static final ExecutorService PROFILE_EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static Block getBlockPlayerIsLookingAt(Player player, int range) {
        return player.getTargetBlock(null, range);
    }

    public static CompletableFuture<ItemStack> getPlayerHead(OfflinePlayer player) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(player.getName());

        // Player heads don't work with bedrock players
        if (Bukkit.getPluginManager().getPlugin("floodgate") != null) {
            if (FloodgateApi.getInstance().isFloodgateId(player.getUniqueId())) {
                itemStack.setItemMeta(skullMeta);
                return CompletableFuture.completedFuture(itemStack);
            }
        }

        PlayerProfile playerProfile = player.getPlayerProfile();
        // PlayerProfile is already completed that means the profile has skin data so lets use that.
        if (playerProfile.isComplete()) {
            skullMeta.setOwnerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
            return CompletableFuture.completedFuture(itemStack);
        }

        // If the player is already in cache then return the head from that
        ItemStack fromPlayerCache = getPlayerHeadFromCache(player);
        if (fromPlayerCache != null) {
            return CompletableFuture.completedFuture(fromPlayerCache);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                PlayerProfile newPlayerProfile = player.getPlayerProfile().update().get();

                if (newPlayerProfile == null || !newPlayerProfile.isComplete()) {
                    itemStack.setItemMeta(skullMeta);
                    return itemStack;
                }

                PLAYER_TEXTURES_CONCURRENT_HASH_MAP.putIfAbsent(player.getUniqueId(), newPlayerProfile.getTextures());
                skullMeta.setOwnerProfile(newPlayerProfile);
                itemStack.setItemMeta(skullMeta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemStack;
        }, PROFILE_EXECUTOR_SERVICE);
    }

    public static ItemStack getPlayerHeadFromCache(OfflinePlayer offlinePlayer) {
        PlayerTextures playerTextures = PLAYER_TEXTURES_CONCURRENT_HASH_MAP.getOrDefault(offlinePlayer.getUniqueId(), null);
        if (playerTextures == null) return null;

        PlayerProfile playerProfile = offlinePlayer.getPlayerProfile();
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(offlinePlayer.getName());

        playerProfile.setTextures(playerTextures);
        skullMeta.setOwnerProfile(playerProfile);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public static void getPlayerHead(OfflinePlayer player, Consumer<ItemStack> consumer) {
        getPlayerHead(player).thenAcceptAsync(consumer, runnable -> Bukkit.getScheduler().runTask(World16Utils.getInstance(), runnable));
    }

    public static void getPlayerHeads(List<OfflinePlayer> players, Consumer<Map<OfflinePlayer, ItemStack>> consumer) {
        // Get all CompletableFutures
        Map<OfflinePlayer, CompletableFuture<ItemStack>> completableFutures = new HashMap<>();
        for (OfflinePlayer player : players) {
            completableFutures.put(player, getPlayerHead(player));
        }

        Map<OfflinePlayer, ItemStack> itemStacks = new HashMap<>();
        CompletableFuture.allOf(completableFutures.values().toArray(new CompletableFuture[0])).thenAcceptAsync((v) -> {
            // Get all the ItemStacks then put them in the map
            for (Map.Entry<OfflinePlayer, CompletableFuture<ItemStack>> entry : completableFutures.entrySet()) {
                OfflinePlayer player = entry.getKey();
                CompletableFuture<ItemStack> completableFuture = entry.getValue();
                try {
                    ItemStack itemStack = completableFuture.get();
                    itemStacks.put(player, itemStack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Call the consumer with the itemStacks map
            consumer.accept(itemStacks);
        }, runnable -> Bukkit.getScheduler().runTask(World16Utils.getInstance(), runnable));
    }
}
