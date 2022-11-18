package cn.BokerLite.gui.EV0;


import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.Direction;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRender {
    //Bot
    private final Animation targetlistani = new EaseInOutQuad(300, 400, Direction.FORWARDS) ;

    public ModuleType category;
    
    public int x,y =0 ;

    private boolean selected,renderbot;

    private boolean Loader = true;

    //主界面xy
    public int mainx,mainy;

    private final List<SubRender> sub = new ArrayList<>();
    public CategoryRender(ModuleType category, int x){
        this.x = x;
        this.category = category;
        int y2 = 0;
        for (final ModuleType.SubCategory subCategory : category.getSubCategories()) {
            sub.add(new SubRender(category,subCategory,this,y2));
            y2 +=23;
        }
    }


    public String newcatename(ModuleType moduleCategory){
        if (moduleCategory.name().equals("Combat")){
            return "RAGE";
        }else if (moduleCategory.name().equals("Player")){
            return "PLAYER";
        }else if (moduleCategory.name().equals("Movement")){
            return "MOVE";
        }else if (moduleCategory.name().equals("Render")){
            return "VISUALS";
        }else if (moduleCategory.name().equals("World")){
            return "WORLD";
        }else if (moduleCategory.name().equals("Misc")){
            return "MISC";
        }else if (moduleCategory.name().equals("Exploit")){
            return "EXPLOIT";
        }
        return "";
    }

    public void drawScreen(int mouseX, int mouseY) {
        //设置初始页面
        if (getSelectedTab() == null){
            for (SubRender categoryRender : sub){
                if (Loader){
                    categoryRender.setSelected(true);
                    Loader = false;
                }
            }
        }

        mainx = EV0Clickgui.mainx;
        mainy = EV0Clickgui.mainy;

        //绘制图标ICONFONT
        String l = "";
        if (category.name().equalsIgnoreCase("Combat")) {
            l = "D";
        }  else if (category.name().equalsIgnoreCase("Player")) {
            l = "B";
        } else if (category.name().equalsIgnoreCase("Misc")) {
            l = "F";
        }

        //CHeck icon
        String l2 = "";
        if (category.name().equalsIgnoreCase("Render")) {
            l2 = "d";
        } else if (category.name().equalsIgnoreCase("World")) {
            l2 = "b";
        } else if (category.name().equalsIgnoreCase("Exploit")) {
            l2 = "a";
        }else if (category.name().equalsIgnoreCase("Movement")) {
            l2 = "f";
        }

        FontLoaders.NLLogo20.drawString(newcatename(category),mainx +52 + x,mainy + 12,selected ? new Color(HUD.Hudcolor.getValue()).getRGB() : -1);

     //   Fonts.ICONFONT.ICONFONT_17.ICONFONT_17.drawString(l,mainx +42 + x,mainy + 13,selected ? new Color(HUD.Hudcolor.getValue()).getRGB() : -1 );
      // Fonts.CheckFont.CheckFont_17.CheckFont_17.drawString(l2,mainx +42 + x,mainy + 13,selected ? new Color(HUD.Hudcolor.getValue()).getRGB() : -1);


        for (SubRender subRender: sub){
            subRender.drawScreen(mouseX,mouseY);
        }

        if (category.name().equalsIgnoreCase("Render") && isSelected()){

            RenderUtil.drawRoundedRect(mainx + 8, mainy + 267,mainx + 8+ 90f,  mainy + 267+12, 0,new Color(40, 42, 46).getRGB());
            FontLoaders.NL20.drawString("Visual BOT",mainx + 26, mainy + 270,renderbot? new Color(HUD.Hudcolor.getValue()).getRGB() : new Color(110,110,110).getRGB());

            if (renderbot) {
                //Visual BOT
                RenderUtil.drawRoundedRect(mainx + (float) targetlistani.getOutput() + 24, mainy - 1.5f, mainx + (float) targetlistani.getOutput() + 24+25 + 140, mainy - 1.5f+3, 1, new Color(HUD.Hudcolor.getValue()).getRGB());
                RenderUtil.drawRect(mainx + (float) targetlistani.getOutput() + 23, mainy, mainx + (float) targetlistani.getOutput() + 50 + 140, mainy + 290, new Color(26, 28, 30).getRGB());

                RenderUtil.drawRect(mainx + (float) targetlistani.getOutput() + 23, mainy + 30, mainx + (float) targetlistani.getOutput() + 50 + 140, mainy + 31, new Color(51, 50, 52).getRGB());
                RenderUtil.drawRect(mainx + (float) targetlistani.getOutput() + 23, mainy + 31, mainx + (float) targetlistani.getOutput() + 50 + 140, mainy + 290, new Color(16, 16, 16).getRGB());

                FontLoaders.NL18.drawString("Visual BOT", mainx + (float) targetlistani.getOutput() + 37, mainy + 12, -1);
                //绘制人物
                if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
                    FakeEntityPlayer ac = new FakeEntityPlayer(Minecraft.getMinecraft().thePlayer.getGameProfile(), Minecraft.getMinecraft().thePlayer.getLocationSkin());
                    drawEntity((int) (mainx + (float) targetlistani.getOutput() + 108), mainy + 230, 85, ac);
                }
            }

        }
    }
    public void drawEntity(int posX, int posY,int size,EntityLivingBase entityLivingBase) {
            Minecraft mc = Minecraft.getMinecraft();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.translate((float) posX, (float) posY, 50.0F);
            GL11.glScalef(-size, size, size);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);

            float tanX = (float) Math.atan(0 / 40.0F);
            float tanY = -((float) Math.atan(0 / 40.0F));

            GlStateManager.rotate(tanY * 20.0F, 1.0F, 0.0F, 0.0F);
            entityLivingBase.renderYawOffset = tanX * 20.0F;
            entityLivingBase.rotationYaw = tanX * 40.0F;
            entityLivingBase.rotationPitch = tanY * 20.0F;
            entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
            entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
            entityLivingBase.posY = posY;
            entityLivingBase.posX =posX;
            entityLivingBase.posZ = 50;
            entityLivingBase.lastTickPosY = posY;
            entityLivingBase.lastTickPosX =posX;
            entityLivingBase.lastTickPosZ = 50;

            GlStateManager.translate(0.0F, 0.0F, 0.0F);

            try {
                RenderManager renderManager = mc.getRenderManager();
                renderManager.setPlayerViewY(180.0F);
                renderManager.setRenderShadow(false);
                renderManager.renderEntityWithPosYaw(entityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
                renderManager.setRenderShadow(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }



    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0){
            //选择面板
            for (SubRender categoryRender : sub){
                if(categoryRender.isHovered(mouseX, mouseY)) {
                    for(SubRender other : sub) {
                        //判断是否是当前已经所选
                        other.setSelected(false);
                    }
                    categoryRender.setSelected(true);
                }
            }
            if (RenderUtil.isHovering(mainx + 8, mainy + 267, 90f,  12,mouseX,mouseY)){
                renderbot = !renderbot;
            }
        }


        //判断是否是已经选择的
        SubRender selectedTab = getSelectedTab();
        if(selectedTab != null) selectedTab.mouseClicked(mouseX, mouseY, mouseButton);

    }
    public void keyTyped(char typedChar, int keyCode) {
        sub.forEach(e -> e.keyTyped(typedChar,keyCode));
    }

    public
    SubRender getSelectedTab() {
        return sub.stream().filter(SubRender::isSelected).findAny().orElse(null);
    }


    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= mainx +42 + x && mouseX <= mainx +42 + x + FontLoaders.NLLogo20.getStringWidth(newcatename(category))  && mouseY >= mainy + 13 && mouseY <=mainy + 13 + FontLoaders.NLLogo20.getHeight();
    }
    public void setSelected(boolean s){
        this.selected = s;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        sub.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
    }

    public boolean isSelected() {
        return this.selected;
    }



}
