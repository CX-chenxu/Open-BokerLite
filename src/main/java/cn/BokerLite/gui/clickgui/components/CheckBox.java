package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.value.Option;

public class CheckBox extends Component {
    final Option<Boolean> option;

    public CheckBox(Option<Boolean> option) {
        this.option = option;

    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, 10, 10, 0xFF1D201C);
        Gui.drawRect(1, 1, 9, 9, 0xFF0C0D0C);
        if (option.getValue()) {
            Gui.drawRect(2, 2, 8, 8, Client.THEME_RGB_COLOR);
        }
        FontRenderer.F16.drawString(Client.chinese ? option.ch : option.getDisplayName(), 14, 2, -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            option.setValue(!option.getValue());
    }

    public int getWidth() {
        return 10 + 4 + FontRenderer.F16.getStringWidth(Client.chinese ? option.ch : option.getDisplayName());
    }

    public int getHeight() {
        return 12;
    }
}
