package cn.BokerLite.gui.EV0;


import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.Direction;
import cn.BokerLite.gui.Skeet.Position;
import cn.BokerLite.gui.Skeet.SmoothStepAnimation;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.math.MathUtil;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubRender {
    //滚轮

    private float maxScroll = Float.MAX_VALUE;
    private final float minScroll = 0;
    private float rawScroll;

    private float scroll;
    //

    public Position pos;

    public ModuleType moduleCategory;
    public ModuleType.SubCategory subCategory;

    public CategoryRender categoryRender;

    public int y,x;

    private boolean selected;

    //主界面xy
    public int mainx,mainy;

    public int leftAdd,rightAdd;

    private final List<ModuleRender> renders = new ArrayList<>();

    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);

    public SubRender(ModuleType category,ModuleType.SubCategory subCategory,CategoryRender categoryRender,int y){
        this.moduleCategory = category;
        this.subCategory = subCategory;
        this.categoryRender = categoryRender;
        this.y = y;

        this.pos = new Position(0,0,0,0);

        int count=0;

        //new ly ry
        for (Module holder : ModuleManager.getModules()) {
            if (holder.getModuleType().equals(category) && holder.getSubCategory().equals(subCategory)) {
                float posWidth = 0;

                //判断左右分别添加
                //奇偶判断
                float posX = pos.x+((count%2 == 0) ? 0 : 157);
                float posY = pos.y+((count%2 == 0) ? leftAdd : rightAdd);

                Position pos = new Position(posX, posY, posWidth, 20);
                //只需要x y height
                ModuleRender otlM = new ModuleRender(holder,pos.x, pos.y, pos.width, pos.height);
                pos.height = otlM.height;
                if(count%2 == 0){
                    leftAdd+=pos.height+10;
                }else{
                    rightAdd+=pos.height+10;
                }
                renders.add(otlM);
                count++;
            }
        }
    }


    //420
    public void drawScreen(int mouseX, int mouseY) {
        mainx = EV0Clickgui.mainx;
        mainy = EV0Clickgui.mainy;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        //判断已选再绘制
        if (categoryRender.isSelected()) {
            if (selected) {

                //Debug
               // if (RenderUtil.isHovering(mainx,mainy,mainx + 420,mainy+ 290,mouseX,mouseY)){
               //    Fonts.SFBOLD.SFBOLD_22.SFBOLD_22.drawString("Scroll :"+getScroll(),sr.getScaledWidth()/2,20,-1,true);
              //  }

                double scrolll = getScroll();
                for (ModuleRender module : renders)
                {
                    module.scrollY = (int) MathUtil.roundToHalf(scrolll);
                }

                onScroll(30);
                //判断
                maxScroll = Math.max(0,renders.get(renders.size() -1).getY() + renders.get(renders.size() -1).height*2 + 15) ;
                //

                RenderUtil.drawRect(mainx, mainy + 31 + y, mainx + 2.5f, mainy + 50 + y, new Color(HUD.Hudcolor.getValue()).getRGB());
                RenderUtil.drawRect(mainx+ 2.5f, mainy + 31 + y, mainx + 104f, mainy + 50 + y, new Color(35,35,35,180).getRGB());

                //开始gl切割 mainx+ 105,mainy + 31,mainx + 420,mainy+ 290
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderUtil.scissor(mainx+ 105,mainy + 31,420,258);

                for (ModuleRender moduleRender: renders){
                    moduleRender.drawScreen(mouseX,mouseY);
                }
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
        }

            //绘制所选
        if (categoryRender.isSelected()) {
            FontLoaders.NL18.drawString(subCategory.toString(), selected || isHovered(mouseX, mouseY) ? mainx + 10 : mainx + 8, mainy + 38 + y, selected ? new Color(HUD.Hudcolor.getValue()).getRGB() :
                    new Color(112, 112, 112).getRGB());
        }

        }
    //滚轮
    public void onScroll(int ms) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }
    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll;
    }


    public void mouseReleased(int mouseX, int mouseY, int state) {
        renders.forEach(e ->e.mouseReleased(mouseX,mouseY,state));
    }
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        renders.forEach( e -> e.mouseClicked(mouseX,mouseY,mouseButton));
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= mainx+ 2.5f && mouseX <= mainx + 104f && mouseY >= mainy + 31 + y && mouseY <=mainy + 50 + y;
    }

    public void setSelected(boolean s){
        this.selected = s;
    }


    public void keyTyped(char typedChar, int keyCode) {
        renders.forEach(e -> e.keyTyped(typedChar,keyCode));
    }
    public boolean isSelected() {
        return this.selected;
    }


}
