package cn.BokerLite.gui.alt.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.utils.render.RenderUtil;
import cn.BokerLite.utils.timer.TimerUtil;

import java.awt.*;

public class GuiMantheButton extends GuiButton {
    /**
     * The x position of this control.
     */
    public int xPosition;
    /**
     * The y position of this control.
     */
    public int yPosition;
    /**
     * The string displayed on this control.
     */
    public String displayString;
    public int id;
    /**
     * True if this control is enabled, false to disable.
     */
    public boolean enabled;
    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    /**
     * Button width in pixels
     */
    protected int width;
    /**
     * Button height in pixels
     */
    protected int height;
    protected boolean hovered;

    TimerUtil valuetimer = new TimerUtil();

    public GuiMantheButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiMantheButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }


    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if (hovered) {
                RenderUtil.drawRoundedRect(this.xPosition, this.yPosition, xPosition + width, yPosition + height, 1, new Color(23, 168, 137).darker().getRGB());
            } else {
                RenderUtil.drawRoundedRect(this.xPosition, this.yPosition, xPosition + width, yPosition + height, 1, new Color(23, 168, 137).getRGB());
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = -1;
            ClickGui.drawCenteredStrings(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }

    /**
     * 拖动鼠标按钮时激发。相当于MouseListener。鼠标标记（MouseEvent e）。
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseClicked() {
    }

    /**
     * 释放鼠标按钮时激发。相当于MouseListener。鼠标松开（MouseEvent e）。
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
