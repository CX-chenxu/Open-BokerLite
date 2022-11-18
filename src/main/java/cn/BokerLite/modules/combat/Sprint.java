package cn.BokerLite.modules.combat;

import cn.BokerLite.modules.ModuleManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;



public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "疾跑",Keyboard.KEY_NONE, ModuleType.Combat, "Force sprint when you moving",ModuleType.SubCategory.COMBAT_LEGIT);
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        try {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0) {

                    mc.thePlayer.setSprinting(true);

            }
        } catch (IllegalArgumentException E) {
            //
        }
    }
}
