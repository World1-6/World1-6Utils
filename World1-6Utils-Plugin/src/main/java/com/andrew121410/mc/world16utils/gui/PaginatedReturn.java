package com.andrew121410.mc.world16utils.gui;

import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;

import java.util.List;

public class PaginatedReturn {

    private boolean hasNextPage;
    private List<CloneableGUIButton> buttons;

    public PaginatedReturn(boolean hasNextPage, List<CloneableGUIButton> buttons) {
        this.hasNextPage = hasNextPage;
        this.buttons = buttons;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public List<CloneableGUIButton> getButtons() {
        return buttons;
    }
}
