package cn.BokerLite.modules.render;


import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static cn.BokerLite.Client.mc;

public class InventoryHUD  {

    public InventoryHUD() {

    }


    public static void drawHUD(int x, int y) {


        RenderUtil.drawRect(x, y, x+(20 * 9) + 2, y+(20 * 3) + 2, new Color(0, 0, 0, 150).getRGB());
        RenderUtil.drawRect(x, y, x+(20 * 9) + 2, y-13, new Color(HUD.Icons.getValue()).getRGB());
        FontRenderer.F18.drawString("InventoryHUD",x+2,y-12,new Color(255,255,255).getRGB());

        int startX = x + 2;
        int startY = y + 3;
        int curIndex = 0;
        for (int i = 9; i < 36; ++i) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            if (slot == null) {
                startX += 20;
                curIndex += 1;

                if (curIndex > 8) {
                    curIndex = 0;
                    startY += 20;
                    startX = x + 2;
                }

                continue;
            }
            drawItemStack(slot, startX, startY);
            startX += 20;
            curIndex += 1;
            if (curIndex > 8) {
                curIndex = 0;
                startY += 20;
                startX = x + 2;
            }
        }
    }

    public static void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
