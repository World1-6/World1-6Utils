package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.entity.Player;

public interface ISignScreen {

    boolean onDoneConstructed(SignScreenEngine SignScreenEngine);

    boolean onButton(SignScreenEngine SignScreenEngine, Player player, SignLayout signLayout, SignPage signPage, int linePointer, int currentSide);

    boolean onPageBoundary(SignScreenEngine SignScreenEngine, Player player, boolean up);

    /**
     * Called every tick cycle by the SignScreenEngine's BukkitRunnable.
     * Implementations can use this to refresh sign content with live data.
     * If this method calls updateLayoutAndPage(), the tick loop will automatically
     * pick up the new content via the needsTextChanged flag.
     *
     * @param SignScreenEngine the manager running the tick
     * @return true if content was updated, false otherwise
     */
    default boolean onTick(SignScreenEngine SignScreenEngine) { return false; }

    /**
     * Called after the player scrolls (line change, side change, or page flip).
     * Fired after SignScreenEngine has already updated its internal pointer/page state.
     * Useful for refreshing displayed data when the player navigates to a different page.
     *
     * @param SignScreenEngine the manager
     * @param player the player who scrolled
     * @param up true if scrolled up, false if scrolled down
     */
    default void onScroll(SignScreenEngine SignScreenEngine, Player player, boolean up) {}

    /**
     * Called when a player enters focus mode on this sign screen.
     *
     * @param SignScreenEngine the manager
     * @param player the player entering focus
     */
    default void onFocusEnter(SignScreenEngine SignScreenEngine, Player player) {}

    /**
     * Called when a player exits focus mode on this sign screen.
     * Also called on disconnect, so the player may no longer be online.
     *
     * @param SignScreenEngine the manager
     * @param player the player exiting focus
     */
    default void onFocusExit(SignScreenEngine SignScreenEngine, Player player) {}
}
