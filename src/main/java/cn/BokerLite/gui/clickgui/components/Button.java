package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;

public class Button extends Component {

    protected String text;
    protected String ch;
    protected SubWindow parent;

    public Button(String text, SubWindow parent) {
        this.text = text;
        this.ch = "none";
        this.parent = parent;
    }

    public Button(String text, String ch, SubWindow parent) {
        this.text = text;
        this.ch = ch;
        this.parent = parent;
    }

    public boolean isEnabled() {
        return false;
    }

    public int getWidth() {
        return FontRenderer.F16.getStringWidth((ch == "none") ? text : Client.chinese ? ch : text);
    }

    public int getHeight() {
        return 17;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (mouseX > 0 && mouseX < parent.getWidth() &&
                mouseY > 0 && mouseY < getHeight()) {
            Gui.drawRect(0, 0, parent.getWidth(), getHeight(), 0xAA000000);
        }
        int x = (parent.getWidth() - FontRenderer.F16.getStringWidth((ch == "none") ? text : Client.chinese ? ch : text)) / 2;
        int y = 6;
        FontRenderer.F16.drawString((ch == "none") ? text : Client.chinese ? ch : text, x, y, isEnabled() ? Client.THEME_RGB_COLOR : -1);
    }

}
