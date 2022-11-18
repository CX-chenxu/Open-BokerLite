package cn.BokerLite.modules.Player;

import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;

import java.util.ArrayList;

public class Panic extends Module {
    public Panic() {
        super("Panic", "关闭所有功能", Keyboard.KEY_NONE, ModuleType.Player, "Disable all module",ModuleType.SubCategory.PLayer_Player);
    }

    @Override
    public void enable() {
        ArrayList<Module> modules = new ArrayList<>(ModuleManager.getModules());
        for (Module m : modules) {
            if (m != null) {
                m.setState(false);
            }
        }
    }
}