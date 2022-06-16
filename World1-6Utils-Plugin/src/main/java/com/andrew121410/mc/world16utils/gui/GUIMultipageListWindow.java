package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.NoEventButton;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;

public class GUIMultipageListWindow extends AdvanceGUIWindow {

    private final int itemsPerPage;
    private final List<List<GUIButton>> pages;

    private int page = 0;

    public GUIMultipageListWindow(List<GUIButton> buttons, int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        this.pages = Lists.partition(buttons, itemsPerPage);

        for (List<GUIButton> page : this.pages) {
            determineSlotNumbers(page);
        }
    }

    @Override
    public void onCreate(Player player) {
        List<GUIButton> bottomButtons = new ArrayList<>();

        if (page != 0 && Utils.indexExists(pages, page - 1)) {
            bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "Previous Page"), (guiClickEvent) -> {
                this.page--;
                this.onCreate(player);
            }));
        }

        if (pages.size() >= 2 && Utils.indexExists(pages, page + 1)) {
            bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "Go to next page"), (guiClickEvent) -> {
                this.page++;
                this.onCreate(player);
            }));
        }

        List<GUIButton> guiButtonList = new ArrayList<>(pages.get(page));

        bottomButtons.add(new NoEventButton(49, InventoryUtils.createItem(Material.PAPER, 1, "Current Page", "&aCurrent Page: &6" + this.page)));
        guiButtonList.addAll(bottomButtons);

        this.update(guiButtonList, "Last Join", 54);
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
}
