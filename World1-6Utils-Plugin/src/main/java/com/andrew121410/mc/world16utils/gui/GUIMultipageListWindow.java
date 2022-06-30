package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ChatResponseButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.NoEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.GUINextPageEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.PageEventType;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIMultipageListWindow extends GUIWindow {

    private String name;
    private int slots = 54;

    private int itemsPerPage;
    private List<List<GUIButton>> pages;

    private int page = 0;

    private Consumer<GUINextPageEvent> pageEvent = null;

    public GUIMultipageListWindow(String name, List<GUIButton> buttons, Integer itemsPerPage) {
        this.name = name;

        this.itemsPerPage = itemsPerPage != null ? itemsPerPage : 45;
        this.pages = Lists.partition(buttons, this.itemsPerPage);

        // Set the correct slot numbers for the buttons.
        for (List<GUIButton> page : this.pages) {
            determineSlotNumbers(page);
        }
    }

    @Override
    public void onCreate(Player player) {
        List<GUIButton> bottomButtons = new ArrayList<>();

        if (page != 0 && Utils.indexExists(pages, page - 1)) {
            bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "Previous Page"), (guiClickEvent) -> {
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, this.page - 1, PageEventType.PREV_PAGE, false));
                this.page--;
                this.onCreate(player);
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, null, PageEventType.PREV_PAGE, true));
            }));
        }

        if (pages.size() >= 2 && Utils.indexExists(pages, page + 1)) {
            bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "Go to next page"), (guiClickEvent) -> {
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, this.page + 1, PageEventType.NEXT_PAGE, false));
                this.page++;
                this.onCreate(player);
                if (this.pageEvent != null)
                    this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.page, null, PageEventType.NEXT_PAGE, true));
            }));
        }

        List<GUIButton> guiButtonList = new ArrayList<>(pages.get(page));

        bottomButtons.add(new NoEventButton(49, InventoryUtils.createItem(Material.PAPER, 1, "Current Page", "&aCurrent Page: &6" + this.page)));
        bottomButtons.add(new ChatResponseButton(48, InventoryUtils.createItem(Material.COMPASS, 1, "Search", ""), null, null, (player1, string) -> {
            List<GUIButton> sortByContainsList = new ArrayList<>();

            List<GUIButton> toSort = new ArrayList<>();
            for (List<GUIButton> guiButtons : this.pages) {
                toSort.addAll(guiButtons);
            }

            // Loop through all the buttons and add them to a list if they contain the search string.
            for (GUIButton guiButton : toSort) {
                ItemStack itemStack = guiButton.getItemStack();
                if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                    if (itemStack.getItemMeta().getDisplayName().toLowerCase().contains(string.toLowerCase())) {
                        sortByContainsList.add(guiButton);
                    }
                }
            }

            if (sortByContainsList.size() == 0) {
                player1.sendMessage("Nothing was found with the search string of: " + string);
                return;
            }

            List<List<GUIButton>> thePages = Lists.partition(sortByContainsList, itemsPerPage);
            // For now only support one page for search.
            sortByContainsList = thePages.get(0);

            // Determine the slot numbers for the buttons.
            determineSlotNumbers(sortByContainsList);

            // Update the GUI and open it.
            this.update(sortByContainsList, this.name, this.slots);
            this.open(player1);
        }));
        guiButtonList.addAll(bottomButtons);

        this.update(guiButtonList, this.name, this.slots);
        if (!super.isFirst()) {
            this.refresh(player);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    private void determineSlotNumbers(List<GUIButton> guiButtonList) {
        int i = 0;

        for (GUIButton guiButton : guiButtonList) {
            guiButton.setSlot(i);

            if (i < this.itemsPerPage) {
                i++;
            } else {
                i = 0;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public List<List<GUIButton>> getPages() {
        return pages;
    }

    public void setPages(List<List<GUIButton>> pages) {
        this.pages = pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Consumer<GUINextPageEvent> getPageEvent() {
        return pageEvent;
    }

    public void setPageEvent(Consumer<GUINextPageEvent> pageEvent) {
        this.pageEvent = pageEvent;
    }
}