package cn.BokerLite.modules.Player;


import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.reflect.ReflectionUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class SpeedMine
        extends Module {
    private final boolean bzs = false;
    private final float bzx = 0.0f;
    private BlockPos blockPos;
    public static Numbers speed = new Numbers("Speed","Speed","Speed", 1.4D, 1.0D, 2.0D, 0.1D);
    private EnumFacing facing;
    private boolean digging;
    private float damage;

    public SpeedMine() {
        super("SpeedMine", "快速挖掘", Keyboard.KEY_NONE, ModuleType.Player, "Make you dig faster",ModuleType.SubCategory.PLayer_Player);
        //   this.modeValue = new Mode<modeEnums>("Mode", "Mode", modeEnums.values(), modeEnums.Packet);
        //   this.Speed = new Numbers("SpeedMine", "SpeedMine","SpeedMine",1.4, 0.0, 3.0, 0.1);
    }

    @SubscribeEvent
    void onUpdate(final TickEvent.PlayerTickEvent event) {

        ReflectionUtil.setFieldValue(mc.playerController, 0, "blockHitDelay", "field_78781_i");
        if (this.digging && !mc.playerController.isInCreativeMode()) {
            final Block block = mc.theWorld.getBlockState(this.blockPos).getBlock();
            this.damage += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, this.blockPos) * speed.getValue().floatValue();
            if (this.damage >= 1.0f) {
                mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.damage = 0.0f;
                this.digging = false;
            }

        }
    }

    @EventHandler
    void onPacket(final EventPacketSend event) {
        final Packet<?> p = (Packet<?>) event.getPacket();
        if (p instanceof C07PacketPlayerDigging && !mc.playerController.isInCreativeMode()) {
            final C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging) p;
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.digging = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.damage = 0.0f;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.digging = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    }
}

