package cn.BokerLite.gui.clickgui.components;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.modules.value.Mode;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class ModeSelection extends Component {

    final Mode<Enum<?>> mode;

    private final Component parent;
    private boolean expaned, unexpanding; // 治标不治本


    public ModeSelection(Mode<Enum<?>> mode, Component parent) {
        this.mode = mode;
        this.parent = parent;
    }

    public int getExpanedHeight() {
        int height = 0;
        for (@SuppressWarnings("unused") Enum<?> e : mode.getModes()) {
            height += FontRenderer.F16.getFontHeight() + 1;
        }
        return height;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (unexpanding) {
            expaned = unexpanding = false;
        }
        int height = getHeight() - 2;
        int parentWidth = ((BlockQuote) parent).parent.getWidth() - ((BlockQuote) parent).space * 2;
        ClickGui.drawCenteredStrings(mode.getDisplayName(), (int) (parentWidth / 2D), 6, Client.THEME_RGB_COLOR); // 字体偏移
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        Gui.drawRect(2, FontRenderer.F16.getFontHeight() + 5, parentWidth, height + 1, 0xFF << 24 | Client.THEME_RGB_COLOR);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        boolean flag = mouseX > 2 && mouseX < parentWidth &&
                mouseY > FontRenderer.F16.getFontHeight() + 5 && mouseY < height;
        if (flag && !expaned)
            Gui.drawRect(3, FontRenderer.F16.getFontHeight() + 6, parentWidth, height, 0x55 << 24);
        int y = FontRenderer.F16.getFontHeight() + 6;
        if (expaned) {
            for (@SuppressWarnings("unused") Enum<?> e : mode.getModes()) {
                flag = mouseX > 2 && mouseX < parentWidth &&
                        mouseY > y && mouseY < y + 10;
                if (flag) Gui.drawRect(3, y, parentWidth, y + FontRenderer.F16.getFontHeight(), 0x55 << 24);
                y += FontRenderer.F16.getFontHeight() + 1;
            }
        }
        y = FontRenderer.F16.getFontHeight() + 5;
        ClickGui.drawCenteredStrings(mode.getValue().name(), (int) (parentWidth / 2D), y, Client.THEME_RGB_COLOR); //字体偏移x2
        if (expaned) {
            y += FontRenderer.F16.getFontHeight() + 1;
            for (Enum<?> e : mode.getModes()) {
                if (e == mode.getValue())
                    continue;
                ClickGui.drawCenteredStrings(e.name(), (int) (parentWidth / 2D), y, Client.THEME_RGB_COLOR);
                y += FontRenderer.F16.getFontHeight() + 1;
            }

        }
        //GlStateManager.scale(2, 2, 2);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int parentWidth = ((BlockQuote) parent).parent.getWidth() - ((BlockQuote) parent).space;
        boolean flag = mouseX > 2 && mouseX < parentWidth && !expaned &&
                mouseY > FontRenderer.F16.getFontHeight() + 5 && mouseY < getHeight();
        if (flag && mouseButton == 0) {
            if (expaned) {
                unexpanding = true;
            } else {
                expaned = true;
            }
            return;
        }
        ArrayList<Enum<?>> values = Lists.newArrayList(mode.getModes());
        ArrayList<Enum<?>> list = new ArrayList<>();
        list.add(mode.getValue());
        for (Enum<?> e : values) {
            if (e != mode.getValue())
                list.add(e);
        }
        if (expaned) {
            int y = FontRenderer.F16.getFontHeight() + 5;
            for (Enum<?> e : list) {
                flag = mouseX > 2 && mouseX < parentWidth &&
                        mouseY > y && mouseY < y + FontRenderer.F16.getFontHeight() + 1;
                if (flag) {
                    mode.setValue(e);
                    unexpanding = true;
                }
                y += FontRenderer.F16.getFontHeight() + 1;
            }
        }
    }

    public int getWidth() {
        int width = (int) (FontRenderer.F16.getStringWidth(mode.getDisplayName()) * 0.75);
        for (Enum<?> e : mode.getModes()) {
            width = Math.max(width, FontRenderer.F16.getStringWidth(e.name()));
        }
        return width;
    }

    public int getHeight() {
        return expaned ? getExpanedHeight() + 17 : 28;
    }
}
