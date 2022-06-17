package com.andrew121410.mc.world16utils.player;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class PlayerUtils {

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static ItemStack getPlayerHead(OfflinePlayer player, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(lore);

        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }
}
