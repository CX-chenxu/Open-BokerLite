package cn.BokerLite.modules.Player;

import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.utils.render.RenderHelper;

import java.awt.*;

public class Fucker extends Module {
    private static final int radius = 5;
    public TimerUtil timer = new TimerUtil();
    private final Mode<Enum<Target>> targetmod = new Mode<>("Target", "Target", Target.values(), Target.Bed);
    private final Mode<Enum<FuckMode>> fuckmode = new Mode<>("Mode", "Mode", FuckMode.values(), FuckMode.Auto);
    
    public Fucker() {
        super("Fucker","自动挖床", Keyboard.KEY_NONE, ModuleType.Player, "Destroy bed egg and cake",ModuleType.SubCategory.PLayer_Player);
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        this.setSuffix(this.targetmod.getValue().name());
        int x = -radius;
        while (x < radius) {
            if (this.fuckmode.getValue() == FuckMode.OnClick && !mc.gameSettings.keyBindAttack.isKeyDown())
                return;
            int y = radius;
            while (y > -radius) {
                int z = -radius;
                while (z < radius) {
                    int xPos = (int) mc.thePlayer.posX + x;
                    int yPos = (int) mc.thePlayer.posY + y;
                    int zPos = (int) mc.thePlayer.posZ + z;
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block.getBlockState().getBlock() == Block.getBlockById(92) && this.targetmod.getValue() == Target.Cake) {
                        mc.thePlayer.swingItem();
                        if (timer.delay(300)) {
                            System.out.println("OK");
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            // mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1));

                            timer.reset();
                        }

                        RenderHelper.drawBlockESP(blockPos,new Color(0,255,0).getRGB());
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(122) && this.targetmod.getValue() == Target.Egg) {
                        mc.thePlayer.swingItem();
                        if (timer.delay(300)) {
                            System.out.println("OK");
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            // mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1));

                            timer.reset();
                        }

                        RenderHelper.drawBlockESP(blockPos,new Color(0,255,0).getRGB());
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(26) && this.targetmod.getValue() == Target.Bed) {
                        mc.thePlayer.swingItem();
                        if (timer.delay(300)) {
                            System.out.println("OK");
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                            // mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1));

                            timer.reset();
                        }


                        RenderHelper.drawBlockESP(blockPos,new Color(0,255,0).getRGB());
                    }
                    ++z;
                }
                --y;
            }
            ++x;
        }
    }
    
    enum Target {
        Cake,
        Egg,
        Bed
    }
    
    enum FuckMode {
        OnClick,
        Auto
    }
}


