package cn.BokerLite.modules.render;

import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.combat.LegitAura;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Option;

import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ESP extends Module {
    public static Mode<Enum<ESPMode>> mode = new Mode<>("Mode","Mode", ESPMode.values(), ESPMode.ESP2D);

    public Mode<Enum<TwoDMode>> True2Dmode = new Mode<>("2DMode","2DMode",   TwoDMode.values(), TwoDMode.Box);
    public static Option<Boolean> HEALTH = new Option<Boolean>("Health", "Health","Health",true);
    public static Option<Boolean> ARMOR = new Option<Boolean>("Armor", "Armor","Armor", true);
    //    public static Option<Boolean> TEXT = new Option<Boolean>("Text",  true);
    public static Option<Boolean> Black = new Option<Boolean>("Black",  "Black", "Black", false);

    public ESP() {
        super("ESP", "超感觉知觉", Keyboard.KEY_NONE, ModuleType.Render, "Draw boxes for other player and item",ModuleType.SubCategory.RENDER_OVERLAY);
    }
    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(i, h);
        GL11.glVertex2d(g, h);
        GL11.glVertex2d(g, j);
        GL11.glVertex2d(i, j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public void renderBox(Entity entity) {
        mc.getRenderManager();
        double x = entity.lastTickPosX
                + (entity.posX - entity.lastTickPosX) * Client.getTimer().renderPartialTicks
                - ( mc.getRenderManager()).viewerPosX;
        mc.getRenderManager();
        double y = entity.lastTickPosY
                + (entity.posY - entity.lastTickPosY) * Client.getTimer().renderPartialTicks
                - ( mc.getRenderManager()).viewerPosY;
        mc.getRenderManager();
        double z = entity.lastTickPosZ
                + (entity.posZ - entity.lastTickPosZ) * Client.getTimer().renderPartialTicks
                - ( mc.getRenderManager()).viewerPosZ;
        double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - 0.1;
        double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY
                + 0.25;
        RenderUtil.drawEntityESP(x, y, z, width, height, 1f,
                1f, 1f, 0.2f, 1f,
                1f, 1f, 0f, 1f);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mode.getValue() == ESPMode.Box) {
            for (Object o : mc.theWorld.loadedEntityList) {
                if (o instanceof EntityPlayer) {
                    EntityPlayer ent = (EntityPlayer) o;
                    if (ent != mc.thePlayer && !ent.isDead) {
                        if (isValid(ent))
                            renderBox(ent);
                    }
                }
            }
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        return entity != mc.thePlayer && (!(entity.getHealth() <= 0.0F) && (entity instanceof EntityPlayer));
    }

    @SubscribeEvent
    public void doOther2DESP(RenderWorldLastEvent e) {
        if (mode.getValue() == ESPMode.Box)
            return;
        Matrix4f mvMatrix = getMatrix(GL11.GL_MODELVIEW_MATRIX);
        Matrix4f projectionMatrix = getMatrix(GL11.GL_PROJECTION_MATRIX);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1.0f, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);

        GL11.glLineWidth(1.0f);

        for (final Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && !AntiBot.isServerBot(entity) && ((LegitAura.players.getValue() && entity instanceof EntityPlayer) || (LegitAura.mobs.getValue() && entity instanceof EntityMob) || (LegitAura.animals.getValue() && entity instanceof EntityAnimal))) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entity;
                final RenderManager renderManager = mc.getRenderManager();
                final Timer timer = Client.getTimer();

                AxisAlignedBB bb = entityLiving.getEntityBoundingBox()
                        .offset(-entityLiving.posX, -entityLiving.posY, -entityLiving.posZ)
                        .offset(entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks,
                                entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks,
                                entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks)
                        .offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);

                double[][] boxVertices = {
                        {bb.minX, bb.minY, bb.minZ},
                        {bb.minX, bb.maxY, bb.minZ},
                        {bb.maxX, bb.maxY, bb.minZ},
                        {bb.maxX, bb.minY, bb.minZ},
                        {bb.minX, bb.minY, bb.maxZ},
                        {bb.minX, bb.maxY, bb.maxZ},
                        {bb.maxX, bb.maxY, bb.maxZ},
                        {bb.maxX, bb.minY, bb.maxZ},
                };

                float minX = Float.MAX_VALUE;
                float minY = Float.MAX_VALUE;

                float maxX = -1;
                float maxY = -1;

                for (double[] boxVertex : boxVertices) {
                    Vector2f screenPos = worldToScreen(new Vector3f((float) boxVertex[0], (float) boxVertex[1], (float) boxVertex[2]), mvMatrix, projectionMatrix, mc.displayWidth, mc.displayHeight);

                    if (screenPos == null) {
                        continue;
                    }

                    minX = Math.min(screenPos.x, minX);
                    minY = Math.min(screenPos.y, minY);

                    maxX = Math.max(screenPos.x, maxX);
                    maxY = Math.max(screenPos.y, maxY);
                }
                float wid = 0.5f;
                if(mc.gameSettings.thirdPersonView != 0){
                    wid = 1.f;
                }
                if ((minX > 0 || minY > 0 || maxX <= mc.displayWidth || maxY <= mc.displayWidth) && maxX > 8) {
                    float health = entityLiving.getHealth();
                    float maxHealth = entityLiving.getMaxHealth();

                    int RColor;
                    RColor = 0;
                    int GColor;
                    GColor = 0;
                    int BColor;
                    BColor = 0;
                    //Draw Hp text
//                    if(HEALTH.getValue() && TEXT.getValue()) {
//                        mc.fontRendererObj.drawString((int) health + "hp", (int) (minX - 7.5f - FontLoaders.F24.getStringWidth((int) health + "hp")), (int) (maxY + (minY - maxY) / maxHealth * health),new Color(250,250,250,255).getRGB());
//                    }
                    //draw out line
                    if(True2Dmode.getValue() == TwoDMode.CornerA) {
                        //Black
                        glColor4f(0f, 0f, 0f, 1.0f);
                        glLineWidth(2.5f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, minY - 1);
                        glVertex2f(minX, (maxY - minY) / 3 + minY + 1f);
                        glEnd();
                        glColor4f(0f, 0f, 0f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX - 1, minY);
                        glVertex2f((maxX - minX) / 3 + minX + 1f, minY);
                        glEnd();
                        //Write
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glLineWidth(wid);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, minY);
                        glVertex2f(minX, (maxY - minY) / 3 + minY);
                        glEnd();
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, minY);
                        glVertex2f((maxX - minX) / 3 + minX, minY);
                        glEnd();
                        //black
                        glColor4f(0f / 255f, 0f / 255f, 0f / 255f, 1.0f);
                        glLineWidth(2.5f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX - 1, minY);
                        glVertex2f(maxX - (maxX - minX) / 3 - 1, minY);
                        glEnd();
                        glColor4f(0f / 255f, 0f / 255f, 0f / 255f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, minY - 1);
                        glVertex2f(maxX, (maxY - minY) / 3 + minY + 1);
                        glEnd();
                        //write
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glLineWidth(wid);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, minY);
                        glVertex2f(maxX - (maxX - minX) / 3, minY);
                        glEnd();
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, minY);
                        glVertex2f(maxX, (maxY - minY) / 3 + minY);
                        glEnd();
                        //black
                        glColor4f(0f, 0f, 0f, 1.0f);
                        glLineWidth(2.5f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX + 1, maxY);
                        glVertex2f(maxX - (maxX - minX) / 3 - 1, maxY);
                        glEnd();
                        glColor4f(0f, 0f, 0f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, maxY + 1);
                        glVertex2f(maxX, maxY - (maxY - minY) / 3 - 1);
                        glEnd();
                        //write
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glLineWidth(wid);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, maxY);
                        glVertex2f(maxX - (maxX - minX) / 3, maxY);
                        glEnd();
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(maxX, maxY);
                        glVertex2f(maxX, maxY - (maxY - minY) / 3);
                        glEnd();
                        //black
                        glColor4f(0f / 255f, 0f / 255f, 0f / 255f, 1.0f);
                        glLineWidth(2.5f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX - 1, maxY);
                        glVertex2f((maxX - minX) / 3 + minX + 1, maxY);
                        glEnd();
                        glColor4f(0f / 255f, 0f / 255f, 0f / 255f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, maxY + 1);
                        glVertex2f(minX, maxY - (maxY - minY) / 3 - 1);
                        glEnd();
                        //write
                        glColor4f(255f, 255f, 255f, 1.0f);
                        glLineWidth(wid);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, maxY);
                        glVertex2f((maxX - minX) / 3 + minX, maxY);
                        glEnd();
                        glColor4f(1f, 1f, 1f, 1.0f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX, maxY);
                        glVertex2f(minX, maxY - (maxY - minY) / 3);
                        glEnd();

                    }else if(True2Dmode.getValue() == TwoDMode.Box){
                        //Black
                        glColor4f(0f / 255f, 0f / 255f, 0f / 255f, 1.0f);
                        glLineWidth(2.5f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX , minY);
                        glVertex2f(maxX, minY);
                        glVertex2f(maxX, maxY);
                        glVertex2f(minX, maxY);
                        glEnd();
                        //White
                        glColor4f(255f, 255f, 255f, 1.0f);
                        glLineWidth(wid);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX , minY);
                        glVertex2f(maxX, minY);
                        glVertex2f(maxX, maxY);
                        glVertex2f(minX, maxY);
                        glEnd();
                    }
                    if(Black.getValue()){
                        glColor4f(0f, 0f, 0f, 0.35f);
                        glBegin(GL_QUAD_STRIP);
                        glVertex2f(maxX - 1, maxY - 1);
                        glVertex2f(maxX - 1, minY + 1);
                        glVertex2f(minX + 1, maxY - 1);
                        glVertex2f(minX + 1, minY + 1);
                        glEnd();
                    }
                    //set health bar color
                    if(HEALTH.getValue()) {
                        if (entityLiving.getHealth() > maxHealth * 0.9) {
                            RColor = 0;
                            GColor = 115;
                            BColor = 0;
                        } else if (entityLiving.getHealth() >  maxHealth * 0.8 && entityLiving.getHealth() <=  maxHealth * 0.9) {
                            RColor = 69;
                            GColor = 139;
                            BColor = 0;
                        } else if (entityLiving.getHealth() > maxHealth * 0.7 && entityLiving.getHealth() <=  maxHealth * 0.8) {
                            RColor = 105;
                            GColor = 139;
                            BColor = 34;
                        } else if (entityLiving.getHealth() > maxHealth * 0.6 && entityLiving.getHealth() <= maxHealth * 0.7) {
                            RColor = 110;
                            GColor = 139;
                            BColor = 61;
                        } else if (entityLiving.getHealth() > maxHealth * 0.5 && entityLiving.getHealth() <= maxHealth * 0.6) {
                            RColor = 115;
                            GColor = 139;
                            BColor = 81;
                        } else if (entityLiving.getHealth() > maxHealth * 0.4 && entityLiving.getHealth() <= maxHealth * 0.5) {
                            RColor = 130;
                            GColor = 139;
                            BColor = 81;
                        } else if (entityLiving.getHealth() > maxHealth * 0.3 && entityLiving.getHealth() <= maxHealth * 0.4) {
                            RColor = 149;
                            GColor = 139;
                            BColor = 71;
                        } else if (entityLiving.getHealth() > maxHealth * 0.2 && entityLiving.getHealth() <= maxHealth * 0.3) {
                            RColor = 184;
                            GColor = 134;
                            BColor = 61;
                        } else if (entityLiving.getHealth() > 0f && entityLiving.getHealth() <= maxHealth * 0.2) {
                            RColor = 139;
                            GColor = 69;
                            BColor = 19;
                        }
                        //draw health bar
                        if ((maxY + (minY - maxY) / entityLiving.getMaxHealth() * entityLiving.getHealth()) < minY) {
                            glColor4f(20 / 255f, 20 / 255f, 20 / 255f, 100f / 255f);
                            glLineWidth(2f);
                            glBegin(GL_LINE_LOOP);
                            glVertex2f(minX - 6, maxY + (minY - maxY) / maxHealth * entityLiving.getHealth());
                            glVertex2f(minX - 6, maxY);
                            glEnd();
                            glColor4f(20 / 255f, 20 / 255f, 20 / 255f, 160f / 255f);
                            glLineWidth(1f);
                            glBegin(GL_LINE_LOOP);
                            glVertex2f(minX - 7.5f, maxY + (minY - maxY) / maxHealth * entityLiving.getHealth() - 1);
                            glVertex2f(minX - 4.5f, maxY + (minY - maxY) / maxHealth * entityLiving.getHealth() - 1);
                            glVertex2f(minX - 4.5f, maxY + 1);
                            glVertex2f(minX - 7.5f, maxY + 1);
                            glEnd();
                        } else {
                            glColor4f(20 / 255f, 20 / 255f, 20 / 255f, 100 / 255f);
                            glLineWidth(2f);
                            glBegin(GL_LINE_LOOP);
                            glVertex2f(minX - 6, minY);
                            glVertex2f(minX - 6, maxY);
                            glEnd();
                            glColor4f(0 / 255f, 0 / 255f, 0 / 255f, 160 / 255f);
                            glLineWidth(1f);
                            glBegin(GL_LINE_LOOP);
                            glVertex2f(minX - 7.5f, minY - 1);
                            glVertex2f(minX - 4.5f, minY - 1);
                            glVertex2f(minX - 4.5f, maxY + 1);
                            glVertex2f(minX - 7.5f, maxY + 1);
                            glEnd();
                        }
                        glColor4f(RColor / 255f, GColor / 255f, BColor / 255f, 1f);
                        glLineWidth(2f);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(minX - 6, maxY);
                        glVertex2f(minX - 6, maxY + (minY - maxY) / maxHealth * health);
                        glEnd();
                        for (int i = 0; i <= 10; i++) {
                            if (entityLiving.getHealth() > maxHealth) {
                                GL11.glColor4f(0 / 255f, 0 / 255f, 0 / 255f, 80 / 255f);
                                GL11.glLineWidth(0.5f);
                                GL11.glBegin(GL11.GL_LINE_LOOP);
                                GL11.glVertex2f(minX - 7.5f, maxY + (minY - maxY) / maxHealth * entityLiving.getHealth() / 10 * i);
                                GL11.glVertex2f(minX - 4.5f, maxY + (minY - maxY) / maxHealth * entityLiving.getHealth() / 10 * i);
                                GL11.glEnd();
                            } else {
                                GL11.glColor4f(0 / 255f, 0 / 255f, 0 / 255f, 80 / 255f);
                                GL11.glLineWidth(0.5f);
                                GL11.glBegin(GL11.GL_LINE_LOOP);
                                GL11.glVertex2f(minX - 7.5f, minY + (maxY - minY) / 10 * i);
                                GL11.glVertex2f(minX - 4.5f, minY + (maxY - minY) / 10 * i);
                                GL11.glEnd();
                            }
                        }
                    }
                    if(ARMOR.getValue()) {
                        //draw armor part
                        for (int i = 4; i > 0; i--) {
                            if (entityLiving.getEquipmentInSlot(i) != null) {
                                float posY = maxY + (minY - maxY) / 4 * (i - 1) - (maxY - minY) / 40;
                                float maxPosY = maxY + (minY - maxY) / 4 * i + (maxY - minY) / 40;
                                glColor4f(65 / 255f, 105 / 255f, 225 / 255f, 1.0f);
                                glLineWidth(3f);
                                glBegin(GL_LINE_LOOP);
                                glVertex2f(maxX + 6, posY);
                                glVertex2f(maxX + 6, maxPosY);
                                glEnd();
                                glColor4f(0 / 255f, 0 / 255f, 0 / 255f, 255 / 255f);
                                glLineWidth(1f);
                                glBegin(GL_LINE_LOOP);
                                glVertex2f(maxX + 8f, posY);
                                glVertex2f(maxX + 8f, maxPosY);
                                glVertex2f(maxX + 4f, maxPosY);
                                glVertex2f(maxX + 4f, posY);
                                glEnd();

//                                //draw armor text
//                                if(TEXT.getValue()) {
//                                    float amror = entityLiving.getEquipmentInSlot(i).getMaxDamage() - entityLiving.getEquipmentInSlot(i).getItemDamage();
//                                    mc.fontRendererObj.drawString(String.valueOf((int) amror), (int) (maxX + 9f), (int) (posY + (maxPosY - posY) / 2), Color.WHITE.getRGB());
//                                }
                            }
                        }
                    }
                }
            }
        }
        glEnable(GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();

        GL11.glPopAttrib();

    }

    enum ESPMode {
        Box,
        ESP2D
    }

    public static void startgl(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1.0f, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }

    public static void finishgl(){
        glEnable(GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();

        GL11.glPopAttrib();
    }

    public static Matrix4f getMatrix(int matrix) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(matrix, floatBuffer);

        return (Matrix4f) new Matrix4f().load(floatBuffer);
    }

    public static Vector4f multiply(Vector4f vec, Matrix4f mat) {
        return new Vector4f(
                vec.x * mat.m00 + vec.y * mat.m10 + vec.z * mat.m20 + vec.w * mat.m30,
                vec.x * mat.m01 + vec.y * mat.m11 + vec.z * mat.m21 + vec.w * mat.m31,
                vec.x * mat.m02 + vec.y * mat.m12 + vec.z * mat.m22 + vec.w * mat.m32,
                vec.x * mat.m03 + vec.y * mat.m13 + vec.z * mat.m23 + vec.w * mat.m33
        );
    }

    public static Vector2f worldToScreen(Vector3f pointInWorld, Matrix4f view, Matrix4f projection, int screenWidth, int screenHeight) {
        Vector4f clipSpacePos = multiply(multiply(new Vector4f(pointInWorld.x, pointInWorld.y, pointInWorld.z, 1.0f), view), projection);

        Vector3f ndcSpacePos = new Vector3f(clipSpacePos.x / clipSpacePos.w, clipSpacePos.y / clipSpacePos.w, clipSpacePos.z / clipSpacePos.w);

//        System.out.println(pointInNdc);

        float screenX = ((ndcSpacePos.x + 1.0f) / 2.0f) * screenWidth;
        float screenY = ((1.0f - ndcSpacePos.y) / 2.0f) * screenHeight;

        // nPlane = -1, fPlane = 1
        if (ndcSpacePos.z < -1.0 || ndcSpacePos.z > 1.0) {
            return null;
        }

        return new Vector2f(screenX, screenY);
    }

    public enum TwoDMode {
        Box, CornerA, CornerB
    }

}
