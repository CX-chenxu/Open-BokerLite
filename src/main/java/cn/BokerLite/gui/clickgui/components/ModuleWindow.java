package cn.BokerLite.gui.clickgui.components;

public class ModuleWindow extends SubWindow {
    private boolean hidden = true;

    public ModuleWindow(String title, String ch) {
        super(title, ch);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void toggleHidden() {
        hidden = !hidden;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (!hidden) {
            super.drawComponent(mouseX, mouseY, partialTicks);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!hidden) super.keyTyped(typedChar, keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!hidden) super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!hidden) super.mouseReleased(mouseX, mouseY, state);
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (!hidden) super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }


}
