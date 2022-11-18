package cn.BokerLite.gui.clickgui;

import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class ChineseModule extends Module {
    public ChineseModule() {
        super("中文模式", "Chinese Mode", Keyboard.KEY_NONE, ModuleType.Render, "Render Chinese",ModuleType.SubCategory.PLayer_Player);
    }

    @Override
    public void enable() {
        Client.chinese = true;
    }

    @Override
    public void disable() {
        Client.chinese = false;
    }
}
