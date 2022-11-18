package cn.BokerLite.gui.clickgui.components;

import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.gui.configgui.implement.GuiInputBox;
import cn.BokerLite.gui.configgui.implement.GuiShadowButton;
import cn.BokerLite.modules.value.Labels;

import java.util.Random;

public class InputBox extends Component {
    final Labels<String> str;
    final GuiInputBox inputBox;
    protected SubWindow parent;
    boolean addBtn;

    public InputBox(Labels<String> str, Boolean btn, SubWindow parent) {
        this.str = str;
        this.addBtn = btn;
        this.parent = parent;
        if (addBtn) {
            inputBox = new GuiInputBox(new Random().nextInt(66666666), FontRenderer.F16, 0, FontRenderer.F16.getFontHeight() + 2, parent.getWidth() - 2, 12);
        } else {
            inputBox = new GuiInputBox(new Random().nextInt(66666666), FontRenderer.F16, 0, FontRenderer.F16.getFontHeight() + 2, parent.getWidth() + 12, 12);
        }
        inputBox.setPlaceholder(str.getString());
        inputBox.setMaxStringLength(200);
        inputBox.setCanLoseFocus(true);
        inputBox.setEnabled(true);
    }

    // 点击ShadowButton时候Override
    public void buttonClicked() {
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int x = (parent.getWidth() - FontRenderer.F16.getStringWidth(str.getDisplayName())) / 2;
        int y = 0;
        FontRenderer.F16.drawString(str.getDisplayName(), x, y, -1);
        inputBox.drawTextBox();
        if (addBtn) {
            new GuiShadowButton(-1, parent.getWidth() - 12, FontRenderer.F16.getFontHeight() + 2, 12, 12, "+") {
                @Override
                public void mouseClicked() {
                    super.mouseClicked();
                    buttonClicked();
                }
            }.drawButton(mc, mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (inputBox != null && ClickGui.usingInpuBox == inputBox) {
            inputBox.textboxKeyTyped(typedChar, keyCode);
            str.setString(inputBox.getText());
        }
        if (inputBox != null && ClickGui.usingInpuBox == inputBox && inputBox.isFocused() && typedChar == '\r') {
            buttonClicked();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        inputBox.mouseClicked(mouseX, mouseY, mouseButton);
        if (inputBox.isFocused())
            ClickGui.usingInpuBox = inputBox;
    }

    public int getWidth() {
        return 10 + 4;
    }

    public int getHeight() {
        return 14 + FontRenderer.F16.getFontHeight();
    }
}
