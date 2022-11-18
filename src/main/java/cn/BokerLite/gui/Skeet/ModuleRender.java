//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.BokerLite.gui.Skeet;

import cn.BokerLite.gui.Skeet.settings.NumberSetting;
import cn.BokerLite.gui.Skeet.settings.OptionSetting;
import cn.BokerLite.gui.Skeet.settings.StringsSetting;
import cn.BokerLite.gui.Skeet.settings.colorSetting;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.value.*;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class ModuleRender {
    public Module module;
    public int height = 0;
    public float y;
    public float x;
    public int mainx;
    public int mainy;
    public int scrollY = 0;
    public List<Downward> downwards;
    private final Position pos;

    public ModuleRender(Module module, float modX, float modY, float w, float h) {
        this.module = module;
        this.downwards = new ArrayList();
        int cHeight = 28;
        Iterator var7 = module.getValues().iterator();

        while(var7.hasNext()) {
            Value setting = (Value)var7.next();
            if (setting instanceof Option) {
                this.downwards.add(new OptionSetting((Option)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 20;
            }

            if (setting instanceof Numbers) {
                this.downwards.add(new NumberSetting((Numbers)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 20;
            }
            if (setting instanceof Mode) {
                this.downwards.add(new StringsSetting((Mode)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 20;
            }

        
           
        }

        this.height = cHeight;
        this.pos = new Position(modX, modY, w, (float)cHeight);
    }

    public void draw(int mx, int my) {
    	 this.mainx = HyperGui.mainx;
         this.mainy = HyperGui.mainy;
        this.x = this.pos.x;
        this.y = this.pos.y + (float)this.scrollY;
        RenderUtil.drawRoundedRect((float)(this.mainx + 10) + this.x, (float)(this.mainy + 35) + this.y,(float)(this.mainx + 10) + this.x+ 160.0F,(float)(this.mainy + 35) + this.y+ this.pos.height, 2, new Color(1,11,21, 255).getRGB());
        RenderUtil.drawRect((float)(this.mainx + 12) + this.x, (float)(this.mainy + 35) + this.y+20,(float)(this.mainx + 10) + this.x+ 158.0F,(float)(this.mainy + 35) + this.y+ 21,  new Color(6,18,31, 255).getRGB());
        FontLoaders.NL20.drawString(this.module.getName(), (float)(this.mainx + 17) + this.x, (float)(this.mainy + 42) + this.y, new Color(130,140,150).getRGB());
        float var10002 = (float)(this.mainx + 17) + this.x;
        float var10003 = (float)(this.mainy + 60) + this.y;
        
     //   FontLoaders.NL18.drawString("Enable", var10002, var10003, new Color(130,140,150).getRGB());
        RenderUtil.drawRect((float)(this.mainx + 153) + this.x, (float)(this.mainy + 40) + this.y, (float)(this.mainx + 153) + this.x+8.0F,  (float)(this.mainy + 40) + this.y+8.0F, new Color(5,23,37).getRGB());
        if (this.module.getState()) {
        	  RenderUtil.drawCheck((float)this.mainx + 154.5F + this.x, (float)this.mainy + 43.5F + this.y, 2,   (new Color(5,166,238)).getRGB());
        }
      
        
        this.downwards.forEach((e) -> {
            e.draw(mx, my);
        });
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && RenderUtil.isHovering((float)(this.mainx + 153) + this.x, (float)(this.mainy + 40) + this.y, 8.0F, 8.0F, mouseX, mouseY)) {
            this.module.setState(!this.module.getState());
        }

        Iterator var4 = this.downwards.iterator();

        while(var4.hasNext()) {
            Downward downward = (Downward)var4.next();
            downward.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.downwards.forEach((e) -> {
            e.mouseReleased(mouseX, mouseY, state);
        });
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.downwards.forEach((e) -> {
            e.keyTyped(typedChar, keyCode);
        });
    }

    public int getMaxScrollY() {
        return (int)((float)((int)this.pos.y) + this.pos.height);
    }

    public int getPosY() {
        return (int)this.pos.y;
    }

    public int getScrollY() {
        return this.scrollY;
    }
}
