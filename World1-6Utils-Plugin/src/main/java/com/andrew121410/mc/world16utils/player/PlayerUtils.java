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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtils {

    public static final Map<UUID, PlayerTextures> playerTexturesMap = new HashMap<>();

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static ItemStack getPlayerHead(OfflinePlayer player, String displayName, String... lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(Arrays.asList(lore));

        // Player heads don't work with bedrock players
        if (Bukkit.getPluginManager().getPlugin("floodgate") != null) {
            if (FloodgateApi.getInstance().isFloodgateId(player.getUniqueId())) {
                itemStack.setItemMeta(skullMeta);
                return itemStack;
            }
        }

        PlayerProfile playerProfile = player.getPlayerProfile();
        if (!playerProfile.isComplete() && !playerTexturesMap.containsKey(player.getUniqueId())) {
            playerProfile.update().thenAcceptAsync(updatedProfile -> {
                if (updatedProfile == null || !updatedProfile.isComplete()) return;
                playerTexturesMap.putIfAbsent(player.getUniqueId(), updatedProfile.getTextures());
                skullMeta.setOwnerProfile(updatedProfile);
                itemStack.setItemMeta(skullMeta);
            }, runnable -> Bukkit.getScheduler().runTask(World16Utils.getInstance(), runnable));
        } else {
            PlayerTextures playerTextures;
            // If the player is online then it's guaranteed to have textures in its PlayerProfile, so we can use it
            if (player.isOnline()) {
                playerTextures = player.getPlayerProfile().getTextures();
            } else {
                playerTextures = playerTexturesMap.getOrDefault(player.getUniqueId(), null);
            }
            playerProfile.setTextures(playerTextures);
            skullMeta.setOwnerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }
}
