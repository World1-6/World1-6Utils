package com.Zrips.CMI.Containers;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CMIPlayerInventory {
    public enum CMIInventorySlot {
        Armor(),
        Helmet(),
        ChestPlate(),
        Pants(),
        Boots(),
        OffHand(),
        MainHand(),
        QuickBar(),
        PartInventory(),
        MainInventory(),
        CraftingIngredients(),
        CraftingResult();

        public List<Integer> getSlots() {
            return null;
        }

        public Integer getSlot() {
            return null;
        }
    }

    public ItemStack getItem(CMIInventorySlot paramCMIInventorySlot) {
        return null;
    }

    public List<ItemStack> getItems(CMIInventorySlot paramCMIInventorySlot) {
        return null;
    }
}