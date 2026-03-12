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
 * SignScreenEngine engine = new SignScreenEngine(plugin, "myScreen", signLocation, null, mySignScreen);
 * controller.registerScreen(signLocation, engine);
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
    public void registerScreen(Location location, SignScreenEngine signScreenEngine) {
        this.screenMap.put(location, signScreenEngine);
    }

    /**
     * Unregisters a sign screen at the given location.
     */
    public void unregisterScreen(Location location) {
        this.screenMap.remove(location);
    }

    /**
     * Gets the sign screen engine registered at the given location, or null if none.
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
     * Tools are provided by {@link ISignScreen#getTools()}.
     */
    public void enterFocus(Player player, SignScreenEngine signScreenEngine) {
        signScreenEngine.tick(player);
        var tools = signScreenEngine.getSignScreen().getTools();
        this.focusMap.put(player.getUniqueId(), new SignScreenFocus(player, signScreenEngine, this.toolKey, tools));
        signScreenEngine.getSignScreen().onFocusEnter(signScreenEngine, player);
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

        SignScreenEngine signScreenEngine = this.screenMap.get(block.getLocation());
        if (signScreenEngine == null) return;

        event.setCancelled(true);

        // Not focused yet -> enter focus mode
        if (focus == null) {
            enterFocus(player, signScreenEngine);
            return;
        }

        // Focused on a different screen -> switch
        if (focus.getSignScreenEngine() != signScreenEngine) {
            exitFocus(player);
            enterFocus(player, signScreenEngine);
            return;
        }

        // Already focused on this screen -> handle controls
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String toolType = itemMeta != null ? itemMeta.getPersistentDataContainer().get(this.toolKey, PersistentDataType.STRING) : null;

        if (toolType != null) {
            switch (toolType) {
                case SignScreenTool.EXIT -> exitFocus(player);
                case SignScreenTool.SCROLL_UP -> signScreenEngine.onScroll(player, true);
                case SignScreenTool.SCROLL_DOWN -> signScreenEngine.onScroll(player, false);
                default -> signScreenEngine.getSignScreen().onToolUse(signScreenEngine, player, toolType);
            }
        } else {
            signScreenEngine.onClick(player);
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
