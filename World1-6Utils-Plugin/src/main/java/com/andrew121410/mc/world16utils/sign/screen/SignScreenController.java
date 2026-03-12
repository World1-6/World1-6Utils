package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.blocks.UniversalBlockUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Central controller for sign screen interactions.
 * Handles player focus mode, scroll/click controls, and event listeners automatically.
 * <p>
 * Register sign screens with {@link #registerScreen(Location, SignScreenEngine)} and
 * the controller handles everything else — focus mode entry/exit, scroll up/down,
 * button clicks, inventory locking, and cleanup on disconnect.
 * <p>
 * Example usage:
 * <pre>
 * SignScreenController controller = World16Utils.getInstance().getSignScreenController();
 * SignScreenEngine manager = new SignScreenEngine(plugin, "myScreen", signLocation, null, mySignScreen);
 * controller.registerScreen(signLocation, manager);
 * </pre>
 */
public class SignScreenController implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey toolKey;
    private final Map<Location, SignScreenEngine> screenMap = new HashMap<>();
    private final Map<UUID, SignScreenFocus> focusMap = new HashMap<>();

    public SignScreenController(JavaPlugin plugin) {
        this.plugin = plugin;
        this.toolKey = new NamespacedKey(plugin, "sign_screen_tool");
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    /**
     * Registers a sign screen at the given location.
     * When a player right-clicks this location, the controller will automatically
     * handle focus mode and controls.
     */
    public void registerScreen(Location location, SignScreenEngine SignScreenEngine) {
        this.screenMap.put(location, SignScreenEngine);
    }

    /**
     * Unregisters a sign screen at the given location.
     */
    public void unregisterScreen(Location location) {
        this.screenMap.remove(location);
    }

    /**
     * Gets the sign screen manager registered at the given location, or null if none.
     */
    public SignScreenEngine getScreen(Location location) {
        return this.screenMap.get(location);
    }

    /**
     * Checks if a player is currently in focus mode.
     */
    public boolean isPlayerFocused(Player player) {
        return this.focusMap.containsKey(player.getUniqueId());
    }

    /**
     * Gets the focus session for a player, or null if not focused.
     */
    public SignScreenFocus getFocus(Player player) {
        return this.focusMap.get(player.getUniqueId());
    }

    /**
     * Puts a player into focus mode for a specific sign screen.
     * Starts the tick loop, saves inventory, applies blindness, and gives control tools.
     */
    public void enterFocus(Player player, SignScreenEngine SignScreenEngine) {
        SignScreenEngine.tick(player);
        this.focusMap.put(player.getUniqueId(), new SignScreenFocus(player, SignScreenEngine, this.toolKey));
        SignScreenEngine.getSignScreen().onFocusEnter(SignScreenEngine, player);
    }

    /**
     * Removes a player from focus mode.
     * Stops the tick loop, restores inventory and potion effects.
     */
    public void exitFocus(Player player) {
        SignScreenFocus focus = this.focusMap.remove(player.getUniqueId());
        if (focus != null) {
            focus.getSignScreenEngine().setStop(true);
            focus.revert();
            focus.getSignScreenEngine().getSignScreen().onFocusExit(focus.getSignScreenEngine(), player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (block == null || action != Action.RIGHT_CLICK_BLOCK) return;

        SignScreenFocus focus = this.focusMap.get(player.getUniqueId());
        boolean isSign = UniversalBlockUtils.isSign(block) != null;

        // If focused and clicks a non-sign block, exit focus
        if (!isSign && focus != null) {
            event.setCancelled(true);
            exitFocus(player);
            return;
        }

        SignScreenEngine SignScreenEngine = this.screenMap.get(block.getLocation());
        if (SignScreenEngine == null) return;

        event.setCancelled(true);

        // Not focused yet -> enter focus mode
        if (focus == null) {
            enterFocus(player, SignScreenEngine);
            return;
        }

        // Focused on a different screen -> switch
        if (focus.getSignScreenEngine() != SignScreenEngine) {
            exitFocus(player);
            enterFocus(player, SignScreenEngine);
            return;
        }

        // Already focused on this screen -> handle controls
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String toolType = itemMeta != null ? itemMeta.getPersistentDataContainer().get(this.toolKey, PersistentDataType.STRING) : null;

        if (toolType != null) {
            switch (toolType) {
                case SignScreenFocus.TOOL_EXIT -> exitFocus(player);
                case SignScreenFocus.TOOL_SCROLL_UP -> SignScreenEngine.onScroll(player, true);
                case SignScreenFocus.TOOL_SCROLL_DOWN -> SignScreenEngine.onScroll(player, false);
            }
        } else {
            SignScreenEngine.onClick(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (this.focusMap.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SignScreenFocus focus = this.focusMap.remove(player.getUniqueId());
        if (focus != null) {
            focus.getSignScreenEngine().setStop(true);
            focus.revert();
            focus.getSignScreenEngine().getSignScreen().onFocusExit(focus.getSignScreenEngine(), player);
        }
    }

    public Map<Location, SignScreenEngine> getScreenMap() {
        return screenMap;
    }

    public Map<UUID, SignScreenFocus> getFocusMap() {
        return focusMap;
    }
}

