package cn.BokerLite.modules.move;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.MoveUtils;
import cn.BokerLite.utils.MovementUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class InvMove extends Module {
    private final Option<Boolean> Cancel = new Option<>("Cancel", "Cancel", "Cancel", true);
    boolean nomove;
    public InvMove() {
        super("InvMove","背包移动", Keyboard.KEY_NONE, ModuleType.Movement,"Make you can move when you open inventory",ModuleType.SubCategory.MOVEMENT_EXTRAS);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] key = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                    mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump};
            KeyBinding[] array;
            for (int length = (array = key).length, i = 0; i < length; ++i) {
                KeyBinding b = array[i];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
            }
        }
    }
    @SubscribeEvent
    public void move(final TickEvent.PlayerTickEvent e) {
        if (Cancel.getValue() && this.nomove) {
            MovementUtils.setMotion( 0.0);
        }
    }

    @EventHandler
    public void onmove(final EventPacketSend ep) {
        if (Cancel.getValue()) {
            if (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiChest) {
                if (ep.getPacket() instanceof C0EPacketClickWindow) {
                    this.nomove = true;
                }
            } else {
                this.nomove = false;
            }
        }
    }
}