package cn.BokerLite.modules.move;

import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.mod.MessageUtils;
import cn.BokerLite.utils.movement.MoveUtils;

public class LongJump extends Module {
    public Numbers range = new Numbers("Range", "距离", "Range", 4.0, 2.0, 8.0, 0.1);
    public Numbers y = new Numbers("MotionY", "Y值", "MotionY", 0.4, 0.0, 2.0, 0.1);

    public LongJump() {
        super("LongJump", "长跳", Keyboard.KEY_NONE, ModuleType.Movement, "Make your jump longer",ModuleType.SubCategory.MOVEMENT_MAIN);
        noToggle = true;
    }

    @Override
    public void enable() {
        if (Client.nullCheck())
            return;
        if(this.getKey() == Keyboard.KEY_NONE){
            MessageUtils.send("ModuleManager","LongJump must be bond!", NotificationType.WARNING);
        }else {
            Jump();
        }
        this.setState(false);
        super.enable();
    }

    public void Jump() {
        if (MoveUtils.isMoving()) {
            doStrafe(range.getValue().floatValue());
            mc.thePlayer.motionY = this.y.getValue().floatValue();
            doStrafe(range.getValue().floatValue());
        } else {
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.motionX = 0.0;
        }
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }


    public static void doStrafe(float speed) {
        if (!MoveUtils.isMoving()) {
            return;
        }
        double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * (double) speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * (double) speed;
    }
}
