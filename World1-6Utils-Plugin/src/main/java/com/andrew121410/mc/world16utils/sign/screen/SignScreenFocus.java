package com.andrew121410.mc.world16utils.sign.screen;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Manages the "focus mode" state for a player interacting with a sign screen.
 * <p>
 * When constructed, saves the player's inventory and potion effects, applies blindness,
 * and gives scroll/click/exit control tools. Call {@link #revert()} to restore the player's state.
 */
public class SignScreenFocus {

    /**
     * The tool type values stored in the PersistentDataContainer.
     */
    public static final String TOOL_SCROLL_DOWN = "scroll_down";
    public static final String TOOL_SCROLL_UP = "scroll_up";
    public static final String TOOL_EXIT = "exit";

    private final Player player;
    private final SignScreenEngine SignScreenEngine;
    private final NamespacedKey toolKey;

    private final ItemStack[] savedInventory;
    private final Collection<PotionEffect> savedPotionEffects;

    public SignScreenFocus(Player player, SignScreenEngine SignScreenEngine, NamespacedKey toolKey) {
        this.player = player;
        this.SignScreenEngine = SignScreenEngine;
        this.toolKey = toolKey;

        // Save player state
        this.savedInventory = player.getInventory().getContents();
        this.savedPotionEffects = player.getActivePotionEffects();

        // Clear and apply focus effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, true, false));
        player.getInventory().clear();

        giveTools();
    }

    private void giveTools() {
        this.player.getInventory().setItem(0, createTool(Material.GOLDEN_SWORD, TOOL_SCROLL_DOWN, Component.text("SCROLL DOWN", NamedTextColor.GOLD, TextDecoration.BOLD)));
        this.player.getInventory().setItem(2, createTool(Material.GOLDEN_SWORD, TOOL_SCROLL_UP, Component.text("SCROLL UP", NamedTextColor.GOLD, TextDecoration.BOLD)));
        this.player.getInventory().setItem(8, createTool(Material.BARRIER, TOOL_EXIT, Component.text("EXIT", NamedTextColor.RED, TextDecoration.BOLD)));
    }

    private ItemStack createTool(Material material, String toolType, Component displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        meta.getPersistentDataContainer().set(this.toolKey, PersistentDataType.STRING, toolType);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Restores the player's inventory and potion effects to their pre-focus state.
     */
    public void revert() {
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
        return SignScreenEngine;
    }
}

