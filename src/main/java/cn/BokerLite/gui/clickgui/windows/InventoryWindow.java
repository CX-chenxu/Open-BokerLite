package cn.BokerLite.gui.clickgui.windows;

import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.gui.clickgui.components.SubWindow;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;

public class InventoryWindow extends SubWindow {
    public InventoryWindow() {
        super("InventoryHUD", "物品栏");
        hidden = true;
        this.x = 0.6;
        this.y = 0.6;
    }

    public int getWidth() {
        int width = 182;
        return Math.max(width, 0);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (!hidden) {
            super.drawComponent(mouseX, mouseY, partialTicks);
            Client.INSTANCE.invX = getX();
            Client.INSTANCE.invY = getY() + 20;
            GL11.glPushMatrix();
            RenderUtil.drawBordered(Client.INSTANCE.invX, Client.INSTANCE.invY, (20 * 9) + 2, (20 * 3) + 2, 1, new Color(0, 0, 0, 140).getRGB(), Client.THEME_RGB_COLOR);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < 27; i++) {
                ItemStack[] itemStack = mc.thePlayer.inventory.mainInventory;
                int offsetX = Client.INSTANCE.invX + 2 + (i % 9) * 20;
                int offsetY = Client.INSTANCE.invY + (i / 9) * 20;
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack[i + 9], offsetX, offsetY);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack[i + 9], offsetX, offsetY, null);
            }
            GL11.glPopMatrix();
        }
    }
}