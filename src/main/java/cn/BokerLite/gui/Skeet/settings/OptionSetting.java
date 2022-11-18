//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.BokerLite.gui.Skeet.settings;

import cn.BokerLite.gui.Skeet.Downward;
import cn.BokerLite.gui.Skeet.HyperGui;
import cn.BokerLite.gui.Skeet.ModuleRender;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.Color;




public class OptionSetting extends Downward<Option> {
    public int mainx;
    public int mainy;
    public int y;

    public OptionSetting(Option s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    public void draw(int mouseX, int mouseY) {
        this.mainx = HyperGui.mainx;
        this.mainy = HyperGui.mainy;
        this.y = (int)(this.pos.y + (float)this.getScrollY());
        FontLoaders.NL18.drawString(this.setting.getName(), (float)(this.mainx + 17) + this.pos.x, (float)(this.mainy + 38 + this.y), new Color(130,140,150).getRGB());
        RenderUtil.drawRect((float)(this.mainx + 153) + this.pos.x, (float)(this.mainy + 38 + this.y), (float)(this.mainx + 153) + this.pos.x+8.0F,(float)(this.mainy + 38 + this.y)+ 8.0F, new Color(5,23,37).getRGB());
        if((boolean) this.setting.getValue()) {
            RenderUtil.drawCheck((float)this.mainx + 154.5F + this.pos.x, (float)this.mainy + 41.5F + (float)this.y, 2, (new Color(5,166,238)).getRGB());
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovering((float)(this.mainx + 153) + this.pos.x, (float)(this.mainy + 38 + this.y),this.mainx+ 8.0F, 8.0F, mouseX, mouseY) && mouseButton == 0) {
            this.setting.setValue(!(boolean)setting.getValue());
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
