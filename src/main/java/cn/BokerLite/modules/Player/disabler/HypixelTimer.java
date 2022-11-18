package cn.BokerLite.modules.Player.disabler;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.Player.Scaffold;
import cn.BokerLite.modules.Player.TimerUtil;
import cn.BokerLite.utils.MoveUtils;
import cn.BokerLite.utils.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class HypixelTimer extends Module {
    public final ArrayList<
            Packet> packets = new ArrayList<>();
    public final TimerUtil timer = new TimerUtil();
    TimerUtil timer1 = new TimerUtil();
    public TimerUtil moveTimer = null;
    public boolean active = false;
    TimerUtil timer2 = new TimerUtil();
    public boolean cancel;
    public HypixelTimer() {
        super("Disabler", "Watchdog关闭器", Keyboard.KEY_NONE, ModuleType.Player, "Timer bypassing helper",ModuleType.SubCategory.PLayer_Player);
    }

    @Override
    public void enable() {
        keepAlivePackets.clear();
        transactionPackets.clear();

        active = false;

        moveTimer = new TimerUtil();
        super.enable();
    }

    @Override
    public void disable() {
        keepAlivePackets.clear();
        transactionPackets.clear();
        super.disable();
    }

    public final List<C00PacketKeepAlive> keepAlivePackets = new ArrayList<>();



    public final List<C0FPacketConfirmTransaction> transactionPackets = new ArrayList<>();

    @EventHandler
    public void onPacketSend(EventPacketRecieve event){



        if (mc.isSingleplayer())return;
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C00PacketKeepAlive && packet != keepAlivePackets.get(keepAlivePackets.size() - 1)) {
            keepAlivePackets.add((C00PacketKeepAlive) packet);
            event.setCancelled(true);
        }
        if (packet instanceof C0FPacketConfirmTransaction && packet != transactionPackets.get(transactionPackets.size() - 1)) {
            transactionPackets.add((C0FPacketConfirmTransaction) packet);
            event.setCancelled(true);
        }
    }
    @SubscribeEvent
    public void onTick2(TickEvent e){
        if(moveTimer == null)
            return;
        if(moveTimer.hasTimeElapsed(10000)){
           // NotificationsManager.addNotification(new Notification("Disabler is working!", Notification.Type.Info));
         //   state = EnumChatFormatting.GREEN + "Disabler is working.";
            active = true;
            moveTimer = null;
        }
    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if(!active)
            return;
        doTimerDisabler(e);
        if (e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            if (mc.thePlayer.ticksExisted < 50) {
                e.setCancelled(true);
            }
        }


    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (timer1.hasTimeElapsed(10000, true)) {
            cancel = true;
            timer2.reset();
        }
    }

    @SubscribeEvent
    public void onMove(TickEvent.PlayerTickEvent update) {
        if (mc.thePlayer==null) return;
        if (timer1.hasTimeElapsed(10000, true)) {
            cancel = true;
            timer2.reset();
        }




    }
    public void doTimerDisabler(EventPacketSend e) {

             if (e.getPacket() instanceof C03PacketPlayer) {
                   C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
                   if (!mc.thePlayer.isUsingItem()&&!packet.isMoving()) {
                    //   System.out.println("1");
                       e.setCancelled(true);
                   }
             }
            if (cancel) {
                if (!timer2.hasTimeElapsed(400, true)) {
                    if (ModuleManager.getModule(Scaffold.class).getState()) {
//                        e.setCancel(true);
//                        packets.add(e.getPacket());
                    }
                } else {

                    packets.forEach(PacketUtil::sendPacketWithoutEvent);
                    packets.clear();
                    cancel = false;
                }

        }
    }
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event){
        if (mc.isSingleplayer())return;
        if (keepAlivePackets.size() > 0 && transactionPackets.size() > 0) {
            if (timer.delay(3000)) {
                mc.getNetHandler().addToSendQueue(keepAlivePackets.get(keepAlivePackets.size() - 1));
                mc.getNetHandler().addToSendQueue(transactionPackets.get(transactionPackets.size() - 1));
                keepAlivePackets.clear();
                transactionPackets.clear();
                timer.reset();
            }
        }
    }
}
