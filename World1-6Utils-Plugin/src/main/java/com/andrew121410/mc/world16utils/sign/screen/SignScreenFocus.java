package com.andrew121410.mc.world16utils.sign.screen;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Manages the "focus mode" state for a player interacting with a sign screen.
 * <p>
 * When constructed, saves the player's inventory and potion effects, applies blindness,
 * and gives the provided control tools. Call {@link #revert()} to restore the player's state.
 */
public class SignScreenFocus {

    private final Player player;
    private final SignScreenEngine signScreenEngine;
    private final NamespacedKey toolKey;

    private final ItemStack[] savedInventory;
    private final Collection<PotionEffect> savedPotionEffects;

    public SignScreenFocus(Player player, SignScreenEngine signScreenEngine, NamespacedKey toolKey, List<SignScreenTool> tools) {
        this.player = player;
        this.signScreenEngine = signScreenEngine;
        this.toolKey = toolKey;

        // Save player state (clone the array — getContents() may return the live backing array)
        this.savedInventory = player.getInventory().getContents().clone();
        this.savedPotionEffects = player.getActivePotionEffects();

        // Clear and apply focus effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, true, false));
        player.getInventory().clear();

        giveTools(tools);
    }

    private void giveTools(List<SignScreenTool> tools) {
        for (SignScreenTool tool : tools) {
            ItemStack item = new ItemStack(tool.material());
            ItemMeta meta = item.getItemMeta();
            meta.displayName(tool.displayName());
            meta.getPersistentDataContainer().set(this.toolKey, PersistentDataType.STRING, tool.toolType());
            item.setItemMeta(meta);
            this.player.getInventory().setItem(tool.slot(), item);
        }
    }

    /**
     * Restores the player's inventory and potion effects to their pre-focus state.
     */
    public void revert() {
        if (!this.player.isOnline()) {
            Logger.getLogger("SignScreen").warning("Cannot restore inventory for " + this.player.getName() + " — player is offline.");
            return;
        }

        this.player.getInventory().clear();

        for (PotionEffect effect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(effect.getType());
        }

        this.player.addPotionEffects(this.savedPotionEffects);
        this.player.getInventory().setContents(this.savedInventory);
    }

    public Player getPlayer() {
        return player;
    }

    public SignScreenEngine getSignScreenEngine() {
        return signScreenEngine;
    }
}
