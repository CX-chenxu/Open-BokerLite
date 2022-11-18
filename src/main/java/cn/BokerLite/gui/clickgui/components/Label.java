package cn.BokerLite.gui.clickgui.components;


import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;

public class Label extends Component {
    private final String str;
    private final String ch;
    protected SubWindow parent;

    public Label(String str, String ch, SubWindow parent) {
        this.str = str;
        this.ch = ch;
        this.parent = parent;
    }

    public int getWidth() {
        return FontRenderer.F16.getStringWidth(Client.chinese ? ch : str);
    }

    public int getHeight() {
        return 16;
    }

    public String getStr() {
        return Client.chinese ? ch : str;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int x = (parent.getWidth() - FontRenderer.F16.getStringWidth(Client.chinese ? ch : str)) / 2;
        int y = 6;
        FontRenderer.F16.drawString(Client.chinese ? ch : str, x, y, -1);
    }

}
