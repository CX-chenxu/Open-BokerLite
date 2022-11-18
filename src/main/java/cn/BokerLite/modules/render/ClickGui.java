package cn.BokerLite.modules.render;

import cn.BokerLite.gui.EV0.EV0Clickgui;
import cn.BokerLite.gui.Skeet.HyperGui;
import cn.BokerLite.gui.clickgui.GuiHelper;
import cn.BokerLite.gui.clickgui.WorstClickGui;
import cn.BokerLite.modules.combat.LegitAura;
import cn.BokerLite.modules.value.Mode;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class ClickGui extends Module {
    public static Mode<Enum<CliMode>> mode = new Mode<>("Mode", "Mode", CliMode.values(), CliMode.OneTap);
    public ClickGui() {
        super("ClickGui", "点击GUI",Keyboard.KEY_RSHIFT, ModuleType.Render,"ClickGui Rendering",ModuleType.SubCategory.RENDER_OVERLAY);
        noToggle = true;
    }
    
    @Override
    public void enable(){
        this.setState(false);
        if (mode.getValue() == CliMode.BokerLite) {
            GuiHelper.displayGuiScreen(new WorstClickGui());
        } else if (mode.getValue() == CliMode.NeverLoser) {
            GuiHelper.displayGuiScreen(new HyperGui());
        }else if (mode.getValue() == CliMode.OneTap) {
            GuiHelper.displayGuiScreen(new EV0Clickgui());
        }


    }
    enum CliMode {
        BokerLite,
        NeverLoser,
        OneTap,

    }
}
