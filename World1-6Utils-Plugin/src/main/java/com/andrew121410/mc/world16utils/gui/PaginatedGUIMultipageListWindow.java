package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.NoEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.GUINextPageEvent;
import com.andrew121410.mc.world16utils.gui.buttons.events.pages.PageEventType;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaginatedGUIMultipageListWindow extends GUIWindow {

    private Component name;
    private int size = 54; // Default size of gui

    private Map<Integer, List<CloneableGUIButton>> pages = new HashMap<>();
    private int currentPage;

    private Function<Integer, List<CloneableGUIButton>> buttonProvider = null;
    private Consumer<GUINextPageEvent> pageEvent = null;

    public PaginatedGUIMultipageListWindow(Component name, Integer currentPage) {
        this.name = name;
        this.currentPage = currentPage != null ? currentPage : 0;
    }

    @Override
    public void onCreate(Player player) {
        handle(player);
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    private void handle(Player player) {
        List<CloneableGUIButton> bottomButtons = new ArrayList<>();

        // Call button provider to populate the page
        if (this.buttonProvider != null) {
            this.pages.putIfAbsent(this.currentPage, this.buttonProvider.apply(this.currentPage));
        }

        // Show previous page button if not on first page and previous page exists
        if (this.currentPage != 0 && this.pages.get(this.currentPage - 1) != null) {
            bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "&6Previous Page"), (guiClickEvent) -> {
                handlePageChange(player, guiClickEvent, this.currentPage - 1, PageEventType.PREV_PAGE);
            }));
        }

        // Show next page button if the next page exists
        if (this.pages.get(this.currentPage + 1) != null) {
            bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "&6Go to next page"), (guiClickEvent) -> {
                handlePageChange(player, guiClickEvent, this.currentPage + 1, PageEventType.NEXT_PAGE);
            }));
        }

        List<AbstractGUIButton> guiButtonList = new ArrayList<>();

        // Add the items to the gui
        if (this.pages.containsKey(this.currentPage)) {
            guiButtonList.addAll(this.pages.get(this.currentPage));
        }

        // Add the bottom buttons
        guiButtonList.addAll(bottomButtons);

        int realPageNumber = this.currentPage + 1;
        bottomButtons.add(new NoEventButton(49, InventoryUtils.createItem(Material.PAPER, realPageNumber <= 64 ? realPageNumber : 1, "&5Current Page", "&aCurrent Page: &6" + realPageNumber)));

        this.update(guiButtonList, this.name, this.size);
        if (!super.isFirst()) {
            this.refresh(player);
        }
    }

    private void handlePageChange(Player player, GUIClickEvent guiClickEvent, int newPage, PageEventType pageEventType) {
        // Call page event (before page change)
        if (this.pageEvent != null) {
            this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.currentPage, newPage, pageEventType, false));
        }

        // Change page number
        this.currentPage = newPage;

        // Handle the new page
        this.handle(player);

        // Call page event (after page change)
        if (this.pageEvent != null) {
            this.pageEvent.accept(new GUINextPageEvent(guiClickEvent, this.currentPage, null, pageEventType, true));
        }
    }

    @Override
    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<Integer, List<CloneableGUIButton>> getPages() {
        return pages;
    }

    public void setPages(Map<Integer, List<CloneableGUIButton>> pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Function<Integer, List<CloneableGUIButton>> getButtonProvider() {
        return buttonProvider;
    }

    public void setButtonProvider(Function<Integer, List<CloneableGUIButton>> buttonProvider) {
        this.buttonProvider = buttonProvider;
    }

    public Consumer<GUINextPageEvent> getPageEvent() {
        return pageEvent;
    }

    public void setPageEvent(Consumer<GUINextPageEvent> pageEvent) {
        this.pageEvent = pageEvent;
    }
}
