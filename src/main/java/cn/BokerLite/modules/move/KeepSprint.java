package cn.BokerLite.modules.move;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class KeepSprint extends Module {
    public KeepSprint() {
        super("KeepSprint","保持疾跑", Keyboard.KEY_NONE, ModuleType.Movement,"Force sprint any time",ModuleType.SubCategory.MOVEMENT_EXTRAS);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Client.nullCheck())
            return;
        if(!mc.thePlayer.isSprinting()) mc.thePlayer.setSprinting(true);
    }
}