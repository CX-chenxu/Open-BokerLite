package cn.BokerLite.utils.rotation;

import cn.BokerLite.utils.mod.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;

public class RotationUtil {
    public static float pitch() {
        return Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public static void pitch(float pitch) {
        Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
    }

    public static float yaw() {
        return Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

    public static void yaw(float yaw) {
        Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
    }
    public static float getSensitivityMultiplier() {
        float SENSITIVITY = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENSITIVITY * SENSITIVITY * SENSITIVITY * 8.0F) * 0.15F;
    }
    public static float smoothRotation(float from, float to, float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return from + f;
    }
    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var6;
        double var4 = target.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var8 = target.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase) target;
            var6 = var10.posY + (double) var10.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        new Random();
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float) (-Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
        float pitch = RotationUtil.changeRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, var13, p_706253);
        float yaw = RotationUtil.changeRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, var12, p_706252);
        return new float[]{yaw, pitch};
    }
    public static boolean canEntityBeSeen(Entity entity) {
        boolean bl;
        Vec3 vec3 = new Vec3(Helper.mc.thePlayer.posX, Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight(), Helper.mc.thePlayer.posZ);
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        Vec3 vec32 = new Vec3(entity.posX, entity.posY + (double)(entity.getEyeHeight() / 1.32f), entity.posZ);
        double d = entity.posX - 0.25;
        double d2 = entity.posX + 0.25;
        double d3 = entity.posY;
        double d4 = entity.posY + Math.abs(entity.posY - axisAlignedBB.maxY);
        double d5 = entity.posZ - 0.25;
        double d6 = entity.posZ + 0.25;
        boolean bl2 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d2, d3, d5);
        boolean bl3 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d, d3, d5);
        boolean bl4 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d, d3, d6);
        boolean bl5 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d2, d3, d6);
        boolean bl6 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d2, d4, d5);
        boolean bl7 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d, d4, d5);
        boolean bl8 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d, d4, d6 - 0.1);
        boolean bl9 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        if (bl) {
            return true;
        }
        vec32 = new Vec3(d2, d4, d6);
        boolean bl10 = bl = Helper.mc.theWorld.rayTraceBlocks(vec3, vec32) == null;
        return bl;
    }

    public static float getYawDifference(float f, double d, double d2, double d3) {
        double d4 = d - Helper.mc.thePlayer.posX;
        double d5 = d2 - Helper.mc.thePlayer.posY;
        double d6 = d3 - Helper.mc.thePlayer.posZ;
        double d7 = 0.0;
        double d8 = Math.toDegrees(Math.atan(d6 / d4));
        if (d6 < 0.0 && d4 < 0.0) {
            if (d4 != 0.0) {
                d7 = 90.0 + d8;
            }
        } else if (d6 < 0.0 && d4 > 0.0) {
            if (d4 != 0.0) {
                d7 = -90.0 + d8;
            }
        } else if (d6 != 0.0) {
            d7 = Math.toDegrees(-Math.atan(d4 / d6));
        }
        return MathHelper.wrapAngleTo180_float(-(f - (float)d7));
    }

    public static float changeRotation(float p_706631, float p_706632, float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Minecraft.getMinecraft().thePlayer.posX;
        double pY = Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double pZ = Minecraft.getMinecraft().thePlayer.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double) (entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase) entity;
            diffY = elb.posY + ((double) elb.getEyeHeight() - 0.4) - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getCollisionBoundingBox().minY + entity.getCollisionBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public static float[] grabBlockRotations(BlockPos pos) {
        return RotationUtil.getVecRotation(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), new Vec3((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5));
    }

    public static float[] getVecRotation(Vec3 position) {
        return RotationUtil.getVecRotation(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), position);
    }

    public static Vec3 flat(Vec3 v) {
        return new Vec3(v.xCoord, 0.0, v.zCoord);
    }


    public static float[] getVecRotation(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = flat(difference).lengthVector();
        float yaw = (float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static int wrapAngleToDirection(float yaw, int zones) {
        int angle = (int) ((double) (yaw + (float) (360 / (2 * zones))) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }
}

