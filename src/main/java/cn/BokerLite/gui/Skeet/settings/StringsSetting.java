//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.BokerLite.gui.Skeet.settings;

import java.awt.Color;

import cn.BokerLite.gui.Skeet.Downward;
import cn.BokerLite.gui.Skeet.HyperGui;
import cn.BokerLite.gui.Skeet.ModuleRender;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;


public class StringsSetting extends Downward<Mode> {
    public int mainx;
    public int mainy;
    public int y;
    private double length = 3.0;
	boolean previousmouse = true;
    private double anim = 5.0;

    public StringsSetting(Mode s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    public void draw(int mouseX, int mouseY) {
    	  this.mainx = HyperGui.mainx;
          this.mainy = HyperGui.mainy;
        this.y = (int)(this.pos.y + (float)this.getScrollY());
        FontLoaders.NL18.drawString(this.setting.getName(), (float)(this.mainx + 17) + this.pos.x, (float)(this.mainy + 38 + this.y),new Color(130,140,150).getRGB());
        RenderUtil.drawRect((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 35 + this.y),(float)(this.mainx + 91) + this.pos.x+ 70.0F, (float)(this.mainy + 35 + this.y)+ 12.0F,  new Color(5,23,37).getRGB());
        FontLoaders.NL16.drawString(this.setting.getValue().toString(), (float)(this.mainx + 93) + this.pos.x, (float)(this.mainy + 38 + this.y), new Color(130,140,150).getRGB());
        double val = (double)Minecraft.getDebugFPS() / 8.3;
        if (this.setting.openList && this.length > -3.0) {
            this.length -= 3.0 / val;
        } else if (!this.setting.openList && this.length < 3.0) {
            this.length += 3.0 / val;
        }

        if (this.setting.openList && this.anim < 8.0) {
            this.anim += 3.0 / val;
        } else if (!this.setting.openList && this.anim > 5.0) {
            this.anim -= 3.0 / val;
        }
    //    RenderUtil.drawArrow((double)((float)(this.mainx + 152) + this.pos.x), (double)((float)this.mainy + 34.5F + (float)this.y) + this.anim, 2, new Color(5,166,238).getRGB(), this.length);
        if (this.setting.openList) {
            GL11.glTranslatef(0.0F, 0.0F, 2.0F);
            RenderUtil.drawRect((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 35 + 12 + this.y),(float)(this.mainx + 91) + this.pos.x+ 70.0F,(float)(this.mainy + 35 + 12 + this.y)+ (float) this.setting.getModes().length * 12.0F, new Color(5,23,37).getRGB());
            Enum[] var5 = this.setting.getModes();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Enum option = var5[var7];
                if (option.equals(this.setting.getValue())) {
                	 RenderUtil.drawRect((float)(this.mainx + 91 + 69) + this.pos.x, (float)(this.mainy + 38 + 11 + this.y + this.setting.getModeListinde(String.valueOf(option)) * 12),(float)(this.mainx + 91 + 69) + this.pos.x+ 1.0F,(float)(this.mainy + 38 + 11 + this.y + this.setting.getModeListinde(String.valueOf(option)) * 12)+ 8.0F,   new Color(5,166,238).getRGB());
                }

                FontLoaders.NL16.drawString(String.valueOf(option), (float)(this.mainx + 93) + this.pos.x, (float)(this.mainy + 38 + 12 + this.y + this.setting.getModeListinde(String.valueOf(option)) * 12), new Color(130,140,150).getRGB());
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
            }

            GL11.glTranslatef(0.0F, 0.0F, -2.0F);
        }

    }
    
    

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && RenderUtil.isHovering((float)(this.mainx + 91) + this.pos.x, (float)(this.mainy + 35 + this.y), 70.0F, 12.0F, mouseX, mouseY)) {
            this.setting.openList = !this.setting.openList;
        }

        if (mouseButton == 0 && this.setting.openList && (float)mouseX >= (float)(this.mainx + 91) + this.pos.x && (float)mouseX <= (float)(this.mainx + 91) + this.pos.x + 70.0F) {
            for(int i = 0; i < this.setting.getModes().length; ++i) {
                int v = this.mainy + 38 + 12 + this.y + i * 12;
                if (mouseY >= v && mouseY <= v + 12) {
                    this.setting.setValue(this.setting.getModeGet(i));
                }
            }
        }

    }
	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
