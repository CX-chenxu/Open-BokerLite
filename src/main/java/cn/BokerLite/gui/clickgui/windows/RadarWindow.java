package cn.BokerLite.gui.clickgui.windows;

import cn.BokerLite.Client;
import cn.BokerLite.gui.clickgui.components.SubWindow;

public class RadarWindow extends SubWindow {
    public RadarWindow() {
        super("Radar", "雷达");
        hidden = true;
        this.x = 0.1;
        this.y = 0.1;
    }

    public int getWidth() {
        int width = 128;
        return Math.max(width, 0);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (!hidden) {
            super.drawComponent(mouseX, mouseY, partialTicks);
            Client.radarX = this.getX();
            Client.radarY = this.getY() + 20;
        }
    }
}