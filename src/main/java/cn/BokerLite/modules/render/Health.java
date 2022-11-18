package cn.BokerLite.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.mod.MessageUtils;

public class Health extends Module {
    int fuck = 0;


    public Health() {
        super("Health", "生命显示", Keyboard.KEY_NONE, ModuleType.Render, "Rendering your health",ModuleType.SubCategory.RENDER_MODEL);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (mc.thePlayer.getHealth() >= 0.0f && mc.thePlayer.getHealth() < 6.0f && fuck == 0) {
            fuck = 1;
            MessageUtils.send("Health", "Your HP is low!", NotificationType.ERROR);
        } else if (mc.thePlayer.getHealth() >= 0.0f && mc.thePlayer.getHealth() > 6.0f && fuck == 1) {
            fuck = 0;
        }
    }

}
