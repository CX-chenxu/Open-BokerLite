package cn.BokerLite.modules.move;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.timer.TimerUtil;

public class Criticals extends Module {
    public static final Mode<Enum<CriticalsMode>> mode = new Mode<>("Mode", "mode", CriticalsMode.values(), CriticalsMode.Packet);
    public static Numbers delay = new Numbers("Delay", "间隔", "delay", 50.0, 0.0, 100.0, 10.0);
    private final float[] watchdogL = {0.0562f, 0.0162f, 0.00311f};
    private final TimerUtil timer = new TimerUtil();
    private int groundTicks;
    public Criticals() {
        super("Criticals","暴击", Keyboard.KEY_NONE, ModuleType.Combat, "Make you Criticals on Attack",ModuleType.SubCategory.COMBAT_RAGE);
    }
    
    public static boolean canJump() {
        if (mc.thePlayer.isOnLadder()) {
            return false;
        }
        if (mc.thePlayer.isInWater()) {
            return false;
        }
        if (mc.thePlayer.isInLava()) {
            return false;
        }
        if (mc.thePlayer.isSneaking()) {
            return false;
        }
        return !mc.thePlayer.isRiding();
    }

    public boolean canCrit() {
        return mc.thePlayer.onGround && !mc.thePlayer.isInWater();
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        try {
            groundTicks = mc.thePlayer.onGround ? groundTicks + 1 : 0;
        } catch (NullPointerException e) {
            //
        }
    }
    
    @EventHandler
    public void onPacket(EventPacketSend e) {

            if (e.getPacket() instanceof C02PacketUseEntity && this.canCrit() && mode.getValue() == CriticalsMode.Jump) {
                mc.thePlayer.jump();
            }
            if (e.getPacket() instanceof C02PacketUseEntity && this.canCrit() && mode.getValue() == CriticalsMode.MinJump) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.114514, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0114514, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.014514, mc.thePlayer.posZ, false));
            }
            if (e.getPacket() instanceof C0APacketAnimation && this.canCrit() && mode.getValue() == CriticalsMode.Packet) {
                // Created by Hidomocd#4399
                if (groundTicks > 1 && timer.hasReached(800L)) {
                    for (double offset : watchdogL) {
                        mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(
                                mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                    }
                    timer.reset();
                }

        }
    }
    
    @SubscribeEvent
    public void onTick(TickEvent event) {
        this.setSuffix(mode.getValue().name());
        if (mode.getValue() == CriticalsMode.Legit) {
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                if (mc.objectMouseOver.entityHit != null && canJump() && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.playerController.attackEntity(mc.thePlayer, Minecraft.getMinecraft().objectMouseOver.entityHit);
                }
            }
        }
    }
    
    
    enum CriticalsMode {
        Legit,
        Jump,
        Packet,
        MinJump
    }
}
