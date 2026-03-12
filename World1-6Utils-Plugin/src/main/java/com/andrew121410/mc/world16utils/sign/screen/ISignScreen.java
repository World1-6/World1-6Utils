package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.entity.Player;

import java.util.List;

public interface ISignScreen {

    boolean onDoneConstructed(SignScreenEngine signScreenEngine);

    boolean onButton(SignScreenEngine signScreenEngine, Player player, SignLayout signLayout, SignPage signPage, int linePointer, int currentSide);

    boolean onPageBoundary(SignScreenEngine signScreenEngine, Player player, boolean up);

    /**
     * Called every tick cycle by the SignScreenEngine's BukkitRunnable.
     * Implementations can use this to refresh sign content with live data.
     * If this method calls updateLayoutAndPage(), the tick loop will automatically
     * pick up the new content via the needsTextChanged flag.
     *
     * @param signScreenEngine the engine running the tick
     * @return true if content was updated, false otherwise
     */
    default boolean onTick(SignScreenEngine signScreenEngine) { return false; }

    /**
     * Called after the player scrolls (line change, side change, or page flip).
     * Fired after SignScreenEngine has already updated its internal pointer/page state.
     * Useful for refreshing displayed data when the player navigates to a different page.
     *
     * @param signScreenEngine the engine
     * @param player the player who scrolled
     * @param up true if scrolled up, false if scrolled down
     */
    default void onScroll(SignScreenEngine signScreenEngine, Player player, boolean up) {}

    /**
     * Called when a player enters focus mode on this sign screen.
     *
     * @param signScreenEngine the engine
     * @param player the player entering focus
     */
    default void onFocusEnter(SignScreenEngine signScreenEngine, Player player) {}

    /**
     * Called when a player exits focus mode on this sign screen.
     * Also called on disconnect, so the player may no longer be online.
     *
     * @param signScreenEngine the engine
     * @param player the player exiting focus
     */
    default void onFocusExit(SignScreenEngine signScreenEngine, Player player) {}

    /**
     * Returns the tools to give the player when entering focus mode.
     * Override to customize materials, names, slots, or add entirely new tools.
     *
     * @return list of tools to give the player
     */
    default List<SignScreenTool> getTools() { return SignScreenTool.defaults(); }

    /**
     * Called when the player uses a tool with a type not handled by the built-in controls
     * (i.e. not scroll_up, scroll_down, or exit). Use this to handle custom tools.
     *
     * @param signScreenEngine the engine
     * @param player the player who used the tool
     * @param toolType the tool type string from the PersistentDataContainer
     */
    default void onToolUse(SignScreenEngine signScreenEngine, Player player, String toolType) {}
}
