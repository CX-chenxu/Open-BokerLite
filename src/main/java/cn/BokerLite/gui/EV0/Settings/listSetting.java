package cn.BokerLite.gui.EV0.Settings;

import cn.BokerLite.gui.EV0.Downward;
import cn.BokerLite.gui.EV0.EV0Clickgui;
import cn.BokerLite.gui.EV0.EaseInOutQuad;
import cn.BokerLite.gui.EV0.ModuleRender;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.Direction;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.value.Mode;

import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class listSetting extends Downward<Mode> {

    public Animation animation;
    private final Mode listValue;
    public listSetting(Mode s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.listValue = s;
    }

    private final Animation arrowAnimation = new EaseInOutQuad(250, 1, Direction.BACKWARDS);

    private float modulex,moduley,listy;

    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = EV0Clickgui.mainx + 90;
        moduley= EV0Clickgui.mainy - 10;

        listy = pos.y+ getScrollY();

        RenderUtil.drawRoundedRect(pos.x + modulex + 102f,listy + moduley + 53,65f,11f,0.1f,new Color(16,16,16).getRGB(), 1.1f,new Color(52,52,51).getRGB());

        FontLoaders.NL18.drawString(listValue.getName(),pos.x + modulex + 27.5f,listy + moduley + 55, -1);

        FontLoaders.NL16.drawString( this.setting.getValue().toString(),pos.x + modulex + 104.5f,listy + moduley + 56f, -1);

        //绘制箭头
        arrowAnimation.setDirection(listValue.openList ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtil.drawClickGuiArrow(pos.x + modulex + 160f,listy + moduley + 58f, 4, arrowAnimation,-1);

        if (this.listValue.openList) {
            //循环添加Strings
            //覆盖下面的Value
            GL11.glTranslatef(0.0f, 0.0f, 2.0f);
            RenderUtil.drawRoundedRect(pos.x + modulex + 102f, (float) (listy + moduley + 57 + animation.getOutput()),85f,1f + listValue.getModes().length * 11f,0.1f,new Color(16,16,16).getRGB(), 1.1f,new Color(52,52,51).getRGB());
          //  RenderUtil.drawBorderedRect(modulex+ 5+ pos.x + 80 ,moduley+ 17 +listy + 8 + 13,modulex+ 5+ pos.x + 80 + 50f,moduley+ 17 +listy + 8 + 13 + listValue.getValues().length * 11f,1f,new ColorValue(85,90,96).getRGB(), new ColorValue(59,63,72).getRGB());

            for (Enum option : listValue.getModes()) {
                if (option.equals(listValue.getValue())){
                 RenderUtil.drawRect(pos.x + modulex + 102.5f, (float) (listy + moduley + 57f +   animation.getOutput() + listValue.getModeListinde(String.valueOf(option))* 11), pos.x + modulex + 102.5f + 1.5f, (float) (listy + moduley + 57f +   animation.getOutput() +   listValue.getModeListinde(String.valueOf(option))* 11 + 11f),new Color(HUD.Hudcolor.getValue()).getRGB());
                 //   RenderUtil.drawRoundedRect(pos.x + modulex + 102.5f,listy + moduley + 67f +  listValue.getModeListinde(option)* 11,1.5f,11f,0.1f,new ColorValue(HUD.Hudcolor.getVaule()).getRGB(), 1f,new ColorValue(HUD.Hudcolor.getVaule()).getRGB());
                }
                FontLoaders.NL16.drawString(String.valueOf(option),option.equals(listValue.getValue()) ? pos.x + modulex + 105f + 3 : pos.x + modulex + 105f, (float) (listy + moduley + 61f +  animation.getOutput()  + listValue.getModeListinde(String.valueOf(option))* 11), option.equals(listValue.getValue()) ?new Color(HUD.Hudcolor.getValue()).getRGB():new Color(200,200,200).getRGB());
            }
            GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && isHovered(mouseX,mouseY)){
            animation = new DecelerateAnimation(225, 10, Direction.FORWARDS);
            listValue.openList = !listValue.openList;
        }
        //设置mode
        if (mouseButton == 0) {
            if (this.listValue.openList //在这个x里面
                    && mouseX >= pos.x + modulex + 102f // 最小x
                    && mouseX <= pos.x + modulex + 102f + 65f // 最大x
            ) {
                //循环判断点击
                for (int i = 0; i < listValue.getModes().length; i++) {
                    //判断Y
                    final int v = (int) (listy + moduley + 67f + i * 11);

                    if (mouseY >= v && mouseY <= v + 11) {
                        this.setting.setValue(this.setting.getModeGet(i));
   
                        this.listValue.openList = false;
                    }

                }
            }
        }

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=pos.x + modulex + 102f && mouseX <=pos.x + modulex + 102f + 65f  && mouseY >= listy + moduley + 53 && mouseY <= listy + moduley + 53 + 11f ;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
