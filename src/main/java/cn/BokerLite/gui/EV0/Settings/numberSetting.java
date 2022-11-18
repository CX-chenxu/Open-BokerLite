package cn.BokerLite.gui.EV0.Settings;


import cn.BokerLite.gui.EV0.Downward;
import cn.BokerLite.gui.EV0.EV0Clickgui;
import cn.BokerLite.gui.EV0.ModuleRender;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class numberSetting extends Downward<Numbers> {



    //获取主界面xy跟随移动
    private float modulex,moduley,numbery;

    public float percent = 0;

    //anti-bug
    private boolean iloveyou;
    
    private final Numbers numberValue;



    public numberSetting(Numbers s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.numberValue = s;
    }
    

    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = EV0Clickgui.mainx + 90;
        moduley= EV0Clickgui.mainy - 10;

        //修复滚轮
        numbery = pos.y + getScrollY() ;
        double clamp = MathHelper.clamp_double(Minecraft.getDebugFPS() / 30, 1, 9999);
        final double percentBar = (numberValue.getValue().doubleValue()- numberValue.getMinimum().doubleValue()
        ) / (numberValue.getMaximum().doubleValue() - numberValue.getMinimum().doubleValue());

        percent = Math.max(0, Math.min(1, (float) (percent + (Math.max(0, Math.min(percentBar, 1)) - percent)* (0.2 / clamp))));

        FontLoaders.NL18.drawString(numberValue.getName(),pos.x + modulex + 27.5f,numbery + moduley + 55, -1);
        RenderUtil.drawRoundedRect(pos.x + modulex + 102f,numbery + moduley + 55,65f,6f,0.1f,new Color(16,16,16).getRGB(), 2,new Color(52,52,51).getRGB());
        RenderUtil.drawRect(pos.x + modulex + 102f,numbery + moduley + 55.5f, pos.x + modulex + 102f + 65f * percent,numbery + moduley + 55+5.5f,new Color(HUD.Hudcolor.getValue()).getRGB());

        //设置新的值
        if (iloveyou){
            float percentt = Math.min(1, Math.max(0, ((mouseX - (pos.x + modulex + 102f)) / 99)* 1.55f));
            double newValue = (percentt * (numberValue.getMaximum().doubleValue()
                    - numberValue.getMinimum().doubleValue())) + numberValue.getMinimum().doubleValue();

            double set = incValue(newValue, numberValue.getIncrement().doubleValue());

            numberValue.setValue(set);
        }


        //显示数值框
        if (isHovered(mouseX,mouseY) || iloveyou){
            GL11.glTranslatef(0.0f, 0.0f, 2.0f);
            RenderUtil.drawRoundedRect(pos.x + modulex + 100f,numbery + moduley + 37, (float) (5 +FontLoaders.NL14.getStringWidth(EnumChatFormatting.GRAY + "" + numberValue.getMinimum().floatValue() +"min  "+ EnumChatFormatting.WHITE +  numberValue.getValue().floatValue()+ "  " + EnumChatFormatting.GRAY+ numberValue.getMaximum().floatValue() + "max")),14f,0.1f,new Color(17,17,17).getRGB(), 2,new Color(52,52,51).getRGB());
            FontLoaders.NL14.drawString(EnumChatFormatting.GRAY + "" + numberValue.getMinimum().floatValue() +"min  "+ EnumChatFormatting.WHITE +  numberValue.getValue().floatValue()+ "  " + EnumChatFormatting.GRAY+ numberValue.getMaximum().floatValue()+ "max",pos.x + modulex + 102f,numbery + moduley + 42,-1);
            GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        }
    }

    public static double incValue(double val, double inc) {
        double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                iloveyou = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) iloveyou = false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=pos.x + modulex + 102f && mouseX <= pos.x + modulex + 102f + 65f && mouseY >= numbery + moduley + 55 && mouseY <= numbery + moduley + 55 +6f ;
    }
}
