package cn.BokerLite.modules.move;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class Strafe extends Module {
    int delay = 0;
    
    public Strafe() {
        super("Strafe", "空中灵活",Keyboard.KEY_NONE, ModuleType.Movement, "Strafe",ModuleType.SubCategory.MOVEMENT_MAIN);
    }
    
    public static double[] directionSpeed(double speed) {
        float forward = mc.thePlayer.movementInput.moveForward;
        float side = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.prevRotationYaw + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * Client.getTimer().renderPartialTicks;
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double) forward * speed * cos + (double) side * speed * sin;
        double posZ = (double) forward * speed * sin - (double) side * speed * cos;
        return new double[]{posX, posZ};
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e) {
        mc.thePlayer.setSprinting(true);
        ++this.delay;
        mc.thePlayer.motionY *= 0.985;
        if (mc.thePlayer.onGround) {
            if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                mc.thePlayer.jump();
                double[] dir = directionSpeed(0.6);
                mc.thePlayer.motionX = dir[0];
                mc.thePlayer.motionZ = dir[1];
            }
        } else {
            double[] dir = directionSpeed(0.26);
            mc.thePlayer.motionX = dir[0];
            mc.thePlayer.motionZ = dir[1];
        }
    }
    
}
