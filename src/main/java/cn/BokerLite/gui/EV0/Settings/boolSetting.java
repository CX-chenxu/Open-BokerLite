package cn.BokerLite.gui.EV0.Settings;

import cn.BokerLite.gui.EV0.Downward;
import cn.BokerLite.gui.EV0.EV0Clickgui;
import cn.BokerLite.gui.EV0.ModuleRender;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;


import java.awt.*;

public class boolSetting extends Downward<Option> {

    //获取主界面xy跟随移动
    private float modulex,moduley,booly;

    public boolSetting(Option s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        //内16,16,19 外52,52,51
        modulex = EV0Clickgui.mainx;
        moduley = EV0Clickgui.mainy;

        booly = pos.y + getScrollY();

        FontLoaders.NL18.drawString(setting.getName(),pos.x + modulex + 117,booly + moduley + 46, -1);

        RenderUtil.drawRoundedRect(pos.x + modulex + 247,booly + moduley + 45,9.5f,9.5f,0.1f,new Color(16,16,19).getRGB(), 2,new Color(52,52,51).getRGB());
        if (isHovered(mouseX,mouseY) && !(boolean) this.setting.getValue()){
            RenderUtil.drawCheck(pos.x + modulex + 249,booly + moduley + 49,2 ,new Color(95,94,98,200).getRGB());
        }
        if ((boolean) this.setting.getValue()){
            RenderUtil.drawCheck(pos.x + modulex + 249,booly + moduley + 49,2 ,new Color(HUD.Hudcolor.getValue()).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY)){
            if (mouseButton == 0){
                this.setting.setValue(!(boolean)setting.getValue());
            }
        }

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= pos.x + modulex + 247 && mouseX <= pos.x + modulex + 247 + 9.5f  && mouseY >= pos.y + getScrollY() + moduley + 45  &&mouseY <= pos.y + getScrollY() + moduley + 45 +9.5f ;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
