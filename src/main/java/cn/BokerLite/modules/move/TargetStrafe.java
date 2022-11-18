/*
 * Decompiled with CFR 0_132.
 */
package cn.BokerLite.modules.move;

import java.awt.Color;

import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.LegitAura;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.MovementUtils;
import cn.BokerLite.utils.RotationUtils;
import cn.BokerLite.utils.render.GLUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;




import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class TargetStrafe extends Module {
    private static final Numbers radius = new Numbers("Radius", "Radius","Radius", 2.0, 1.0, 3.0, 0.25);
    private static final Numbers lol = new Numbers("Sides", "Sides","Sides", 9.0, 5.0, 25.0, 1.0);
    private static final Option<Boolean> render = new Option<Boolean>("Render", "Render", "Render", true);
    private static final Option<Boolean> space = new Option<Boolean>("Space", "Space","Space", true);
    private static final Option<Boolean> third = new Option<Boolean>("ThirdPersonView","ThirdPersonView",  "ThirdPersonView", false);

    private static int direction = -1;
    private static boolean strafing = false;
    //    private static Aura aura;
    static int preview;

    public TargetStrafe() {
        super("TargetStrafe", "杀戮转圈", Keyboard.KEY_NONE, ModuleType.Movement, "Strafe for killaura target",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

//    @Override
//    public void onEnable() {
//    	//Chocola.protect();
//        if (this.aura == null) {
//            this.aura = (Aura) ModuleManager.getModuleByClass(Aura.class);
//        }
//    }

 

  

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {

        if (!canStrafe() && strafing) {
            strafing = false;
            mc.gameSettings.thirdPersonView = preview;
        }
        if (mc.thePlayer.isCollidedHorizontally || isAboveVoid()) {
            this.switchDirection();
        }
        if (mc.gameSettings.keyBindLeft.isPressed()) {
            direction = 1;
        }
        if (mc.gameSettings.keyBindRight.isPressed()) {
            direction = -1;
        }
        if (strafing && third.getValue()) {
            mc.gameSettings.thirdPersonView = 1;
        }
    }

    private void switchDirection() {
        direction = direction == 1 ? -1 : 1;
    }

    public static void strafe( double moveSpeed) {
        if (!strafing) {
            preview = mc.gameSettings.thirdPersonView;
        }
        strafing = true;
        EntityLivingBase target = LegitAura.target;
        float[] rotations = RotationUtils.getRotations(target);

        if ((double) mc.thePlayer.getDistanceToEntity(target) <= TargetStrafe.radius.getValue()) {
            MovementUtils.setSpeed( moveSpeed, rotations[0], TargetStrafe.direction, 0.0);
        } else {
            MovementUtils.setSpeed( moveSpeed, rotations[0], TargetStrafe.direction, 1.0);
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event){
        if (canStrafe() && render.getValue().booleanValue()) {
//            this.drawCircle(Aura.target, e.getPartialTicks(), (Double)this.radius.getValue());
            drawCircle2(LegitAura.target, new Color(255, 255, 255).getRGB(), event);
        }
//        EntityLivingBase entity = mc.thePlayer;
//        drawCylinderESP(entity, new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB(),e);
    }
    public static void drawCircle2(EntityLivingBase entity, int color, RenderWorldLastEvent e) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) e.partialTicks
                - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) e.partialTicks
                - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) e.partialTicks
                - mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-entity.width, 0.0f, 1.0f, 0.0f);
        glColor(color);
        enableSmoothLine(1.0f);
        Cylinder c = new Cylinder();
        GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        c.setDrawStyle(100011);
        c.draw(radius.getValue().floatValue(), radius.getValue().floatValue() + 0.005f, 0.005f,
                lol.getValue().intValue(), 1);
        c.draw(radius.getValue().floatValue() + 0.005f, radius.getValue().floatValue() + 0.01f, 0.005f,
                lol.getValue().intValue(), 1);
        c.draw(radius.getValue().floatValue() + 0.01f, radius.getValue().floatValue() + 0.015f, 0.005f,
                lol.getValue().intValue(), 1);
        c.draw(radius.getValue().floatValue() + 0.015f, radius.getValue().floatValue() + 0.02f, 0.005f,
                lol.getValue().intValue(), 1);
        c.draw(radius.getValue().floatValue() + 0.02f, radius.getValue().floatValue() + 0.025f, 0.005f,
                lol.getValue().intValue(), 1);
//        glColor(new Color(50,50,50).getRGB());
//        c.draw(radius.getValue().floatValue(), radius.getValue().floatValue() + 0.00f, 0.005f, lol.getValue().intValue(), 1);
//        c.draw(radius.getValue().floatValue() + 0.025f, radius.getValue().floatValue() + 0.025f, 0.005f, lol.getValue().intValue(), 1);
        disableSmoothLine();
        GL11.glPopMatrix();
    }

    public boolean isAboveVoid() {
        if (ModuleManager.getModule("Fly").getState())
            return false;
        for (int i = (int) Math.ceil(mc.thePlayer.posY); i >= 0; --i) {
            double var10004 = i;
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, var10004, mc.thePlayer.posZ))
                    .getBlock() != Blocks.air) {
                return false;
            }
        }

        return true;
    }

    public static boolean canStrafe() {
        return ModuleManager.getModule("LegitAura").getState() && LegitAura.target != null
                && ModuleManager.getModule("TargetStrafe").getState()
                && (ModuleManager.getModule("Speed").getState()
                || ModuleManager.getModule("Fly").getState()
                || ModuleManager.getModule("LongJump").getState())
                && (TargetStrafe.space.getValue() == false || mc.gameSettings.keyBindJump.isKeyDown());
    }



    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha == 0.0f ? 1.0f : alpha);
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}
