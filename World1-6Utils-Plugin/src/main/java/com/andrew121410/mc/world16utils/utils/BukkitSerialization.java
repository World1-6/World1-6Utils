package com.andrew121410.mc.world16utils.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// https://gist.github.com/graywolf336/8153678
// https://discord.com/channels/289587909051416579/555462289851940864/1225393174415937608
public class BukkitSerialization {
    public static String serializeItemStack(ItemStack itemStack) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput output = new DataOutputStream(outputStream);

            if (itemStack == null || itemStack.getType().isAir()) {
                output.writeInt(0);
                return Base64Coder.encodeLines(outputStream.toByteArray());
            }

            byte[] bytes = itemStack.serializeAsBytes();
            output.writeInt(bytes.length);
            output.write(bytes);
            return Base64Coder.encodeLines(outputStream.toByteArray()); // Base64 encoding is not strictly needed
        } catch (IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    public static ItemStack deserializeItemStack(String data) throws IOException {
        byte[] bytes = Base64Coder.decodeLines(data); // Base64 encoding is not strictly needed
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            DataInputStream input = new DataInputStream(inputStream);

            int length = input.readInt();
            if (length == 0) {
                return null;
            }

            byte[] itemBytes = new byte[length];
            input.read(itemBytes);
            return ItemStack.deserializeBytes(itemBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

    public static String serializeWithList(List<ItemStack> items) {
        return serialize(items.toArray(new ItemStack[0]));
    }

    public static String serialize(ItemStack[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput output = new DataOutputStream(outputStream);
            output.writeInt(items.length);

            for (ItemStack item : items) {
                if (item == null || item.getType().isAir()) {
                    output.writeInt(0);
                    continue;
                }

                byte[] bytes = item.serializeAsBytes();
                output.writeInt(bytes.length);
                output.write(bytes);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray()); // Base64 encoding is not strictly needed
        } catch (IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    public static List<ItemStack> deserializeToList(String encodedItems) {
        ItemStack[] items = deserialize(encodedItems);

        // Do not clean up the code, it's needed to keep the null entries
        List<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack item : items) {
            if (item == null) {
                itemStacks.add(null);
            } else {
                itemStacks.add(item);
            }
        }

        return itemStacks;
    }

    public static ItemStack[] deserialize(String encodedItems) {
        byte[] bytes = Base64Coder.decodeLines(encodedItems); // Base64 encoding is not strictly needed
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            DataInputStream input = new DataInputStream(inputStream);
            int count = input.readInt();
            ItemStack[] items = new ItemStack[count];
            for (int i = 0; i < count; i++) {
                int length = input.readInt();
                if (length == 0) {
                    // Empty item, keep entry as null
                    continue;
                }

                byte[] itemBytes = new byte[length];
                input.read(itemBytes);
                items[i] = ItemStack.deserializeBytes(itemBytes);
            }
            return items;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

    public static String turnInventoryIntoBase64(Player player) {
        List<ItemStack> inventoryContents = new ArrayList<>();

        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack itemStack = player.getInventory().getContents()[i];

            if (itemStack == null || itemStack.getType().isAir()) {
                itemStack = new ItemStack(org.bukkit.Material.AIR);
            }

            inventoryContents.add(itemStack);
        }

        return BukkitSerialization.serializeWithList(inventoryContents);
    }

    /*
     * This method will give the player the items from the base64 string.
     *
     * @param player The player to give the items to.
     * @param base64 The base64 string to give the player. (This is the base64 string of the player's inventory)
     */
    public static void giveFromBase64(Player player, String base64) {
        List<ItemStack> inventoryContents = BukkitSerialization.deserializeToList(base64);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack itemStack = InventoryUtils.getItemInItemStackArrayIfExist(inventoryContents, i);

            // If the itemStack is null or air, continue.
            if (itemStack == null || itemStack.getType().isAir()) continue;

            // Don't overwrite the player's item if it's not null. Just add it to the inventory if then.
            if (player.getInventory().getItem(i) != null) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getInventory().setItem(i, itemStack);
            }
        }
    }
}
