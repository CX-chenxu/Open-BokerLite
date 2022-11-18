package cn.BokerLite.utils.movement;

import static java.lang.Math.*;
import static cn.BokerLite.Client.mc;

public class MovementUtils {
    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f);
    }
    public static float getSpeed() {
        return (float) sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);

}
    public static void strafe(float speed) {
        if (!isMoving()){
            return;
        }
                double yaw = getDirection();
        mc.thePlayer.motionX = -sin(yaw) * speed;
        mc.thePlayer.motionZ = cos(yaw) * speed;
    }
    public static void strafe(){
        strafe(getSpeed());
    }
   public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0f) {
            rotationYaw += 180f;
        }
        float forward = 1f;
        if (mc.thePlayer.moveForward < 0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0f) {
            rotationYaw -= 90f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0f) {
            rotationYaw += 90f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}
