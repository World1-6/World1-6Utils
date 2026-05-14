# GUI System

A from-scratch inventory GUI framework for Paper plugins. Handles slot management, pagination, click routing, and chat input — no external GUI library needed.

## Class Overview

```
AbstractGUIWindow          — base for all windows
└── GUIWindow              — slot-mapped button grid
    ├── MiddleGUIWindow    — auto-centers buttons horizontally
    ├── GUIMultipageListWindow        — paginated list (buttons provided upfront)
    └── PaginatedGUIMultipageListWindow — paginated list (buttons loaded per-page, supports async)

AbstractGUIButton          — base for all buttons
└── CloneableGUIButton     — cloneable, no-op click (required for paginated lists)
    ├── NoEventButton      — decoration / spacer
    ├── ClickEventButton   — fires a Consumer<GUIClickEvent> on click
    ├── ChatResponseButton — closes GUI, waits for chat input, fires BiConsumer<Player, String>
    └── LoreShifterButton  — cycles through a list of options in the item lore
```

---

## Windows

### `AbstractGUIWindow`
Base class. Implement these four methods:

| Method | When called |
|---|---|
| `onCreate(Player)` | First `open()` only — use for initialization |
| `populateInventory(Player)` | Every `open()` and `refresh()` — puts items into slots |
| `onSlotClicked(InventoryClickEvent)` | Every click inside the inventory |
| `onClose(InventoryCloseEvent)` | When the player closes the GUI |

`open(Player)` — opens the inventory. Calls `onCreate` on the first call, then always calls `populateInventory`.  
`refresh(Player)` — clears and repopulates without calling `onCreate` again.

---

### `GUIWindow`
Manages a `Map<Integer, AbstractGUIButton>` keyed by slot number. Call `update()` in `onCreate` to set everything up.

```java
update(List<AbstractGUIButton> buttons, Component name, Integer slotCount)
```

Each button's `slot` field determines which inventory slot it occupies. Passing `null` for name or slotCount keeps the existing value.

---

### `MiddleGUIWindow`
Extends `GUIWindow`. Overrides `update()` to automatically calculate slot positions so all buttons are centered in a single row. Size is determined by button count (27 slots for ≤3 buttons, grows in 9-slot increments).

```java
// Default spacing of 2 between buttons
new MiddleGUIWindow() { ... }

// Custom spacing
new MiddleGUIWindow(3) { ... }
```

Only `onCreate` and `onClose` need to be implemented. Call `this.update(buttons, title, null)` inside `onCreate`.

**Example:**
```java
MiddleGUIWindow gui = new MiddleGUIWindow() {
    @Override
    public void onCreate(Player player) {
        List<AbstractGUIButton> buttons = new ArrayList<>();
        buttons.add(new ClickEventButton(0, itemA, event -> { ... }));
        buttons.add(new ClickEventButton(0, itemB, event -> { ... }));
        // Slots are ignored — MiddleGUIWindow assigns them automatically
        this.update(buttons, Component.text("Title"), null);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {}
};
gui.open(player);
```

---

### `GUIMultipageListWindow`
Takes the full button list upfront and handles pagination automatically. Defaults to 45 items per page in a 54-slot inventory. Built-in prev/next navigation, page indicator, and search.

**Reserved bottom slots:** 45 (prev), 48 (search/return), 49 (page indicator), 53 (next).  
**Available for custom buttons:** 46, 47, 50, 51, 52.

```java
// All buttons provided at construction time
GUIMultipageListWindow gui = new GUIMultipageListWindow(title, buttons);

// Custom items per page
GUIMultipageListWindow gui = new GUIMultipageListWindow(title, buttons, 18);

// Add a custom button to the bottom row
gui.getCustomBottomButtons().add(new ClickEventButton(47, item, event -> { ... }));

gui.open(player);
```

