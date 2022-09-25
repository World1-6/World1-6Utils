package com.Zrips.CMI.Modules.Kits;

import com.Zrips.CMI.Containers.CMIPlayerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kit {
    private List<ItemStack> item = new ArrayList<>();
    private HashMap<CMIPlayerInventory.CMIInventorySlot, ItemStack> extraItems = new HashMap<>();

    public Kit(String paramString) {
    }

    public List<ItemStack> getItems() {
        return null;
    }

    public void addItem(ItemStack paramItemStack) {
    }

    public void setExtraItem(CMIPlayerInventory.CMIInventorySlot paramCMIInventorySlot, ItemStack paramItemStack) {
    }
}
