package cn.BokerLite.modules.move;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class LiquidWalk extends Module {
    public LiquidWalk() {
        super("LiquidWalk", "水上行走", Keyboard.KEY_NONE, ModuleType.Movement, "Allow you walk on liquid",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent e) {
        if (mc.thePlayer.isInWater()) {
            mc.thePlayer.motionY = 0.2F;
        }
    }
}