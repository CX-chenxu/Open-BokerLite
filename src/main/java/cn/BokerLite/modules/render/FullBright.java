package cn.BokerLite.modules.render;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class FullBright extends Module {
    private float old;
    
    public FullBright() {
        super("FullBright","亮度提升", Keyboard.KEY_NONE, ModuleType.Render, "Make the bright for night and dark",ModuleType.SubCategory.RENDER_MODEL);
    }
    
    @Override
    public void enable() {
        this.old = mc.gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 300;
    }
    
    @Override
    public void disable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = this.old;
    }
}
