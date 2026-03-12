package com.andrew121410.mc.world16utils.sign.screen;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.List;

/**
 * Defines a tool item given to the player during sign screen focus mode.
 *
 * @param toolType    unique string identifier stored in PersistentDataContainer (e.g. "scroll_up")
 * @param slot        inventory slot to place the tool in (0-8)
 * @param material    the item material
 * @param displayName the display name shown on the item
 */
public record SignScreenTool(String toolType, int slot, Material material, Component displayName) {

    /** Built-in tool type for scrolling down. */
    public static final String SCROLL_DOWN = "scroll_down";
    /** Built-in tool type for scrolling up. */
    public static final String SCROLL_UP = "scroll_up";
    /** Built-in tool type for exiting focus mode. */
    public static final String EXIT = "exit";

    /**
     * Returns the default set of tools: SCROLL DOWN (slot 0), SCROLL UP (slot 2), EXIT (slot 8).
     */
    public static List<SignScreenTool> defaults() {
        return List.of(
                new SignScreenTool(SCROLL_DOWN, 0, Material.GOLDEN_SWORD, Component.text("SCROLL DOWN", NamedTextColor.GOLD, TextDecoration.BOLD)),
                new SignScreenTool(SCROLL_UP, 2, Material.GOLDEN_SWORD, Component.text("SCROLL UP", NamedTextColor.GOLD, TextDecoration.BOLD)),
                new SignScreenTool(EXIT, 8, Material.BARRIER, Component.text("EXIT", NamedTextColor.RED, TextDecoration.BOLD))
        );
    }
}

