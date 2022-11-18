package cn.BokerLite.modules.move;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Option;

public class Step extends Module {
    private final Option<Boolean> tp = new Option<>("Teleport", "传送模式", "Teleport", false);

    public Step() {
        super("Step", "爬楼梯", Keyboard.KEY_NONE, ModuleType.Movement, "Make you steping faster",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

    @Override
    public void disable() {
        mc.thePlayer.stepHeight = 0.6f;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent e) {
        if (this.tp.getValue()) {
            mc.thePlayer.stepHeight = 0.6f;
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && mc.thePlayer.isCollided) {
                mc.thePlayer.setSprinting(true);
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.setSprinting(true);
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.753, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.setSprinting(true);
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
                Client.getTimer().timerSpeed = 0.5f;
                mc.thePlayer.setSprinting(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Client.getTimer().timerSpeed = 1.0f;
                }).start();
            }
        } else {
            mc.thePlayer.stepHeight = 1.0f;
        }
    }

}
