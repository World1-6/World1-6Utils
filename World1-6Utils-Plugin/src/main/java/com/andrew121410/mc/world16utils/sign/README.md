# Sign Screen System

A mini "operating system" framework for Minecraft signs. Lets you build interactive, navigable, multi-page UIs on physical sign blocks — complete with a blinking cursor, scroll controls, button presses, and automatic focus mode.

## Package Structure

```
sign/
├── SignCache.java                  # Snapshot of a sign's 4 lines (Component text)
└── screen/
    ├── ISignScreen.java            # Interface — your sign OS implements this
    ├── SignScreenEngine.java      # Core engine — tick loop, pointer, navigation
    ├── SignScreenController.java   # Control API — focus mode, event listeners, registration
    ├── SignScreenFocus.java        # Focus mode state — inventory save/restore, control tools
    └── pages/
        ├── SignLayout.java         # Groups pages together, tracks parent layout for back-nav
        ├── SignPage.java           # One screen of content (4 sign lines)
        └── SignLinePattern.java    # Cursor-stop positions for a single line
```

## Quick Start

### 1. Implement `ISignScreen`

This is your sign OS. It defines what content to show and what happens when the player presses buttons.

```java
public class MySignOS implements ISignScreen {

    @Override
    public boolean onDoneConstructed(SignScreenEngine manager) {
        // Build your initial layout and show it
        SignLayout layout = new SignLayout("Main", null);
        SignPage page = new SignPage("HomePage", null, 0, 0, 3, null);
        page.setLine(0, Translate.colorc("&6My Sign OS"));
        page.setLine(1, Translate.colorc("-Option A"));
        page.setLine(2, Translate.colorc("-Option B"));
        layout.addSignPage(page);
        manager.updateLayoutAndPage(layout, 0);
        return true;
    }

    @Override
    public boolean onButton(SignScreenEngine manager, Player player,
                            SignLayout layout, SignPage page,
                            int pointerLine, int currentSide) {
        // Player pressed the button while the cursor is on this line/side
        if (pointerLine == 1 && currentSide == 1) {
            player.sendMessage("You picked Option A!");
            return true;
        }
        return false;
    }

    @Override
    public boolean onPageBoundary(SignScreenEngine manager, Player player, boolean up) {
        // Called when scrolling past the first/last page and there's nowhere to go.
        // Return true if you handled it (e.g. wrapped around), false for the default error message.
        return false;
    }
}
```

### 2. Register it with the controller

```java
// In your plugin's onEnable or wherever you set up the sign screen:
SignScreenController controller = World16Utils.getInstance().getSignScreenController();

Location signLocation = /* your sign block location */;
SignScreenEngine manager = new SignScreenEngine(this, "myScreen", signLocation, null, new MySignOS());

controller.registerScreen(signLocation, manager);
// That's it. Focus mode, scroll/click controls, inventory locking, and quit cleanup
// are all handled automatically by the controller.
```

### 3. Unregister when done

```java
controller.unregisterScreen(signLocation);
```

## How It Works

### Player Interaction Flow

1. **Player right-clicks a registered sign** → enters **focus mode** (inventory saved, blindness applied, control tools given)
2. **Player holds SCROLL UP/DOWN tool and right-clicks the sign** → cursor moves up/down through lines, sides, and pages
3. **Player right-clicks the sign with empty hand** → triggers `onButton()` on your `ISignScreen`
4. **Player holds EXIT tool and right-clicks** → exits focus mode (inventory restored, ticker stopped)
5. **Player right-clicks a non-sign block while focused** → also exits focus mode
6. **Player disconnects while focused** → automatic cleanup

### The Tick Loop

`SignScreenEngine` runs a `BukkitRunnable` timer that:
1. Calls `ISignScreen.onTick()` — lets your implementation push fresh data
2. If content changed (`needsTextChanged`): writes the new sign text and resets the cursor
3. If the cursor line changed (`needsLineChanged`): moves the cursor to the new position
4. Otherwise: blinks the `>` cursor on and off (alternates between showing and hiding it)

The default tick speed is `10L` (10 ticks = 0.5 seconds). Pass a custom value to `SignScreenEngine`'s constructor to change it.

## Key Classes Explained

### `SignCache`

A simple snapshot of a sign's 4 lines as Adventure `Component` objects. Used internally to apply text to the physical sign and to save/restore the cursor line's content for blinking.

- `fromSign(Sign)` — reads the 4 lines from a sign
- `update(Sign)` — writes the 4 lines to a sign and calls `sign.update()`
- `getLine(int)` / `setLine(int, Component)` — access individual lines

### `ISignScreen`

The interface your sign OS implements. All callbacks receive the `SignScreenEngine` so you can call `updateLayoutAndPage()` to change what's displayed.

