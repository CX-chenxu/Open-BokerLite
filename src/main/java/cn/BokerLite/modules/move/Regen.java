package cn.BokerLite.modules.move;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Option;

public class Regen extends Module {
    private final Option<Boolean> guardian = new Option<>("Guardian", "安全模式", "guardian", false);

    public Regen() {
        super("Regen", "快速回血", Keyboard.KEY_NONE, ModuleType.Combat, "Fast health regen",ModuleType.SubCategory.COMBAT_RAGE);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent e) {
        if (mc.thePlayer.onGround && (double) mc.thePlayer.getHealth() < Minecraft.getMinecraft().thePlayer.getMaxHealth()-((Minecraft.getMinecraft().thePlayer.getMaxHealth()/20)*4) && mc.thePlayer.getFoodStats().getFoodLevel() > 17 && mc.thePlayer.isCollidedVertically) {
            int i = 0;
            while (i < 60) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
                ++i;
            }
            if (this.guardian.getValue() && mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 999.0, mc.thePlayer.posZ, true));
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}