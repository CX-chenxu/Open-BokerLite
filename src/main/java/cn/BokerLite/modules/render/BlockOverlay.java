package cn.BokerLite.modules.render;

import net.minecraft.block.BlockAir;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Colors;
import cn.BokerLite.utils.render.RenderHelper;

import java.awt.*;

public class BlockOverlay extends Module {

    public BlockOverlay() {
        super("BlockOverlay", "方块碰撞箱", Keyboard.KEY_NONE, ModuleType.Render, "Show your block hitbox overlay",ModuleType.SubCategory.RENDER_OVERLAY);
    }

    @SubscribeEvent
    public void onRender3D(final RenderWorldLastEvent event) {
        try {
            if (mc.objectMouseOver == null)
                return;
            if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
                return;
            if (mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir)
                return;
            RenderHelper.drawBlockESP(mc.objectMouseOver.getBlockPos(),  (new Color(0,125,227)).getRGB());
        } catch (NullPointerException exception) {
//            exception.printStackTrace();
        }
    }
}
