package cn.BokerLite.modules.move;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk","安全行走", Keyboard.KEY_NONE, ModuleType.Movement,"Make you Safewalk",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

    @SubscribeEvent
    public void onMove(TickEvent.ClientTickEvent event) {
        double x2 = mc.thePlayer.motionX;
        double y2 = mc.thePlayer.motionY;
        double z2 = mc.thePlayer.motionZ;
        if (mc.thePlayer.onGround) {
            double increment = 0.05;
            while (x2 != 0.0) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x2, -1.0, 0.0)).isEmpty()) break;
                if (x2 < increment && x2 >= - increment) {
                    x2 = 0.0;
                    continue;
                }
                if (x2 > 0.0) {
                    x2 -= increment;
                    continue;
                }
                x2 += increment;
            }
            while (z2 != 0.0) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z2)).isEmpty()) break;
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
            while (x2 != 0.0 && z2 != 0.0 && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x2, -1.0, z2)).isEmpty()) {
                x2 = x2 < increment && x2 >= - increment ? 0.0 : (x2 > 0.0 ? (x2 -= increment) : (x2 += increment));
                if (z2 < increment && z2 >= - increment) {
                    z2 = 0.0;
                    continue;
                }
                if (z2 > 0.0) {
                    z2 -= increment;
                    continue;
                }
                z2 += increment;
            }
        }
        mc.thePlayer.motionX = x2;
        mc.thePlayer.motionY = y2;
        mc.thePlayer.motionZ = z2;
    }
}