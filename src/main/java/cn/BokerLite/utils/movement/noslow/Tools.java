package cn.BokerLite.utils.movement.noslow;

import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class Tools {
    private static Field timerField = null;
    private static Field mouseButton = null;
    private static Field mouseButtonState = null;
    private static Field mouseButtons = null;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static String getLocalPath() {
        return System.getProperty("user.dir");

    }

    public static void su() {
        try {
            timerField = Minecraft.class.getDeclaredField("field_71428_T");
        } catch (Exception var4) {
            try {
                timerField = Minecraft.class.getDeclaredField("timer");
            } catch (Exception ignored) {
            }
        }

        if (timerField != null) {
            timerField.setAccessible(true);
        }

        try {
            mouseButton = MouseEvent.class.getDeclaredField("button");
            mouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
            mouseButtons = Mouse.class.getDeclaredField("buttons");
        } catch (Exception var2) {
        }

    }

    public static boolean nullCheck() {
        return (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null);
    }

    public static void setMouseButtonState(int mouseButton, boolean held) {
        if (Tools.mouseButton != null && mouseButtonState != null && mouseButtons != null) {
            MouseEvent m = new MouseEvent();

            try {
                Tools.mouseButton.setAccessible(true);
                Tools.mouseButton.set(m, mouseButton);
                mouseButtonState.setAccessible(true);
                mouseButtonState.set(m, held);
                MinecraftForge.EVENT_BUS.post(m);
                mouseButtons.setAccessible(true);
                ByteBuffer bf = (ByteBuffer) mouseButtons.get(null);
                mouseButtons.setAccessible(false);
                bf.put(mouseButton, (byte) (held ? 1 : 0));
            } catch (IllegalAccessException var4) {
            }

        }
    }

    public static boolean isPlayerHoldingWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }

    public static String getConfigPath() {
        return getLocalPath() + "\\Fair";
    }

    public static boolean isHyp() {
        if (!Tools.isPlayerInGame()) return false;
        try {
            return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
        } catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }

    public static String getFontPath() {
        return getLocalPath() + "\\Fair\\Fonts";
    }

    public static String getLogoPath() {
        return getLocalPath() + "\\Fair\\Logos";
    }

    public static boolean isPlayerInGame() {

        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static int getCurrentPlayerSlot() {
        return mc.thePlayer.inventory.currentItem;
    }

    public static void hotkeyToSlot(int slot) {
        if (!Tools.isPlayerInGame())
            return;

        mc.thePlayer.inventory.currentItem = slot;
    }

    public static boolean currentScreenMinecraft() {
        return mc.currentScreen == null;
    }

    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            //System.out.println("114514");
            //System.out.println(( (mc.thePlayer.movementInput.moveForward != 0.0F )||mc.thePlayer.movementInput.moveStrafe != 0.0F));
            return ((mc.thePlayer.movementInput.moveForward != 0.0F) || (mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static boolean isBlocking() {
        if (mc.thePlayer.isUsingItem()) {
            return true;
        }
        return mc.thePlayer.isBlocking();
    }

    public static boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
}
