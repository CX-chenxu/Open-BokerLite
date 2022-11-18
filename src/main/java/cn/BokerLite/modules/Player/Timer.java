package cn.BokerLite.modules.Player;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;

public class Timer extends Module {
    private final Numbers timer = new Numbers("TimerSpeed", "Timer速度", "TimerSpeed", 1.2, 0.0, 3.0, 0.1);
    
    public Timer() {
        super("Timer","变速", Keyboard.KEY_NONE, ModuleType.World, "Make world quickly",ModuleType.SubCategory.PLayer_Player);
    }
    
    public static net.minecraft.util.Timer getTimer() {
        return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
    }
    
    public static void resetTimer() {
        try {
            getTimer().timerSpeed = 1.0F;
        } catch (NullPointerException ignored) {
        }
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (!(mc.currentScreen instanceof ClickGui)) {
            getTimer().timerSpeed = timer.getValue().floatValue();
        } else {
            resetTimer();
        }
        
    }
    
    @Override
    public void disable() {
        resetTimer();
    }
    
}