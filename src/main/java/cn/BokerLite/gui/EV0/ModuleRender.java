package cn.BokerLite.gui.EV0;


import cn.BokerLite.gui.EV0.Settings.boolSetting;
import cn.BokerLite.gui.EV0.Settings.listSetting;
import cn.BokerLite.gui.EV0.Settings.numberSetting;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.Skeet.Position;
import cn.BokerLite.gui.Skeet.settings.colorSetting;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.value.*;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleRender {
    public Position pos;
    //主界面xy
    public int mainx,mainy;

    public float y,x,bindx,bindy,finaly,finalx;

    public int height = 0;


    public boolean renderbind,binds;

    public List<Downward> downwards = new ArrayList<>();

    public Module module;
    public ModuleRender(Module module, float modX, float modY, float w, float h){
    this.module = module;
        //临时变量计算value
        int cHeight = 16;
        //value向下递增y
        //开始计算vlaue h

        //当Value size > 4开启Slient 启用module的独立滚轮
        for(Value setting : module.getValues()) {
            if(setting instanceof Option){
                this.downwards.add(new boolSetting((Option) setting, modX, modY+cHeight, 0, 0,this));
                cHeight+=20;
            }
            if(setting instanceof Numbers){
                this.downwards.add(new numberSetting((Numbers) setting, modX, modY+cHeight, 0, 0,this));
                cHeight+=20;
            }
            if(setting instanceof Mode){
                this.downwards.add(new listSetting((Mode) setting, modX, modY+cHeight, 0, 0,this));
                cHeight+=20;
            }
            if (setting instanceof ColorValue){
                this.downwards.add(new colorSetting((ColorValue) setting,modX,modY+cHeight,0,0,this));
                cHeight+=20;
            }
        }
        if (module.getValues().isEmpty()){
            cHeight+=20;
        }
        this.height = cHeight;

        pos = new Position(modX,modY,w,cHeight);
    }

    public void drawScreen(int mouseX, int mouseY) {
        mainx = EV0Clickgui.mainx;
        mainy = EV0Clickgui.mainy;


        this.x = pos.x;
        this.y = pos.y  + getScrollY();

        //如果移动了
        if (finalx != mainx || finaly != mainy){
            renderbind = false;
            binds = false;
        }

        //默认 x 112 w 147 x2 112 + 157
        //顶部画圆
        RenderUtil.drawRoundedRect(mainx + 112+ x, mainy+ 39+ y,mainx + 112+ x+147,mainy+ 39+ y+pos.height,1,new Color(29,29,29).getRGB());

        RenderUtil.drawRect(mainx + 111+ x - 0.01f, mainy+ 39+ 15+ y,mainx + 112 + 148+ x- 0.01f,mainy+ 39 +15+  1+ y,new Color(52,52,52).getRGB());

        //.filter(s -> s.setting.getDisplayable())
        downwards.forEach(setting -> setting.draw(mouseX, mouseY));

        //文字
        FontLoaders.NLLogo20.drawString(module.getName(),mainx + 117 + x, mainy+ 39+ 4+ y,-1);

        if (module.getValues().isEmpty()){
            FontLoaders.NL16.drawString("No Settings :(",mainx + 117 + x, mainy+ 57+ 5+ y,-1);
        }

        RenderUtil.drawRoundedRect(mainx + 247 + x, mainy+ 36+ 5+ y,9.5f,9.5f,0.1f,new Color(16,16,19).getRGB(), 2,new Color(52,52,51).getRGB());
        if (!module.getState() && isHovered(mouseX,mouseY)){
            RenderUtil.drawCheck(mainx + 249 + x, mainy+ 40+ 5+ y,2 ,new Color(95,94,98,200).getRGB());
        }
        if (module.getState()){
            RenderUtil.drawCheck(mainx + 249 + x, mainy+ 40+ 5+ y,2 , new Color(HUD.Hudcolor.getValue()).getRGB());
        }
        //绑定案件
        if (renderbind) {
            GL11.glTranslatef(0.0f, 0.0f, 2.0f);
            RenderUtil.rectangleBordered(bindx, bindy,bindx+ 60, bindy+40, 1f,new Color(16, 16, 20).getRGB(), new Color(52, 52, 51).getRGB());
            FontLoaders.NL18.drawString("Hold",bindx + 5, bindy+ 7,-1);
            RenderUtil.rectangleBordered(bindx + 5, bindy + 20, bindx + 5+45,  bindy + 20+13, 1f, new Color(16, 16, 19).getRGB(),  new Color(52, 52, 51).getRGB());
            FontLoaders.NL16.drawString(Keyboard.getKeyName(module.getKey()),bindx + 8, bindy + 24, binds ?new Color(HUD.Hudcolor.getValue()).getRGB() : new Color(255,255,255).getRGB());
            GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.binds) {
            module.setKey(keyCode == 211 ? 0 : keyCode);
            this.binds = false;
        }
        downwards.forEach(e -> e.keyTyped(typedChar,keyCode));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY) && mouseButton == 0){
            this.module.setState(!this.module.getState());
        }

        if (mouseButton == 1){
            if (RenderUtil.isHovering(mainx + 112+ x, mainy+ 39+ y,147,16,mouseX,mouseY)){
                renderbind = !renderbind;
                bindx = mouseX;
                bindy = mouseY;

                //判断是否移动了
                finalx = mainx;
                finaly = mainy;
            }
            if (!RenderUtil.isHovering(mainx + 112+ x, mainy+ 39+ y,147,16,mouseX,mouseY) && binds){
                module.setKey(0);
                binds = false;
            }
        }

        if (mouseButton == 0){
            if (renderbind){
                if (RenderUtil.isHovering(bindx + 5, bindy + 20, 45, 13,mouseX,mouseY)){
                    binds = !binds;
                }
            }
        }

        downwards.forEach(e -> e.mouseClicked(mouseX,mouseY,mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        downwards.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=mainx + 247 + x   && mouseX <= mainx + 247 + x + 9.5f && mouseY >=mainy+ 36+ 5+ y   && mouseY <= mainy+ 36+ 5+ y  + 9.5f;
    }

    //单独的滚轮Y
    public int scrollY = 0;

    public float getY(){
        return pos.y + getScrollY();
    }
    public float getMaxValueY(){
        return downwards.get(downwards.size() -1).getY();
    }
    public void setY(float y){
        this.pos.y = y;
    }

    public int getScrollY() {
        return scrollY;
    }


}
