package cn.BokerLite.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.timer.MSTimer;

public class AntiAFK extends Module {
  public static Numbers delay = new Numbers("Delay", "间隔", "Delay", 500.0, 0.0, 5000.0, 100.0);
  public AntiAFK() {
        super("AntiAFK", "反挂机检测",Keyboard.KEY_NONE, ModuleType.World, "AntiAFK",ModuleType.SubCategory.World_World);
    }


    @Override
    public void disable(){
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(),false);
    }
    MSTimer antiafkTimer = new MSTimer();
    @SubscribeEvent
    public void onUpdateAntiAFK(TickEvent.PlayerTickEvent event) {
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(),true);

        if (antiafkTimer.hasTimePassed((long)delay.getValue().doubleValue())) {
            Minecraft.getMinecraft().thePlayer.rotationYaw += 180F;
            antiafkTimer.reset();
        }
    }


    }

