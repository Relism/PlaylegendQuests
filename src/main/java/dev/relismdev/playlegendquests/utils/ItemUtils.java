package dev.relismdev.playlegendquests.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ItemUtils {

    /**
     * Serializes an ItemStack object to a Base64-encoded string.
     *
     * @param itemStack The ItemStack object to be serialized.
     * @return A Base64-encoded string representing the serialized ItemStack. Returns null if serialization fails.
     */
    public static String serializeItemStack(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes an ItemStack object from a Base64-encoded string.
     *
     * @param base64String The Base64-encoded string representing the serialized ItemStack.
     * @return The ItemStack object deserialized from the provided Base64 string. Returns null if deserialization fails.
     */
    public static ItemStack deserializeItemStack(String base64String) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