Buttons in the list must be `CloneableGUIButton` (or a subclass). Their slot values are reassigned automatically by the pagination system — you don't need to set them.

---

### `PaginatedGUIMultipageListWindow`
Loads buttons on demand via a provider function called per page. Supports async loading and page caching. Use this when the button list is large, expensive to compute, or requires async work (e.g. fetching player heads).

```java
PaginatedGUIMultipageListWindow gui = new PaginatedGUIMultipageListWindow(title, 0, true, true);
// args: title, startPage, cacheMode, asyncMode

gui.setButtonProvider(pageNumber -> {
    if (asyncMode) {
        // Kick off async work, call setPageDone when ready
        fetchDataAsync(pageNumber, data -> {
            gui.setPageDone(pageNumber, new PaginatedReturn(hasNext, hasPrev, buttons));
        });
        return null; // Return null when using async — setPageDone handles the update
    }

    // Sync: return PaginatedReturn directly
    return new PaginatedReturn(hasNext, hasPrev, buttons);
});

// Optional: add custom bottom buttons
gui.getCustomBottomButtons().add(new ClickEventButton(50, item, event -> { ... }));

gui.open(player);
```

`PaginatedReturn(boolean hasNextPage, boolean hasPreviousPage, List<CloneableGUIButton> buttons)`

---

## Buttons

### `ClickEventButton`
Fires a `Consumer<GUIClickEvent>` on any click.

```java
new ClickEventButton(slot, itemStack, event -> {
    Player player = (Player) event.getEvent().getWhoClicked();
    // do something
});
```

### `ChatResponseButton`
Closes the inventory and waits for the player to type in chat. Fires a `BiConsumer<Player, String>` with the response.

```java
new ChatResponseButton(slot, itemStack, titleComponent, subtitleComponent, (player, input) -> {
    // input is what the player typed
});
```

Pass `(Component) null` for title/subtitle if you don't want a title shown. Note: use the `Component` overload explicitly to avoid ambiguity with the `String` overload.

### `NoEventButton`
Does nothing on click. Use for decorative items or spacers.

```java
new NoEventButton(slot, itemStack);
```

### `LoreShifterButton`
Cycles through a list of options displayed in the item lore. The selected option is highlighted with a `>` prefix.

```java
List<Component> options = List.of(
    Component.text("Option A"),
    Component.text("Option B"),
    Component.text("Option C")
);

new LoreShifterButton(slot, itemStack, options, needToConfirm, (event, selectedIndex) -> {
    // selectedIndex is the index of the currently selected option
});
```

`needToConfirm`:
- `true` — left-click cycles, right-click confirms and fires the callback
- `false` — left-click cycles and immediately fires the callback

Optional extra lore shown above the options:
```java
new LoreShifterButton(slot, itemStack, beforeLore, options, needToConfirm, callback);
```

### `CloneableGUIButton`
Base class for all buttons used in paginated lists. Subclass this to attach extra data to a button (e.g. a timestamp for sorting) while keeping the click behavior from `ClickEventButton`.

```java
class MyButton extends ClickEventButton {
    private final long timestamp;

    public MyButton(long timestamp, int slot, ItemStack item, Consumer<GUIClickEvent> consumer) {
        super(slot, item, consumer);
        this.timestamp = timestamp;
    }
}
```

---

## Events

### `GUIClickEvent`
Passed to every button's `onClick`. Wraps the original `InventoryClickEvent` and includes a reference to the window.

```java
event.getEvent()     // InventoryClickEvent
event.getGuiWindow() // AbstractGUIWindow
```

### `GUINextPageEvent` / `PageEventType`
Fired by paginated windows when the player navigates pages. Set a listener with `gui.setPageEvent(Consumer<GUINextPageEvent>)`.

`PageEventType`: `NEXT_PAGE`, `PREV_PAGE`, `PAGE_CREATED`  
`event.isAfterPageCreation()` — whether the event fires before or after the page is rendered.
