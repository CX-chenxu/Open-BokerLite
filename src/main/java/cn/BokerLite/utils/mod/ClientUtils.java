package cn.BokerLite.utils.mod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.EnumFacing;

import java.awt.*;

import static java.lang.Math.asin;

public class ClientUtils {
    public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
        EntityEgg var4 = new EntityEgg(Helper.mc.theWorld);
        var4.posX = (double) var0 + 0.5;
        var4.posY = (double) var1 + 0.5;
        var4.posZ = (double) var2 + 0.5;
        var4.posX += (double) var3.getDirectionVec().getX() * 0.25;
        var4.posY += (double) var3.getDirectionVec().getY() * 0.25;
        var4.posZ += (double) var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity(var4);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = ((float) 1 / 255) * c.getRed();
        float g = ((float) 1 / 255) * c.getGreen();
        float b = ((float) 1 / 255) * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }


    private static float[] getDirectionToEntity(Entity var0) {
        return new float[]{ClientUtils.getYaw(var0) + Helper.mc.thePlayer.rotationYaw, ClientUtils.getPitch(var0) + Helper.mc.thePlayer.rotationPitch};
    }

    public static float getYaw(Entity entity) {
        double x = entity.posX - Helper.mc.thePlayer.posX;
        double z = entity.posZ - Helper.mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.29577951308232;
        yaw = -yaw;
        return (float) yaw;
    }

    public static float getPitch(Entity entity) {
        double y = entity.posY - Helper.mc.thePlayer.posY;
        double pitch = asin(y /= Helper.mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ)) * 57.29577951308232;
        pitch = -pitch;
        return (float) pitch;
    }

    public static float getDirection() {
        float var1 = Helper.mc.thePlayer.rotationYaw;
        if (Helper.mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Helper.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Helper.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Helper.mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Helper.mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= (float) Math.PI / 180;
    }
}