package cn.BokerLite.gui.clickgui.windows;

import cn.BokerLite.Client;
import cn.BokerLite.gui.clickgui.components.SubWindow;

public class TextGuiWindow extends SubWindow {
    public TextGuiWindow() {
        super("Text Gui", "文字GUI");
        this.x = 1;
        this.y = 0;
    }

    public int getWidth() {
        try {
            int width = 32;
            return Math.max(width, Client.INSTANCE.getWidth());
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    public int getHeight() {
        return 20;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        super.drawComponent(mouseX, mouseY, partialTicks);
        Client.INSTANCE.textX = this.x;
        Client.INSTANCE.textY = this.y;
        Client.INSTANCE.textHidden = hidden;
    }
}