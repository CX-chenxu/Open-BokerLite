package cn.BokerLite.gui.keybindgui;

import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class KeyBindManager extends GuiScreen {
    private final int baseHeight = 205;
    private final int baseWidth = 500;

    private final ArrayList<KeyInfo> keys = new ArrayList<>();

    public KeyInfo nowDisplayKey;
    public PopUI popUI;

    public KeyBindManager() {
        this.keys.add(new KeyInfo(12.0F, 12.0F, 27.0F, 32.0F, 41, "`"));
        this.keys.add(new KeyInfo(44.0F, 12.0F, 27.0F, 32.0F, 2, "1"));
        this.keys.add(new KeyInfo(76.0F, 12.0F, 27.0F, 32.0F, 3, "2"));
        this.keys.add(new KeyInfo(108.0F, 12.0F, 27.0F, 32.0F, 4, "3"));
        this.keys.add(new KeyInfo(140.0F, 12.0F, 27.0F, 32.0F, 5, "4"));
        this.keys.add(new KeyInfo(172.0F, 12.0F, 27.0F, 32.0F, 6, "5"));
        this.keys.add(new KeyInfo(204.0F, 12.0F, 27.0F, 32.0F, 7, "6"));
        this.keys.add(new KeyInfo(236.0F, 12.0F, 27.0F, 32.0F, 8, "7"));
        this.keys.add(new KeyInfo(268.0F, 12.0F, 27.0F, 32.0F, 9, "8"));
        this.keys.add(new KeyInfo(300.0F, 12.0F, 27.0F, 32.0F, 10, "9"));
        this.keys.add(new KeyInfo(332.0F, 12.0F, 27.0F, 32.0F, 11, "0"));
        this.keys.add(new KeyInfo(364.0F, 12.0F, 27.0F, 32.0F, 12, "-"));
        this.keys.add(new KeyInfo(396.0F, 12.0F, 27.0F, 32.0F, 13, "="));
        this.keys.add(new KeyInfo(428.0F, 12.0F, 59.0F, 32.0F, 14, "Backspace"));
        this.keys.add(new KeyInfo(12.0F, 49.0F, 43.0F, 32.0F, 15, "Tab"));
        this.keys.add(new KeyInfo(60.0F, 49.0F, 27.0F, 32.0F, 16, "Q"));
        this.keys.add(new KeyInfo(92.0F, 49.0F, 27.0F, 32.0F, 17, "W"));
        this.keys.add(new KeyInfo(124.0F, 49.0F, 27.0F, 32.0F, 18, "E"));
        this.keys.add(new KeyInfo(156.0F, 49.0F, 27.0F, 32.0F, 19, "R"));
        this.keys.add(new KeyInfo(188.0F, 49.0F, 27.0F, 32.0F, 20, "T"));
        this.keys.add(new KeyInfo(220.0F, 49.0F, 27.0F, 32.0F, 21, "Y"));
        this.keys.add(new KeyInfo(252.0F, 49.0F, 27.0F, 32.0F, 22, "U"));
        this.keys.add(new KeyInfo(284.0F, 49.0F, 27.0F, 32.0F, 23, "I"));
        this.keys.add(new KeyInfo(316.0F, 49.0F, 27.0F, 32.0F, 24, "O"));
        this.keys.add(new KeyInfo(348.0F, 49.0F, 27.0F, 32.0F, 25, "P"));
        this.keys.add(new KeyInfo(380.0F, 49.0F, 27.0F, 32.0F, 26, "["));
        this.keys.add(new KeyInfo(412.0F, 49.0F, 27.0F, 32.0F, 27, "]"));
        this.keys.add(new KeyInfo(444.0F, 49.0F, 43.0F, 32.0F, 43, "\\"));
        this.keys.add(new KeyInfo(12.0F, 86.0F, 59.0F, 32.0F, 15, "Caps Lock"));
        this.keys.add(new KeyInfo(76.0F, 86.0F, 27.0F, 32.0F, 30, "A"));
        this.keys.add(new KeyInfo(108.0F, 86.0F, 27.0F, 32.0F, 31, "S"));
        this.keys.add(new KeyInfo(140.0F, 86.0F, 27.0F, 32.0F, 32, "D"));
        this.keys.add(new KeyInfo(172.0F, 86.0F, 27.0F, 32.0F, 33, "F"));
        this.keys.add(new KeyInfo(204.0F, 86.0F, 27.0F, 32.0F, 34, "G"));
        this.keys.add(new KeyInfo(236.0F, 86.0F, 27.0F, 32.0F, 35, "H"));
        this.keys.add(new KeyInfo(268.0F, 86.0F, 27.0F, 32.0F, 36, "J"));
        this.keys.add(new KeyInfo(300.0F, 86.0F, 27.0F, 32.0F, 37, "K"));
        this.keys.add(new KeyInfo(332.0F, 86.0F, 27.0F, 32.0F, 38, "L"));
        this.keys.add(new KeyInfo(364.0F, 86.0F, 27.0F, 32.0F, 39, ";"));
        this.keys.add(new KeyInfo(396.0F, 86.0F, 27.0F, 32.0F, 40, "'"));
        this.keys.add(new KeyInfo(428.0F, 86.0F, 59.0F, 32.0F, 28, "Enter"));
        this.keys.add(new KeyInfo(12.0F, 123.0F, 75.0F, 32.0F, 42, "Shift", "LShift"));
        this.keys.add(new KeyInfo(92.0F, 123.0F, 27.0F, 32.0F, 44, "Z"));
        this.keys.add(new KeyInfo(124.0F, 123.0F, 27.0F, 32.0F, 45, "X"));
        this.keys.add(new KeyInfo(156.0F, 123.0F, 27.0F, 32.0F, 46, "C"));
        this.keys.add(new KeyInfo(188.0F, 123.0F, 27.0F, 32.0F, 47, "V"));
        this.keys.add(new KeyInfo(220.0F, 123.0F, 27.0F, 32.0F, 48, "B"));
        this.keys.add(new KeyInfo(252.0F, 123.0F, 27.0F, 32.0F, 49, "N"));
        this.keys.add(new KeyInfo(284.0F, 123.0F, 27.0F, 32.0F, 50, "M"));
        this.keys.add(new KeyInfo(316.0F, 123.0F, 27.0F, 32.0F, 51, ","));
        this.keys.add(new KeyInfo(348.0F, 123.0F, 27.0F, 32.0F, 52, "."));
        this.keys.add(new KeyInfo(380.0F, 123.0F, 27.0F, 32.0F, 53, "/"));
        this.keys.add(new KeyInfo(412.0F, 123.0F, 75.0F, 32.0F, 54, "Shift", "RShift"));
        this.keys.add(new KeyInfo(12.0F, 160.0F, 43.0F, 32.0F, 29, "Ctrl", "LCtrl"));
        this.keys.add(new KeyInfo(60.0F, 160.0F, 43.0F, 32.0F, 56, "Alt", "LAlt"));
        this.keys.add(new KeyInfo(108.0F, 160.0F, 251.0F, 32.0F, 57, " ", "Space"));
        this.keys.add(new KeyInfo(364.0F, 160.0F, 43.0F, 32.0F, 184, "Alt", "RAlt"));
        this.keys.add(new KeyInfo(412.0F, 160.0F, 27.0F, 32.0F, 199, "\u00d8", "Home"));
        this.keys.add(new KeyInfo(444.0F, 160.0F, 43.0F, 32.0F, 157, "Ctrl", "RCtrl"));

    }

    @Override
    public void initGui() {
        nowDisplayKey = null;
        popUI = null;
        updateAllKeys();
    }

    public void updateAllKeys() {
        new Thread(() -> {
            for (KeyInfo key : keys) {
                key.update();
            }
        }).start();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int mcWidth = (int) ((width * 0.8F) - (width * 0.2F));
        GL11.glPushMatrix();
        GL11.glScalef(2F,2F,2F);
        FontRenderer.F18.drawString("Key Bind Manager",width * 0.21F * 0.5F,height * 0.2F * 0.5F, Color.WHITE.getRGB());
        GL11.glScalef(0.5F,0.5F,0.5F);
        GL11.glTranslatef(width * 0.2F,height * 0.2F + FontRenderer.F18.getFontHeight() * 2.3F,0F);
        float scale = mcWidth / (float)baseWidth;
        GL11.glScalef(scale,scale,scale);
        RenderUtil.drawRect(0F,0F,(float)baseWidth,(float)baseHeight,Color.WHITE.getRGB());
        for (KeyInfo key : keys)
            key.render();
        if (nowDisplayKey != null)
            nowDisplayKey.renderTab();
        GL11.glPopMatrix();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel != 0) {
                if (popUI != null) {
                    popUI.onStroll(width, height, mouseX, mouseY, wheel);
                } else if (nowDisplayKey != null) {
                    float scaledMouseX = (mouseX - width * 0.2F) / scale;
                    float scaledMouseY = (mouseY - (height * 0.2F + FontRenderer.F18.getFontHeight() * 2.3F)) / scale;
                    nowDisplayKey.stroll(scaledMouseX,scaledMouseY,wheel);
                }
            }
        }
        if (popUI != null) {
            popUI.onRender(width,height);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (popUI == null) {
            float scale = ((width * 0.8F) - (width * 0.2F)) / baseWidth;
            float scaledMouseX = (mouseX - width * 0.2F) / scale;
            float scaledMouseY = (mouseY - (height * 0.2F + FontRenderer.F18.getFontHeight() * 2.3F)) / scale;
            if (nowDisplayKey == null) {
                if (scaledMouseX < 0 || scaledMouseY < 0|| scaledMouseX > baseWidth || scaledMouseY > baseHeight) {
                    mc.displayGuiScreen(null);
                    return;
                }
                for (KeyInfo key : keys) {
                    if (scaledMouseX > key.posX && scaledMouseY > key.posY &&
                            scaledMouseX < (key.posX + key.width) && scaledMouseY < (key.posY + key.height)) {
                        key.click(scaledMouseX,scaledMouseY);
                        break;
                    }
                }
            } else {
                nowDisplayKey.click(scaledMouseX,scaledMouseY);
            }
        } else {
            popUI.onClick(width,height,mouseX,mouseY);
        }
    }

    @Override
    public void onGuiClosed() {
        // 保存数据
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            if (popUI != null) {
                popUI = null;
            } else if (nowDisplayKey != null) {
                nowDisplayKey = null;
            } else {
                mc.displayGuiScreen(null);
            }
            return;
        }
        if (popUI != null) {
            popUI.onKey(typedChar, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
