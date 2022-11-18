package cn.BokerLite.modules.render;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class Chams extends Module {
    public Chams() {
        super("Chams", "玩家透视",Keyboard.KEY_NONE, ModuleType.Render, "Allows you to Xray other players",ModuleType.SubCategory.RENDER_OVERLAY);
    }
    
    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre e) {
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1100000.0f);
    }
    
    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Post e) {
        GL11.glDisable(32823);
        GL11.glPolygonOffset(1.0f, 1100000.0f);
    }
    
}
