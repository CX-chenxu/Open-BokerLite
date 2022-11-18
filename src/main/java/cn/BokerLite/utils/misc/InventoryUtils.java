package cn.BokerLite.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static cn.BokerLite.utils.Utils.mc;

public class InventoryUtils {
    public static int findItem(int startSlot, int endSlot, Item item) {
        try {
            int i = startSlot;
            while (i < endSlot) {
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && stack.getItem() == item) {
                    return i;
                }
                ++i;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
    public static void click(int slot, int mouseButton, boolean shiftClick) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, mc.thePlayer);
    }

    public static void drop(int slot) {
        mc.playerController.windowClick(0, slot, 1, 4, mc.thePlayer);
    }
    public static void swap(int n, int n2) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, n, n2, 2, mc.thePlayer);
    }
}
