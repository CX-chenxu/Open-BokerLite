package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.value.Numbers;

public class SliderBar extends Component {

    final Numbers value;

    private final Component parent;

    public SliderBar(Numbers value, Component parent) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int parentWidth = ((BlockQuote) parent).parent.getWidth() - ((BlockQuote) parent).space;
        FontRenderer.F16.drawString(Client.chinese ? value.ch : value.getDisplayName(), 0, 2, -1);
        String str = (int) (value.getValue() * 10) / 10D + "";
        if (value.getIncrement().intValue() == value.getIncrement()) {
            str = String.valueOf(value.getValue().intValue());
        }
        FontRenderer.F16.drawString(str, parentWidth - FontRenderer.F16.getStringWidth(str), 2, -1);
        int y = FontRenderer.F16.getFontHeight() + 1;
        Gui.drawRect(0, y, parentWidth, y + 9, 0xff232523);
        Gui.drawRect(1, y + 1, parentWidth - 1, y + 8, 0xff060906);
        y += 1;
        int color = Client.THEME_RGB_COLOR;
        double precent = (value.getValue() - value.getMinimum()) / (value.getMaximum() - value.getMinimum());
        Gui.drawRect(1, y, (int) ((parentWidth - 2) * precent) - 1, y + 7, 0xFF << 24 | color);
        color = (color & 16579836) >> 2 | color & -16777216;
        Gui.drawRect((int) ((parentWidth - 2) * precent) - 1, y, (int) ((parentWidth - 2) * precent) + 1, y + 7, 0xFF << 24 | color);
        boolean flag = mouseX >= -1 && mouseX <= parentWidth &&
                mouseY > 11 && mouseY < 17;
        if (flag && Mouse.isButtonDown(0)) {
            mouseX--;
            precent = mouseX / (double) (parentWidth - 1);
            double val = Math.max(Math.min((value.getMaximum() - value.getMinimum()) * precent + value.getMinimum(), value.getMaximum()), value.getMinimum());
            val = ((int) (val / value.getIncrement()) * value.getIncrement());
            value.setValue(val);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {


    }

    public int getHeight() {
        return 20;
    }

    public int getWidth() {
        FontRenderer font = FontRenderer.F16;
        return font.getStringWidth(Client.chinese ? value.ch : value.getDisplayName()) + font.getStringWidth(((int) (value.getValue() * 10) / 10D) + "");
    }
}
