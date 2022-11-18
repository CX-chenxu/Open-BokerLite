package cn.BokerLite.modules.move;

import cn.BokerLite.api.EventHandler;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.event.PacketEvent;

import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.movement.MoveUtils;

public class NoFall extends Module {
    public NoFall() {
        super("Nofall","无掉落伤害", Keyboard.KEY_NONE, ModuleType.Movement, "No Falling Damage",ModuleType.SubCategory.PLayer_Player);
    }

    @EventHandler
    public void onPacket(PacketEvent event) {

        if (mc.thePlayer.posY > 0 && mc.thePlayer.fallDistance >= 2 && mc.thePlayer.lastTickPosY - mc.thePlayer.posY > 0 && mc.thePlayer.motionY != 0) {
            if (!MoveUtils.isBlockUnder() || mc.thePlayer.fallDistance > 255 || !MoveUtils.isBlockUnder() && mc.thePlayer.fallDistance > 50) {
                return;
            }

            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

                if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    event.setCancelled(true);
                }
            }

            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

                if (packet.isMoving() && packet.getRotating()) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ(), packet.isOnGround()));
                    event.setCancelled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onMotion(TickEvent.PlayerTickEvent event) {
            if (mc.thePlayer.posY > 0 && mc.thePlayer.lastTickPosY - mc.thePlayer.posY > 0 && mc.thePlayer.motionY != 0 && mc.thePlayer.fallDistance >= 2.5) {
                if (!MoveUtils.isBlockUnder() || mc.thePlayer.fallDistance > 255 || !MoveUtils.isBlockUnder() && mc.thePlayer.fallDistance > 50) {
                    return;
                }
                if (mc.thePlayer.fallDistance > 10 || mc.thePlayer.ticksExisted % 2 == 0) {
//                	Helper.sendMessage("1");
                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                }
            }
    }
}


