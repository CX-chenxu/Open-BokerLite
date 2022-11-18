package cn.BokerLite.gui.clickgui.windows;

import cn.BokerLite.modules.combat.LegitAura;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.gui.clickgui.components.SubWindow;

import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;

public class TargetInfoWindow extends SubWindow {
    public TargetInfoWindow() {
        super("TargetInfo", "目标HUD");
        hidden = true;
        this.x = 0.8;
        this.y = 0.8;
    }

    public int getWidth() {
        int width = 120;
        return Math.max(width, 0);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (!hidden) {
            super.drawComponent(mouseX, mouseY, partialTicks);
            LegitAura.x = (float) this.getX();
            LegitAura.y = (float) this.getY();
            Client.tx = this.getX();
            Client.ty = this.getY();
            float x2 = this.getX();
            float y2 = this.getY() + 20;
            int additionalWidth = Math.max(FontRenderer.F16.getStringWidth(mc.thePlayer.getName()), 75);
            RenderUtil.drawRoundedRect(x2 + 0.0F, y2 + 0.0F, x2 + 45.0F + (float) additionalWidth, y2 + 40.0F, 4, new Color(33, 33, 33).getRGB());
            GL11.glColor4f(1F, 1F, 1F, 1F);
            mc.getTextureManager().bindTexture(LegitAura.getskin(mc.thePlayer));
            RenderUtil.drawScaledCustomSizeModalCircle((int) (x2 + 5), (int) (y2 + 5), 8f, 8f, 8, 8, 30, 30, 64f, 64f);
            RenderUtil.drawScaledCustomSizeModalCircle((int) (x2 + 5), (int) (y2 + 5), 40f, 8f, 8, 8, 30, 30, 64f, 64f);
            ClickGui.drawCenteredStrings(mc.thePlayer.getName(), (int) (x2 + 40 + (additionalWidth / 2f)), (int) (y2 + 5f), -1);
            ClickGui.drawCenteredStrings("Health: " + (int) mc.thePlayer.getHealth(), (int) (x2 + 40 + (additionalWidth / 2f)), (int) (y2 + 6f) + FontRenderer.F16.getFontHeight(), -1);
            RenderUtil.drawRoundedRect(x2 + 40f, y2 + 28f, x2 + 40f + additionalWidth, y2 + 33f, 1, new Color(0, 0, 0).getRGB());
            RenderUtil.drawRoundedRect(x2 + 40f, y2 + 28f, x2 + 40f + (mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth()) * additionalWidth, y2 + 33f, 1, Client.THEME_RGB_COLOR);
        }
    }
}