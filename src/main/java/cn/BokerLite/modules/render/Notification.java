package cn.BokerLite.modules.render;

import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class Notification extends Module {
    public Notification() {
        super("Notification", "通知信息", Keyboard.KEY_NONE, ModuleType.Render, "Notification Rendering",ModuleType.SubCategory.RENDER_MODEL);
    }
}