| Method | When it's called |
|---|---|
| `onDoneConstructed(manager)` | Once, right after the `SignScreenEngine` is created. Set up your initial layout here. |
| `onButton(manager, player, layout, page, pointerLine, currentSide)` | When the player right-clicks with an empty hand (the "select" action). |
| `onPageBoundary(manager, player, up)` | When scrolling past the first or last page with no next/previous page available. Return `true` if you handled it (e.g. navigated to a parent layout). |
| `onTick(manager)` | *(default)* Every tick cycle. Override to push live-updating data. |
| `onScroll(manager, player, up)` | *(default)* After a successful scroll. Override to react to navigation. |
| `onFocusEnter(manager, player)` | *(default)* When a player enters focus mode on this sign screen. |
| `onFocusExit(manager, player)` | *(default)* When a player exits focus mode. Also fires on disconnect, so the player may no longer be online. |
| `getTools()` | *(default)* Returns the list of `SignScreenTool`s to give the player. Override to customize or add tools. |
| `onToolUse(engine, player, toolType)` | *(default)* When the player uses a custom tool (not scroll_up/scroll_down/exit). |

### `SignScreenEngine`

The core engine. Manages the pointer position, tick loop, and sign updates.

**Key state:**
- `currentLayout` / `currentPage` — what's currently displayed
- `pointerLine` — which line (0–3) the cursor is on
- `currentSide` — which cursor-stop position on that line (for lines with multiple stops)
- `pointerLineOffset` — the character index where the `>` cursor is inserted

**Key methods:**
- `updateLayoutAndPage(SignLayout, int)` — switch to a new layout and page (sets `needsTextChanged = true`)
- `onClick(Player)` — routes to `ISignScreen.onButton()`
- `onScroll(Player, boolean)` — moves the cursor or changes pages
- `tick()` / `tick(Player)` — starts the tick loop if not already running
- `setStop(true)` — signals the tick loop to stop gracefully on the next cycle (waits for cursor to reset)
- `forceStop()` — immediately cancels the tick loop (use for plugin disable or sign destruction)

The tick loop re-fetches the `Sign` block state each tick, so it handles chunk unloads and sign destruction gracefully — the ticker auto-stops if the sign disappears.

### `SignScreenController`

The central control API. Handles all the boilerplate that every plugin would otherwise need to write:

- **Screen registration**: `registerScreen(Location, SignScreenEngine)` / `unregisterScreen(Location)`
- **Focus mode**: `enterFocus(Player, SignScreenEngine)` / `exitFocus(Player)`
- **Event listeners** (built-in):
  - `PlayerInteractEvent` — enter focus, scroll, click, exit
  - `InventoryClickEvent` — blocks inventory manipulation during focus
  - `PlayerQuitEvent` — cleanup on disconnect

Items are identified using **PersistentDataContainer** (not display names), so they can't be spoofed by renaming.

### `SignScreenTool`

A record that defines a single tool item given to the player during focus mode.

```java
public record SignScreenTool(String toolType, int slot, Material material, Component displayName)
```

- `toolType` — unique string identifier stored in PDC (e.g. `"scroll_up"`, `"my_custom_action"`)
- `slot` — inventory slot (0–8)
- `material` — item material
- `displayName` — display name Component

Built-in constants: `SignScreenTool.SCROLL_DOWN`, `SignScreenTool.SCROLL_UP`, `SignScreenTool.EXIT`

Call `SignScreenTool.defaults()` for the standard 3-tool set.

### `SignScreenFocus`

Manages a single player's focus session:
- Saves and restores inventory contents and potion effects
- Applies blindness during focus
- Gives tools defined by `ISignScreen.getTools()` (defaults: SCROLL DOWN slot 0, SCROLL UP slot 2, EXIT slot 8)

### `SignLayout`

Groups `SignPage`s together into a navigable collection. Think of it as a "menu" or "view".

- `name` — identifies this layout (e.g. `"SettingsMain"`)
- `parentLayout` — the name of the layout to navigate back to when scrolling up past the first page
- `addSignPage(SignPage)` — adds a page (auto-assigns page numbers starting from 0)
- `getNextPage(int)` / `getPreviousPage(int)` — navigate between pages within the layout

### `SignPage`

Represents one 4-line screen of content.

**Constructor:** `SignPage(name, backPage, startLine, min, max, pattern)`

