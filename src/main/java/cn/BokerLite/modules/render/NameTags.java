package cn.BokerLite.modules.render;


import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.combat.Teams;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.fontRenderer.CFont.CFontRenderer;
import cn.BokerLite.utils.friend.FriendManager;
import cn.BokerLite.utils.render.ColorUtils;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import net.minecraft.client.renderer.entity.RenderManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.entity.player.EntityPlayer;


import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class NameTags extends Module {

    public Option<Boolean> armor = new Option<>("Armor", "Armor", "Armor", true);
    public Option<Boolean> font = new Option<>("Font","Font","Font",true);
    public Option<Boolean> scaleing = new Option<Boolean>("Scale", "Scale","Scale",true);
    public Option<Boolean> player_only = new Option<>("Player Only","Player Only","Player Only", false);
    public Mode<Enum<Modes>> mode = new Mode<>("Mode","Mode",Modes.values(),Modes.Normal);

    private final Numbers factor = new Numbers("Size","Size","Size", 1.0, 0.1, 3.0, 0.1);

    public NameTags() {
        super("NameTags","", Keyboard.KEY_NONE, ModuleType.Render,"Render facing NameTags for targets",ModuleType.SubCategory.RENDER_MODEL);

    }

    enum Modes{
        Normal,
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if(mode.getValue() == Modes.Normal){
            for(EntityPlayer player : mc.theWorld.playerEntities) {
                if(player != null && !player.equals(mc.thePlayer) && player.isEntityAlive() ) {
                    double x = interpolate(player.lastTickPosX, player.posX, event.partialTicks) - mc.getRenderManager().viewerPosX;
                    double y = interpolate(player.lastTickPosY, player.posY, event.partialTicks) - mc.getRenderManager().viewerPosY;
                    double z = interpolate(player.lastTickPosZ, player.posZ, event.partialTicks) - mc.getRenderManager().viewerPosZ;
                    renderNameTag(player, x, y, z, event.partialTicks);
                }
            }
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5D : 0.7D);
        Entity camera = mc.getRenderViewEntity();
        cn.BokerLite.gui.FontRenderer normal = cn.BokerLite.gui.FontRenderer.F24;
        assert camera != null;
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = interpolate(camera.prevPosZ, camera.posZ, delta);

        String displayTag = getDisplayTag(player);
        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        int width = normal.getStringWidth(displayTag) / 2 +2 - 20;
        double scale = (0.0018 + factor.getValue() * (distance * factor.getValue())) / 1300.0;
//        RenderUtil.drawBlurRect(-width - 2, -(cn.BokerLite.gui.FontRenderer.F24.getFontHeight() + 1), width + 2F, 0f,12);
        if (distance <= 8) {
            scale =(0.0018 + factor.getValue() * (8 * factor.getValue())) / 1300.0;
        }

        if(!scaleing.getValue()) {
            scale = factor.getValue() / 100.0;
        }


        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1, -1500000);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableBlend();



        GlStateManager.enableBlend();

     //   RenderUtil.drawShadow(-width- 2, -(normal.getFontHeight()+ 1), width + 2F + width +2, 0f + (normal.getFontHeight() + 1));
        RenderUtil.drawRect(-width - 2, -(normal.getFontHeight() + 1), width + 2F, 2f, new Color(0,0,0,120).getRGB());
        //RenderUtil.drawRect(-width - 2, 0f, -width - 2 + player.getHealth()/player.getMaxHealth() * (width +width + 4), 2F, new Color(0,125,227,200).getRGB());
        GlStateManager.disableBlend();

        normal.drawString(displayTag, -width, -(normal.getFontHeight() - 1), this.getDisplayColour(player));

        ItemStack renderMainHand = player.getCurrentEquippedItem();
        if(renderMainHand != null && renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }

        if(renderMainHand != null && armor.getValue()) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack != null) {
                    xOffset -= 8;
                }
            }

            xOffset -= 8;
            ItemStack renderOffhand = player.getCurrentEquippedItem();
            if(renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }

            this.renderItemStack(renderOffhand, xOffset,  -(cn.BokerLite.gui.FontRenderer.F20.getFontHeight() + 1)*2);
            xOffset += 16;

            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack != null) {
                    ItemStack armourStack = stack.copy();
                    if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                        armourStack.stackSize = 1;
                    }

                    this.renderItemStack(armourStack, xOffset,  -(cn.BokerLite.gui.FontRenderer.F20.getFontHeight() + 1)*2);
                    xOffset += 16;
                }
            }

//            this.renderItemStack(renderMainHand, xOffset,  -(cn.BokerLite.gui.FontRenderer.Baloo24.getFontHeight() + 1)*2);

            GlStateManager.popMatrix();
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1, 1500000);
        GlStateManager.popMatrix();


    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
//        GlStateManager.clear(GL11.GL_ACCUM);

        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();

        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);

        mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();

        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(2F, 2F, 2F);
        GlStateManager.popMatrix();
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if(name.contains(mc.getSession().getUsername())) {
            name = "You";
        }

        float health = getHealth(player);
        String pingStr = "";
        String idString = "";

        if(Math.floor(health) == health) {
            name = EnumChatFormatting.WHITE+name+ " "+ EnumChatFormatting.GRAY+"["+ EnumChatFormatting.GREEN+(health > 0 ? (int) Math.floor(health) : "dead")+EnumChatFormatting.GRAY+"]";
        } else {
            name = EnumChatFormatting.WHITE+name + " "+ EnumChatFormatting.GRAY+"["+ EnumChatFormatting.GREEN + (health > 0 ? (int) health : "dead")+EnumChatFormatting.GRAY+"]";
        }
        return pingStr + idString + name;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = 0xFFAAAAAA;
        if(FriendManager.isFriend(player.getName())) {
            return 0xFF55C0ED;
        } else if (player.isInvisible()) {
            colour = 0xFFef0147;
        } else if (player.isSneaking()) {
            colour = 0xFF9d1995;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return (previous + (current - previous) * delta);
    }

    public static boolean isLiving(final Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static float getHealth(final Entity entity) {
        if (isLiving(entity)) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

}
