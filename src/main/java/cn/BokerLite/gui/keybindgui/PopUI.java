package cn.BokerLite.gui.keybindgui;

import org.lwjgl.opengl.GL11;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;

public class PopUI {
    private final String title;
    private final int baseWidth;
    private final int baseHeight;

    public PopUI(String title) {
        this.title = title;
        this.baseWidth = 150;
        this.baseHeight = 210;
    }

    public final int getBaseWidth() {
        return this.baseWidth;
    }

    public final int getBaseHeight() {
        return this.baseHeight;
    }

    public final void onRender(int width, int height) {
        GL11.glPushMatrix();
        RenderUtil.drawRect(0.0F, 0.0F, (float)width, (float)height, (new Color(0, 0, 0, 50)).getRGB());
        float scale = (float)width * 0.2F / (float)this.baseWidth;
        GL11.glTranslatef((float)width * 0.4F, (float)height * 0.3F, 0.0F);
        GL11.glScalef(scale, scale, scale);
        RenderUtil.drawRect(0.0F, 0.0F, (float)this.baseWidth, (float)this.baseHeight, Color.WHITE.getRGB());
        FontRenderer.F40.drawString(this.title, 8.0F, 8.0F, Color.DARK_GRAY.getRGB());
        this.render();
        GL11.glPopMatrix();
    }

    public final void onClick(int width, int height, int mouseX, int mouseY) {
        float scale = (float)width * 0.2F / (float)this.baseWidth;
        float scaledMouseX = ((float)mouseX - (float)width * 0.4F) / scale;
        float scaledMouseY = ((float)mouseY - (float)height * 0.3F) / scale;
        if(scaledMouseX > 0.0F && scaledMouseY > 0.0F && scaledMouseX < (float)this.baseWidth && scaledMouseY < (float)this.baseHeight) {
            this.click(scaledMouseX, scaledMouseY);
        } else {
            this.close();
        }

    }

    public final void onStroll(int width, int height, int mouseX, int mouseY, int wheel) {
        float scale = (float)width * 0.2F / (float)this.baseWidth;
        float scaledMouseX = ((float)mouseX - (float)width * 0.4F) / scale;
        float scaledMouseY = ((float)mouseY - (float)height * 0.3F) / scale;
        if(scaledMouseX > 0.0F && scaledMouseY > 0.0F && scaledMouseX < (float)this.baseWidth && scaledMouseY < (float)this.baseHeight) {
            this.stroll(scaledMouseX, scaledMouseY, wheel);
        }

    }

    public final void onKey(char typedChar, int keyCode) {
        this.key(typedChar, keyCode);
    }

    public void render() {
    }

    public void key(char typedChar, int keyCode) {
    }

    public void close() {
    }

    public void click(float mouseX, float mouseY) {
    }

    public void stroll(float mouseX, float mouseY, int wheel) {
    }
}

