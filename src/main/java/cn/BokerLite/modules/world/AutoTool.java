package cn.BokerLite.modules.world;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.block.BlockUtils;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTools", "自动切换工具",Keyboard.KEY_NONE, ModuleType.World, "Switch correct tools when you destroy blocks",ModuleType.SubCategory.World_World);
    }
    
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!this.state)
            return;
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        if (mc.objectMouseOver == null) {
            return;
        }
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockUtils.updateTool(pos);
    }
}
