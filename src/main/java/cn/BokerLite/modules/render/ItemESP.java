package cn.BokerLite.modules.render;

import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Colors;

import java.awt.*;

public class ItemESP extends Module {

    public ItemESP() {
        super("ItemESP","物品ESP", Keyboard.KEY_NONE, ModuleType.Render, "ESP Items",ModuleType.SubCategory.RENDER_OVERLAY);
    }

}
