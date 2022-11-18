package cn.BokerLite.modules.move;

import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.render.ClickGui;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.MoveUtils;
import cn.BokerLite.utils.PlayerUtil;
import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class LegitSpeed extends Module {
    public final TimerUtil timer = new TimerUtil();
    public static Numbers timerspeed = new Numbers("Timer","Timer", "Timer", 1.05, 1.00, 1.07, 0.01);
    private final Option<Boolean> strafe = new Option<Boolean>("Strafe", "Strafe","Strafe", false);
    private final Option<Boolean> jump = new Option<Boolean>("AutoJump", "AutoJump","AutoJump", true);
    public LegitSpeed() {
        super("LegitSpeed","LegitSpeed", Keyboard.KEY_NONE, ModuleType.Movement, "Void Pullback",ModuleType.SubCategory.MOVEMENT_EXTRAS);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if(!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
        } else if (mc.thePlayer.moveForward <= 0) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        }



    }
    public boolean isToJump() {
        return mc.thePlayer != null && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder();
    }


    public boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}


