package cn.BokerLite.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MoveUtils {
    public static boolean isMoving(){
        return Minecraft.getMinecraft().thePlayer != null && (Minecraft.getMinecraft().thePlayer.movementInput.moveForward != 0f || Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe != 0f);
    }
    public static void strafe(float speed) {
        //Minecraft mc= Minecraft.getMinecraft();
        if (!isMoving()) {
            return;
        }

                double yaw = getdirection();
        Minecraft.getMinecraft().thePlayer.motionX = -sin(yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = cos(yaw) * speed;
    }
    
    public static boolean isBlockUnder() {
    	Minecraft mc= Minecraft.getMinecraft();
        if (mc.thePlayer == null) return false;

        if (mc.thePlayer.posY < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)mc.thePlayer.posY + 2; off += 2) {
            final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0.0, -off, 0.0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

   public static double getdirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0f) rotationYaw += 180f;
        float forward = 1f;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0f){
            forward = -0.5f;
        }else if (Minecraft.getMinecraft().thePlayer.moveForward > 0f){
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
    }
    public static void handleVanillaKickBypass() {
        double ground = calculateGround();
        try {
            double posY = Minecraft.getMinecraft().thePlayer.posY;
            while (posY > ground) {
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY, Minecraft.getMinecraft().thePlayer.posZ, true));
                if (posY - 8.0 < ground){
                    return;
                }// Prevent next step
                posY-=8.0;
            }
        }catch (Exception e){

        }
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, ground, Minecraft.getMinecraft().thePlayer.posZ, true));
        double posY = ground;
        while (posY < Minecraft.getMinecraft().thePlayer.posY) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY, Minecraft.getMinecraft().thePlayer.posZ, true));
            if (posY + 8.0 > Minecraft.getMinecraft().thePlayer.posY){
                return;
            } // Prevent next step
            posY += 8.0;
        }
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, true));
    }
    public static double calculateGround(){
        AxisAlignedBB playerBoundingBox = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox();
        double blockHeight = 1.0;
        double ground = Minecraft.getMinecraft().thePlayer.posY;
        while (ground > 0.0) {
            AxisAlignedBB customBox =new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (Minecraft.getMinecraft().theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight;
                ground += blockHeight;
                blockHeight = 0.05;
            }
            ground -= blockHeight;
        }
        return 0.0;
    }
}
