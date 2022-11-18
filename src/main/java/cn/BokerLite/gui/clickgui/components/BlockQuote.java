package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;

public class BlockQuote extends Component {

    protected final Component parent;
    final int space;
    private final int color;
    private final ArrayList<Component> components = new ArrayList<>();

    public BlockQuote(Component parent) {
        this(0x33000000, parent);
    }

    public BlockQuote(int color, Component parent) {
        this(color, 5, parent);
    }

    public BlockQuote(int color, int space, Component parent) {
        this.color = color;
        this.space = space;
        this.parent = parent;
    }

    public void addComponent(Component... args) {
        for (Component component : args) {
            this.components.add(component);
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, parent.getWidth(), getHeight(), color);
        GlStateManager.pushMatrix();
        int y = 0;
        GlStateManager.translate(space, 0, 0);
        for (Component component : components) {
            component.drawComponent(mouseX - space, mouseY - y, partialTicks);
            GlStateManager.translate(0, component.getHeight(), 0);
            y += component.getHeight();
        }
        GlStateManager.popMatrix();
    }

    public int getWidth() {
        int width = 0;
        for (Component component : components) {
            width = Math.max(width, component.getWidth());
        }
        return width + space * 2;
    }

    public int getHeight() {
        if (components.size() == 0)
            return 0;
        int height = 0;
        for (Component component : components) {
            height += component.getHeight();
        }
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int y = 0;
        for (Component component : components) {
            if (mouseY > y && mouseY < (y + component.getHeight()))
                component.mouseClicked(mouseX - space, mouseY - y, mouseButton);
            y += component.getHeight();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        int y = 0;
        for (Component component : components) {
            if (mouseY > y && mouseY < (y + component.getHeight()))
                component.mouseReleased(mouseX - space, mouseY - y, state);
            y += component.getHeight();
        }
    }

}