| Parameter | Meaning |
|---|---|
| `name` | Identifier for this page |
| `backPage` | Name of the previous page (for reference, not used by the engine directly) |
| `startLine` | Which line the cursor starts on when entering this page (usually `0`) |
| `min` | Minimum navigable line number (usually `0`) |
| `max` | Maximum navigable line number (usually `3`) |
| `pattern` | Cursor-stop pattern array — see [Cursor Patterns](#cursor-patterns). `null` defaults to `{"*","*","*","*"}` |

Set content with `setLine(int lineNumber, Component text)`.

### `SignLinePattern`

Defines where the cursor can stop on a single line. Created automatically from the pattern string.

- `"*"` — one cursor stop at position 0 (default for most lines)
- `"**"` — two cursor stops at positions 0 and 1
- `"#*"` — one cursor stop at position 1 (skips position 0, e.g. for a title line with `^` prefix)

The cursor-stop positions are called **sides** (numbered starting from 1).

## Cursor Patterns

Each line has a pattern string that defines where the `>` cursor can appear. The `*` character marks a cursor-stop position, and the character's index in the string determines where the `>` is inserted.

**Default pattern:** `{"*", "*", "*", "*"}` — one stop per line, cursor appears at the beginning.

**Example — title line that can't be selected:**
```java
// Use startLine=1 and min=1 to skip line 0
SignPage page = new SignPage("MyPage", null, 1, 1, 3, null);
page.setLine(0, Translate.colorc("^&6Title"));  // ^ just visually indicates "header"
page.setLine(1, Translate.colorc("-Option 1"));  // cursor can land here
page.setLine(2, Translate.colorc("-Option 2"));
page.setLine(3, Translate.colorc("-Option 3"));
```

## Multi-Page Navigation

When scrolling past the last line of a page, the engine automatically moves to the next page in the layout (if one exists). When scrolling past the first line, it moves to the previous page.

If there's no next/previous page, `ISignScreen.onPageBoundary()` is called. Use this to navigate to a parent layout:

```java
@Override
public boolean onPageBoundary(SignScreenEngine manager, Player player, boolean up) {
    if (up) {
        SignLayout layout = manager.getCurrentLayout();
        String parent = layout.getParentLayout();
        if (parent != null) {
            // Navigate back to the parent layout
            goToLayout(manager, parent, player);
            return true; // we handled it, don't show the error message
        }
    }
    return false;
}
```

## Live-Updating Data

Override `onTick()` to push fresh content every tick cycle:

```java
@Override
public boolean onTick(SignScreenEngine manager) {
    if (this.currentMenu == MY_LIVE_VIEW) {
        // Rebuild the page with current data
        SignLayout layout = new SignLayout("Live", null);
        SignPage page = new SignPage("LiveData", null, 0, 0, 3, null);
        page.setLine(0, Translate.colorc("&6Temperature"));
        page.setLine(1, Translate.colorc(getTemperature() + "°C"));
        layout.addSignPage(page);
        manager.updateLayoutAndPage(layout, 0);
        return true;
    }
    return false;
}
```

## Custom Tools

Override `getTools()` in your `ISignScreen` to customize tool materials, names, slots, or add entirely new tools:

```java
@Override
public List<SignScreenTool> getTools() {
    return List.of(
        // Customize the built-in tools
        new SignScreenTool(SignScreenTool.SCROLL_DOWN, 0, Material.ARROW, Component.text("DOWN", NamedTextColor.GREEN)),
        new SignScreenTool(SignScreenTool.SCROLL_UP, 2, Material.ARROW, Component.text("UP", NamedTextColor.GREEN)),
        new SignScreenTool(SignScreenTool.EXIT, 8, Material.BARRIER, Component.text("QUIT", NamedTextColor.RED)),
        // Add a custom tool
        new SignScreenTool("reset", 4, Material.TNT, Component.text("RESET", NamedTextColor.YELLOW, TextDecoration.BOLD))
    );
}

@Override
public void onToolUse(SignScreenEngine engine, Player player, String toolType) {
    // Handle custom tool types (built-in types are handled automatically)
    if (toolType.equals("reset")) {
        player.sendMessage("Reset triggered!");
    }
}
```

## Terminology Notes

| Code Name | Meaning |
|---|---|
| `side` (in SignLinePattern) | **Cursor stop** — a position on a line where the `>` cursor can land (numbered starting from 1) |
| `pointerLineOffset` | **Cursor character index** — where in the line string the `>` is inserted |
| `onButton()` | **On select** — the player "pressed the button" by right-clicking with an empty hand |
| `parentLayout` (in SignLayout) | The name of the layout to navigate back to when scrolling up past the first page |
| `onPageBoundary()` (in ISignScreen) | Called when there's no next/previous page to scroll to |

## Full Example (Fire Alarm Plugin Pattern)

See `World16FireAlarms` for a complete real-world implementation that uses:
- Multiple layouts with parent-layout back-navigation
- Menu state tracking via an enum
- Button actions that trigger game logic
- Popup alerts pushed externally via `sendPopup()`

