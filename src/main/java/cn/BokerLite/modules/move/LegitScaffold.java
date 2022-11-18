package cn.BokerLite.modules.move;

import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.block.BlockInfo;
import cn.BokerLite.utils.block.BlockUtil;
import cn.BokerLite.utils.scaffold.Data;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LegitScaffold extends Module {
    public static final Mode<Enum<ScaffoldMode>> mode = new Mode<>("Mode", "mode", ScaffoldMode.values(), ScaffoldMode.Blatant);
    public static final Option<Boolean> rotation = new Option<>("Rotation", "转头", "Rotation", true);
    public static float pitch;
    public static int fucku = 0;
    private final Option<Boolean> sneak = new Option<>("Sneak", "潜行", "Sneak", false);
    private final Option<Boolean> swing = new Option<>("Swing", "挥手", "Swing", true);
    private final Option<Boolean> tower = new Option<>("Tower", "快速叠高", "Tower", false);
    private final Option<Boolean> silent = new Option<>("Silent", "剑搭", "Silent", true);
    private final Option<Boolean> sprint = new Option<>("Sprint", "疾跑", "Sprint", false);
    public int godBridgeTimer;
    public int currentItem;
    public int width = 0;

    public LegitScaffold() {
        super("LegitScaffold", "安全搭路", Keyboard.KEY_NONE, ModuleType.Movement, "Make you bridge faster",ModuleType.SubCategory.MOVEMENT_MAIN);
        this.currentItem = 0;
    }

    public Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }
    
    public Block getBlockUnderPlayer(EntityPlayer player) {
        return getBlock(new BlockPos(player.posX, player.posY - 1.0d, player.posZ));
    }
    
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.setSuffix(mode.getValue().toString());
        if (mode.getValue() == ScaffoldMode.Legit) {
            if (getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                if (mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                if (mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                }
            }
        } else if (mode.getValue() == ScaffoldMode.Assist) {
            if (this.sneak.getValue()) {
                if (getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                    if (mc.thePlayer.onGround) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    }
                } else {
                    if (mc.thePlayer.onGround) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    }
                }
            }
            doBridge();
        }
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
    	if(sneak.getValue())
    		mc.thePlayer.setSneaking(true);
        if (mode.getValue() != ScaffoldMode.Blatant)
            return;
        if(!sprint.getValue()){
            mc.thePlayer.setSprinting(false);
        }
        BlockPos blockpos;
 		if (mc.thePlayer.ridingEntity!=null) {
 			blockpos = new BlockPos(mc.thePlayer.ridingEntity.posX,mc.thePlayer.ridingEntity.posY-0.5d,mc.thePlayer.ridingEntity.posZ);
 		} else {
 			blockpos = new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY-0.5d,mc.thePlayer.posZ);
 		}
		Block block = mc.theWorld.getBlockState(blockpos).getBlock();
		ItemStack item=mc.thePlayer.getHeldItem();
		int slot = mc.thePlayer.inventory.currentItem;
		if (block.canReplace(mc.theWorld, blockpos, EnumFacing.fromAngle(mc.thePlayer.rotationPitch), item)) {
			int blockSlot=slot;
			if (item==null || !(item.getItem() instanceof ItemBlock) ) {
	            blockSlot = BlockUtil.findAutoBlockBlock();

	            if (blockSlot == -1)
	                return;

	            item = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
	            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
	            //mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw,80,mc.thePlayer.onGround));
			}

            BlockInfo bI = BlockInfo.get(blockpos);
            if (bI == null)
                return;
            if (mc.gameSettings.keyBindJump.isKeyDown() && tower.getValue()) {
                mc.thePlayer.motionY = 0;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ);
            }

            if (rotation.getValue()) {
            	float yaw = bI.yaw, pitch = bI.pitch;
            	if(Float.isNaN(yaw))
            		yaw = mc.thePlayer.rotationYaw;
            	if(Float.isNaN(pitch))
            		yaw = mc.thePlayer.rotationPitch;
//            	mc.thePlayer.rotationYaw = yaw; mc.thePlayer.rotationPitch = pitch;
            	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
            }
            int currentSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = slot;
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, item, bI.blockPos, bI.enumFacing, bI.vec3);
            if (this.silent.getValue()) {
                mc.thePlayer.inventory.currentItem = currentSlot;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
            }
            if (blockSlot != slot) mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(slot));
            if (swing.getValue()) {
                mc.thePlayer.swingItem();
            }
        }
    }

    public Vec3 scale(Vec3 v, double p_186678_1_) {
        return new Vec3(v.xCoord * p_186678_1_, v.yCoord * p_186678_1_, v.zCoord * p_186678_1_);
    }

    public void enable() {
        this.currentItem = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.setSneaking(false);
        super.enable();
    }
    
    public void disable() {
    	if(sneak.getValue())
    		mc.thePlayer.setSneaking(false);
        try {
            Objects.requireNonNull(Client.getTimer()).timerSpeed = 1.0F;
        } catch (NullPointerException e) {
            //
        }
        if (mode.getValue() != ScaffoldMode.Blatant)
            return;
        mc.thePlayer.inventory.currentItem = this.currentItem;
        fucku = 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        super.disable();
    }

    public void doBridge() {
        if (godBridgeTimer > 0) {
            ReflectionHelper.setPrivateValue(Minecraft.class, mc, 0, "rightClickDelayTimer", "field_71467_ac");
            godBridgeTimer--;
        }

        if (mc.theWorld == null || mc.thePlayer == null) return;
        WorldClient world = mc.theWorld;
        EntityPlayerSP player = mc.thePlayer;
        MovingObjectPosition movingObjectPosition = player.rayTrace(mc.playerController.getBlockReachDistance(), 1);
        boolean isKeyUseDown;
        int keyCode = mc.gameSettings.keyBindUseItem.getKeyCode();
        if (keyCode >= 0) {
            isKeyUseDown = Keyboard.isKeyDown(keyCode);
        } else {
            isKeyUseDown = Mouse.isButtonDown(keyCode + 100);
        }
        if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition.sideHit == EnumFacing.UP && isKeyUseDown) {

            ItemStack itemstack = player.inventory.getCurrentItem();
            int i = itemstack != null ? itemstack.stackSize : 0;

            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                ItemBlock itemblock = (ItemBlock) itemstack.getItem();

                if (!itemblock.canPlaceBlockOnSide(world, movingObjectPosition.getBlockPos(), movingObjectPosition.sideHit, player, itemstack)) {
                    BlockPos blockPos = movingObjectPosition.getBlockPos();
                    IBlockState blockState = world.getBlockState(blockPos);
                    AxisAlignedBB axisalignedbb = blockState.getBlock().getSelectedBoundingBox(world, blockPos);

                    if (axisalignedbb == null || world.isAirBlock(blockPos)) return;

                    double x1, x2, y1, y2, z1, z2;
                    Vec3 targetVec3 = null;
                    Vec3 eyeVec3 = player.getPositionEyes((float) 1);
                    x1 = axisalignedbb.minX;
                    x2 = axisalignedbb.maxX;
                    y1 = axisalignedbb.minY;
                    y2 = axisalignedbb.maxY;
                    z1 = axisalignedbb.minZ;
                    z2 = axisalignedbb.maxZ;
                    List<Data> list = new ArrayList<>();
                    if (!(x1 <= eyeVec3.xCoord && eyeVec3.xCoord <= x2 && y1 <= eyeVec3.yCoord && eyeVec3.yCoord <= y2 && z1 <= eyeVec3.zCoord && eyeVec3.zCoord <= z2)) {

                        double xCost = Math.abs(eyeVec3.xCoord - 0.5 * (axisalignedbb.minX + axisalignedbb.maxX));
                        double zCost = Math.abs(eyeVec3.zCoord - 0.5 * (axisalignedbb.minZ + axisalignedbb.maxZ));
                        if (eyeVec3.xCoord < x1) {
                            list.add(new Data(blockPos.west(), EnumFacing.WEST, xCost));
                        } else if (eyeVec3.xCoord > x2) {
                            list.add(new Data(blockPos.east(), EnumFacing.EAST, xCost));
                        }
                        if (eyeVec3.zCoord < z1) {
                            list.add(new Data(blockPos.north(), EnumFacing.NORTH, zCost));
                        } else if (eyeVec3.zCoord > z2) {
                            list.add(new Data(blockPos.south(), EnumFacing.SOUTH, zCost));
                        }
                        Collections.sort(list);
                        double border = 0.05;
                        double x = MathHelper.clamp_double(eyeVec3.xCoord, x1 + border, x2 - border);
                        double y = MathHelper.clamp_double(eyeVec3.yCoord, y1 + border, y2 - border);
                        double z = MathHelper.clamp_double(eyeVec3.zCoord, z1 + border, z2 - border);
                        for (Data data : list) {
                            if (!world.isAirBlock(data.blockPos)) continue;
                            if (data.enumFacing == EnumFacing.WEST || data.enumFacing == EnumFacing.EAST) {
                                x = MathHelper.clamp_double(eyeVec3.xCoord, x1, x2);
                            } else if (data.enumFacing == EnumFacing.UP || data.enumFacing == EnumFacing.DOWN) {
                                y = MathHelper.clamp_double(eyeVec3.yCoord, y1, y2);
                            } else {
                                z = MathHelper.clamp_double(eyeVec3.zCoord, z1, z2);
                            }
                            targetVec3 = new Vec3(x, y, z);
                            break;
                        }

                        if (targetVec3 != null) {
                            double d0 = targetVec3.xCoord - eyeVec3.xCoord;
                            double d1 = targetVec3.yCoord - eyeVec3.yCoord;
                            double d2 = targetVec3.zCoord - eyeVec3.zCoord;
                            double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
                            float f = (float) (MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
                            float f1 = (float) (-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
                            float f2, f3;
                            f2 = player.rotationYaw;
                            f3 = player.rotationPitch;
                            player.rotationYaw = f;
                            player.rotationPitch = f1;
                            MovingObjectPosition movingObjectPosition1 = player.rayTrace(mc.playerController.getBlockReachDistance(), 1);
                            if (movingObjectPosition1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition1.getBlockPos().getX() == blockPos.getX() && movingObjectPosition1.getBlockPos().getY() == blockPos.getY() && movingObjectPosition1.getBlockPos().getZ() == blockPos.getZ()) {
                                if (mc.playerController.onPlayerRightClick(player, mc.theWorld, itemstack, blockPos, movingObjectPosition1.sideHit, movingObjectPosition1.hitVec)) {
                                    player.swingItem();
                                }
                                if (itemstack.stackSize == 0) {
                                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                                } else if (itemstack.stackSize != i || mc.playerController.isInCreativeMode()) {
                                    mc.entityRenderer.itemRenderer.resetEquippedProgress();
                                }
                            }
                            player.rotationYaw = f2;
                            player.rotationPitch = f3;
                            double pitchDelta = 2.5;
                            double targetPitch = 75.5;
                            if (targetPitch - pitchDelta < player.rotationPitch && player.rotationPitch < targetPitch + pitchDelta) {
                                double mod = player.rotationYaw % 45.0;
                                if (mod < 0) {
                                    mod += 45.0;
                                }
                                double delta = 5.0;

                                if (mod < delta) {
                                    player.rotationYaw -= mod;
                                    player.rotationPitch = (float) targetPitch;
                                } else if (45.0 - mod < delta) {
                                    player.rotationYaw += 45.0 - mod;
                                    player.rotationPitch = (float) targetPitch;
                                }
                            }

                            ReflectionHelper.setPrivateValue(Minecraft.class, mc, 1, "rightClickDelayTimer", "field_71467_ac");
                            godBridgeTimer = 10;

                        }
                    }
                }
            }
        }
    }

    public enum ScaffoldMode {


        Legit,
        Assist,
        Blatant
    }
}