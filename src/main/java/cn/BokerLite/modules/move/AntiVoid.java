package cn.BokerLite.modules.move;


import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.MoveUtils;
import cn.BokerLite.utils.packet.PacketUtil;
import cn.BokerLite.utils.timer.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AntiVoid extends Module {
    public static TimeHelper timer = new TimeHelper();
    public static ArrayList<C03PacketPlayer> packets = new ArrayList<>();
    private final Numbers pullbackTime = new Numbers("Pull Back Time","Pull Back Time","Pull Back Time", 700.0, 500.0, 1500.0, 100.0);
    public double[] lastGroundPos = new double[3];

    public AntiVoid() {
        super("AntiFall","虚空回弹", Keyboard.KEY_NONE, ModuleType.Movement, "Void Pullback",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

    public static boolean isInVoid() {
        for (int i = 0; i <= 128; i++) {
            if (MoveUtils.isOnGround(i)) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (!packets.isEmpty() && mc.thePlayer.ticksExisted < 100)
            packets.clear();

        if (e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = ((C03PacketPlayer) e.getPacket());
            if (isInVoid()) {
                e.setCancelled(true);
                packets.add(packet);

                if (timer.isDelayComplete(pullbackTime.getValue().intValue())) {
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(lastGroundPos[0], lastGroundPos[1] - 1, lastGroundPos[2], true));
                }
            } else {
                lastGroundPos[0] = mc.thePlayer.posX;
                lastGroundPos[1] = mc.thePlayer.posY;
                lastGroundPos[2] = mc.thePlayer.posZ;

                if (!packets.isEmpty()) {
                    for (C03PacketPlayer p : packets)
                        PacketUtil.sendPacketWithoutEvent(p);
                    packets.clear();
                }
                timer.reset();
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onRevPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && packets.size() > 1) {
            packets.clear();
        }
    }
}


