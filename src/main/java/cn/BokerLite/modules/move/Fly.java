package cn.BokerLite.modules.move;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.block.BlockUtils;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.movement.MoveUtils;
import cn.BokerLite.utils.movement.MovementUtils;

import static cn.BokerLite.Client.getTimer;

public class Fly extends Module {
    private BlockPos currentBlock;
    private BlockPos lastBlock;
    private final Option<Boolean> keepalive = new Option<>("keepalive", "保持存活", "keepalive", false);
    private final Mode<Enum<flymode>> mode = new Mode<>("Mode", "Mode", flymode.values(), flymode.Creative);
    public Numbers speed = new Numbers("Fly Speed", "飞行速度", "Fly Speed", 1.0, 1.0, 10.0, 0.1);
    public Fly() {
        super("Fly","飞行", Keyboard.KEY_NONE, ModuleType.Movement, "Make u can fly",ModuleType.SubCategory.MOVEMENT_MAIN);
    }
    @Override
    public void disable() {
        if (mode.getValue()==flymode.Vulcan){
            getTimer().timerSpeed = 1.0f;
            if (!isSuccess) {
                mc.thePlayer.setPosition(startX, startY, startZ);
                Helper.sendMessage("§cFly attempt Failed...");
                Helper.sendMessage("§cIf it keeps happen, DONT use it again in CURRENT gameplay");
            }
        }
        if (Fly.mc.thePlayer.capabilities.isFlying&&mode.getValue()==flymode.Creative) {
            Fly.mc.thePlayer.capabilities.isFlying = false;
        }
        if (this.lastBlock != null) {
            mc.theWorld.setBlockState(this.lastBlock, Blocks.air.getDefaultState());
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e){
        this.setSuffix(mode.getValue().toString());

        if (mode.getValue()==flymode.Creative){
            Fly.mc.thePlayer.capabilities.isFlying = true;
            if (Fly.mc.gameSettings.keyBindJump.isPressed()) {
                final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                thePlayer.motionY += 0.2;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isPressed()) {
                final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                thePlayer2.motionY -= 0.2;
            }
            if (Fly.mc.gameSettings.keyBindForward.isPressed()) {
                Fly.mc.thePlayer.capabilities.setFlySpeed(speed.getValue().floatValue() / 10);
            }
        }else if (mode.getValue()==flymode.Vanilla){
           if (keepalive.getValue()){
                mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive());
            }
            Fly.mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.motionZ = 0.0;
            if (Fly.mc.gameSettings.keyBindJump.isPressed()) {
                Minecraft.getMinecraft().thePlayer.motionY += 2f;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isPressed()) {
                Minecraft.getMinecraft().thePlayer.motionY -= 2f;
            }
            MoveUtils.strafe((float) speed.getValue().doubleValue());
        }else if(mode.getValue() == flymode.AirWalk){
            if (mc.theWorld != null) {
                if (mc.thePlayer.prevPosY - mc.thePlayer.posY > 0.4) {
                    return;
                }
                if (mc.thePlayer.capabilities.isFlying) {
                    return;
                }
                final double blockX = mc.thePlayer.posX;
                final double blockY = mc.thePlayer.posY - 1.0;
                final double blockZ = mc.thePlayer.posZ;
                final BlockPos thisBlock = new BlockPos(MathHelper.floor_double(blockX), MathHelper.floor_double(blockY), MathHelper.floor_double(blockZ));
                if (this.currentBlock == null || !this.isSameBlock(thisBlock, this.currentBlock)) {
                    this.currentBlock = thisBlock;
                }
                if (mc.theWorld.isAirBlock(this.currentBlock)) {
                    mc.theWorld.setBlockState(thisBlock, Blocks.barrier.getDefaultState());
                    if (this.lastBlock != null) {
                        mc.theWorld.setBlockState(this.lastBlock, Blocks.air.getDefaultState());
                    }
                    this.lastBlock = thisBlock;
                }
            }
        } else if (mode.getValue()==flymode.Flag) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY + (mc.gameSettings.keyBindJump.isKeyDown()? 1.5624 : 0.00000001) - (mc.gameSettings.keyBindSneak.isKeyDown()? 0.0624 : 0.00000002), mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY - 6969, mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            mc.thePlayer.setPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 11, mc.thePlayer.posY, mc.thePlayer.posZ + mc.thePlayer.motionZ * 11);
            mc.thePlayer.motionY = 0.0;
        } else if (mode.getValue() == flymode.Vulcan) {
            if (stage==FlyStage.FLYING){
                isSuccess = false;

                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionY = 0.0;
                mc.thePlayer.motionZ = 0.0;

                MovementUtils.strafe((float) speed.getValue().doubleValue());
                doCancel = true;

                if(mc.gameSettings.keyBindSneak.isPressed()) {
                    MovementUtils.strafe(0.45f);
                    //More easy to land on ground ....
                }
                if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                    double fixedY = mc.thePlayer.posY - (mc.thePlayer.posY % 1);
                    Block underBlock2 = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, fixedY - 1, mc.thePlayer.posZ));


                    if(underBlock2.isFullBlock()) {
                        stage = FlyStage.WAIT_APPLY;
                        mc.thePlayer.motionX = 0.0;
                        mc.thePlayer.motionY = 0.0;
                        mc.thePlayer.motionZ = 0.0;
                        mc.thePlayer.jumpMovementFactor = 0.00f;
                        doCancel = false;
                        mc.thePlayer.onGround = false;
                        double fixedX = mc.thePlayer.posX - (mc.thePlayer.posX % 1);
                        double fixedZ = mc.thePlayer.posZ - (mc.thePlayer.posZ % 1);
                        if(fixedX>0) {
                            fixedX += 0.5;
                        }else{
                            fixedX -= 0.5;
                        }
                        if(fixedZ>0) {
                            fixedZ += 0.5;
                        }else{
                            fixedZ -= 0.5;
                        }
                        mc.thePlayer.setPosition(fixedX, fixedY, fixedZ);
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, fixedY , mc.thePlayer.posZ, true));
                        doCancel = true;
                        Helper.sendMessage("§aWaiting for landing...");
                    } else {
                      //  Helper.sendMessage("§cYou can only land on a solid block!");
                    }
                }
            } else if (stage==FlyStage.WAIT_APPLY) {
                vticks++;
                doCancel = false;
                if(vticks == 60) {
                    Helper.sendMessage("§cSeems took a long time! Please turn off the Fly manually");
                }
                getTimer().timerSpeed = 1f;
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionY = 0.0;
                mc.thePlayer.motionZ = 0.0;
                mc.thePlayer.jumpMovementFactor = 0.00f;
                double fixedY = mc.thePlayer.posY - (mc.thePlayer.posY % 1);
                if(mc.theWorld.getCollisionBoxes(mc.thePlayer.getEntityBoundingBox().offset(0.0, -10.0, 0.0)).isEmpty() && mc.theWorld.getCollisionBoxes(mc.thePlayer.getEntityBoundingBox().offset(0.0, -12.0, 0.0)).isEmpty()) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, fixedY - 10, mc.thePlayer.posZ, true));
                }else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, fixedY - 1024, mc.thePlayer.posZ, true));
                }
                doCancel = true;
            }
        }
    }

    public void swing() {
        final EntityPlayerSP p = mc.thePlayer;
        final int armSwingEnd = p.isPotionActive(Potion.digSpeed) ? (6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (p.isPotionActive(Potion.digSlowdown) ? (6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!p.isSwingInProgress || p.swingProgressInt >= armSwingEnd / 2 || p.swingProgressInt < 0) {
            p.swingProgressInt = -1;
            p.isSwingInProgress = true;
        }
    }

    @SubscribeEvent
    public void m(final MouseEvent e) {
        if(mode.getValue() == flymode.AirWalk) {
            if (e.buttonstate && (e.button == 0 || e.button == 1)) {
                final MovingObjectPosition mop = mc.objectMouseOver;
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    final int x = mop.getBlockPos().getX();
                    final int z = mop.getBlockPos().getZ();
                    final BlockPos pos = this.currentBlock;
                    if (pos.getX() == x && pos.getZ() == z) {
                        e.setCanceled(true);
                        if (e.button == 0) {
                            this.swing();
                        }
                        Mouse.poll();
                    }
                }
            }
        }
    }

    private boolean isSameBlock(final BlockPos pos1, final BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }







   //timervalue is speed.getvalue

    private boolean isSuccess = false;
    private int vticks = 0;
    private boolean doCancel = false;
    private FlyStage stage = FlyStage.FLYING;
    private double startX = 0.0;
    private double startZ = 0.0;
    private double startY = 0.0;


    @Override
    public void enable() {
        if (mode.getValue() == flymode.Vulcan) {
            vticks = 0;
            doCancel = false;
            if (mc.thePlayer.posY % 1 != 0.0) {
                this.setState(false);
                Helper.sendMessage("§cPlease stand on a solid block to fly!");
                isSuccess = true;
                return;
            }
            stage = FlyStage.FLYING;
            isSuccess = false;
            Helper.sendMessage("§aPlease press Sneak before you land on ground!");
           Helper.sendMessage("§7Tips: DO NOT Use killaura when you're flying!");
            startX = mc.thePlayer.posX;
            startY = mc.thePlayer.posY;
            startZ = mc.thePlayer.posZ;
        }
    }






    enum flymode{
        Creative,
        Vanilla,
        AirWalk,
        Flag,
        Vulcan
    }
    enum FlyStage {
        FLYING,
        WAIT_APPLY
    }
}
