package cn.BokerLite.utils;


import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MovementUtils
{
    private static final Minecraft mc;


    
    public static boolean isBlockAbovePlayer() {
        return !(MovementUtils.mc.theWorld.getBlockState(new BlockPos(MovementUtils.mc.thePlayer.posX, MovementUtils.mc.thePlayer.getEntityBoundingBox().maxY + 0.41999998688697815, MovementUtils.mc.thePlayer.posZ)).getBlock() instanceof BlockAir);
    }
    
    public static void setPos(final double x, final double y, final double z) {
        MovementUtils.mc.thePlayer.setPosition(MovementUtils.mc.thePlayer.posX + x, MovementUtils.mc.thePlayer.posY + y, MovementUtils.mc.thePlayer.posZ + z);
    }
    
    public static double getBaseMoveSpeed3(final double speed) {
        double baseSpeed = speed;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            final int amplifier = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static boolean isOnGround() {
        return MovementUtils.mc.thePlayer.onGround && MovementUtils.mc.thePlayer.isCollidedVertically;
    }
    
    public static boolean isMoving() {
        return MovementUtils.mc.thePlayer != null && (MovementUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isOnGround(final double height) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getSpeedEffect() {
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    

    
    public static void setSpeed(final double moveSpeed, final double v1) {
        double forward = MovementUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.thePlayer.motionX = 0.0;
            MovementUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtils.mc.thePlayer.motionX = (forward * moveSpeed * Math.cos(Math.toRadians(yaw + 88.0)) + strafe * moveSpeed * Math.sin(Math.toRadians((yaw += (float)W(-2.5, 2.5)) + 87.9000015258789))) * v1;
            MovementUtils.mc.thePlayer.motionZ = (forward * moveSpeed * Math.sin(Math.toRadians(yaw + 88.0)) - strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 87.9000015258789))) * v1;
        }
    }
    public static void setSpeed(double d, float f, double d2, double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;



        if (d4 == 0.0 && d5 == 0.0) {
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.motionX = 0.0;
        } else {
            if (d4 != 0.0) {
                if (d5 > 0.0) {
                    f2 += (float) (d4 > 0.0 ? -45 : 45);
                } else if (d5 < 0.0) {
                    f2 += (float) (d4 > 0.0 ? 45 : -45);
                }
                d5 = 0.0;
                if (d4 > 0.0) {
                    d4 = 1.0;
                } else if (d4 < 0.0) {
                    d4 = -1.0;
                }
            }
            double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
            double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
            mc.thePlayer.motionX = d4 * d * d6 + d5 * d * d7;
            mc.thePlayer.motionZ = d4 * d * d7 - d5 * d * d6;
        }
    }



    public static void setSpeed(TickEvent.PlayerTickEvent eventMove, double d, double d2) {
        double d3 = MovementUtils.mc.thePlayer.movementInput.moveForward;
        double d4 = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        float f = MovementUtils.mc.thePlayer.rotationYaw;
        if (d3 == 0.0 && d4 == 0.0) {
            eventMove.player.motionX=0.0;
            eventMove.player.motionZ=0.0;
        } else {
            if (d3 != 0.0) {
                if (d4 > 0.0) {
                    f += (float)(d3 > 0.0 ? -45 : 45);
                } else if (d4 < 0.0) {
                    f += (float)(d3 > 0.0 ? 45 : -45);
                }
                d4 = 0.0;
                if (d3 > 0.0) {
                    d3 = 1.0;
                } else if (d3 < 0.0) {
                    d3 = -1.0;
                }
            }
            double d5 = Math.sin(Math.toRadians(f));
            double d6 = Math.cos(Math.toRadians(f));
            eventMove.player.motionX = ((d3 * d * -d5 + d4 * d * d6) * d2);
            eventMove.player.motionZ =((d3 * d * d6 - d4 * d * -d5) * d2);
        }
    }
    public static double W(final double a, final double a2) {
        final Random a3 = new Random();
        double a4 = a3.nextDouble() * (a2 - a);
        if (a4 > a2) {
            a4 = a2;
        }
        final double a5;
        if ((a5 = a4 + a) <= a2) {
            return a5;
        }
        return a2;
    }
    
    public static void setMotion(final double speed) {
        double forward = MovementUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        double yaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.thePlayer.motionX = 0.0;
            MovementUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -35 : 35);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 35 : -35);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double v9 = Math.sin(Math.toRadians(yaw));
            final double v10 = Math.cos(Math.toRadians(yaw));
            MovementUtils.mc.thePlayer.motionX = forward * speed * -v9 + strafe * speed * v10;
            MovementUtils.mc.thePlayer.motionZ = forward * speed * v10 - strafe * speed * -v9;
        }
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight, final boolean potionJumpHeight) {
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump) && potionJumpHeight) {
            final int amplifier = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static boolean isInLiquid() {
        if (MovementUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int)MovementUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(MovementUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(MovementUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(MovementUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(MovementUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = MovementUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static double getJumpHeight(final double baseJumpHeight) {
        if (isInLiquid()) {
            return 0.13499999955296516;
        }
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return baseJumpHeight + (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static double getJumpHeight() {
        return getJumpHeight(0.41999998688697815);
    }
    
    public static float getSensitivityMultiplier() {
        final float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }
    

    

    
    static {
        mc = Minecraft.getMinecraft();
    }
}
