package cn.BokerLite.modules.Player;

import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.Player.disabler.Timer;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.reflect.ReflectionUtil;

import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Nuker
        extends Module {
    private boolean isRunning = true;
    private final Timers timer;
    public cn.BokerLite.utils.timer.TimerUtil timer1 = new TimerUtil();
    public Option<Boolean> silent= new Option<Boolean>("Silent", "Silent","Silent", true);
    private int posZ;
    public Numbers radius = new Numbers("Horizontal Radius","Horizontal Radius", "Horizontal Radius",  3d, 1d,50d, 1d);
    private int posY;
    public Numbers height = new Numbers("Height Radius","Height Radius","Height Radius", 1d, 1d, 50d,1d);
    private int posX;

    public EnumFacing getFacing(BlockPos blockPos) {
        EnumFacing[] enumFacingArray;
        EnumFacing[] enumFacingArray2 = enumFacingArray = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN};
        int n = enumFacingArray.length;
        for (int i = 0; i < n; ++i) {
            EnumFacing enumFacing = enumFacingArray2[i];
            EntitySnowball entitySnowball = new EntitySnowball(mc.theWorld);
            entitySnowball.posX = (double)blockPos.getX() + 0.5;
            entitySnowball.posY = (double)blockPos.getY() + 0.5;
            entitySnowball.posZ = (double)blockPos.getZ() + 0.5;
            entitySnowball.posX += (double)enumFacing.getDirectionVec().getX() * 0.5;
            entitySnowball.posY += (double)enumFacing.getDirectionVec().getY() * 0.5;
            entitySnowball.posZ += (double)enumFacing.getDirectionVec().getZ() * 0.5;
            if (!mc.thePlayer.canEntityBeSeen(entitySnowball)) continue;
            return enumFacing;
        }
        return null;
    }

    public Nuker() {
        super("Nuker", "Nuker", Keyboard.KEY_NONE, ModuleType.Player, "Reduces your knockback",ModuleType.SubCategory.PLayer_Player);

        this.timer = new Timers(this);
    }

    @SubscribeEvent
    public void onPost(TickEvent.PlayerTickEvent eventPostUpdate) {
        Block block = this.getBlock(this.posX, this.posY, this.posZ);
        if (this.isRunning) {
            mc.thePlayer.swingItem();
            mc.playerController.onPlayerDamageBlock(new BlockPos(this.posX, this.posY, this.posZ), this.getFacing(new BlockPos(this.posX, this.posY, this.posZ)));
            if (((double) ReflectionUtil.getFieldValue(mc.playerController, "curBlockDamageMP", "field_78770_f")) >= 1.0) {
                this.timer.reset();
            }
        }
    }




    @SubscribeEvent
    public void onPre(TickEvent.PlayerTickEvent eventPostUpdate) {
        int n;
        this.isRunning = false;
        int n2 = ((Number)this.radius.getValue()).intValue();
        for (int i = n = ((Number)this.height.getValue()).intValue(); i >= -n; --i) {
            for (int j = -n2; j < n2; ++j) {
                for (int k = -n2; k < n2; ++k) {
                    this.posX = (int)Math.floor(mc.thePlayer.posX) + j;
                    this.posY = (int)Math.floor(mc.thePlayer.posY) + i;
                    this.posZ = (int)Math.floor(mc.thePlayer.posZ) + k;
                    if (!(mc.thePlayer.getDistanceSq(mc.thePlayer.posX + (double)j, mc.thePlayer.posY + (double)i, mc.thePlayer.posZ + (double)k) <= 16.0)) continue;
                    Block block = this.getBlock(this.posX, this.posY, this.posZ);
                    boolean bl = this.timer.hasTimeElapsed(50L);
                    Block block2 = this.getBlock(mc.objectMouseOver.getBlockPos());
                    bl = bl && this.canSeeBlock((float)this.posX + 0.5f, (float)this.posY + 0.9f, (float)this.posZ + 0.5f) && !(block instanceof BlockAir);
                    boolean bl2 = bl = bl && (block.getBlockHardness(mc.theWorld, BlockPos.ORIGIN) != -1.0f || mc.playerController.isInCreativeMode());
                    if (!bl) continue;
                    this.isRunning = true;
                    if (mc.objectMouseOver != null) {
                        float f = 1.0f;
                        int n3 = -1;
                        for (int i2 = 0; i2 < 9; ++i2) {
                            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i2);
                            if (itemStack == null || !(itemStack.getStrVsBlock(block) > f)) continue;
                            f = itemStack.getStrVsBlock(block);
                            n3 = i2;
                        }
                        if (n3 != -1) {
                            mc.thePlayer.inventory.currentItem = n3;
                        }
                    }
                    float[] fArray = this.faceBlock((float)this.posX + 0.5f, (double)this.posY + 0.9, (float)this.posZ + 0.5f);


                    if (silent.getValue()){
                    //    System.out.println("ok");
                        if (timer1.delay(50)) {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(fArray[0], fArray[1], mc.thePlayer.onGround));
                            timer1.reset();
                        }

                    }else {
                        mc.thePlayer.rotationYaw = fArray[0];
                        mc.thePlayer.rotationPitch = fArray[1];
                    }

                    return;
                }
            }
        }
    }

    public boolean canSeeBlock(float f, float f2, float f3) {
        return this.getFacing(new BlockPos(f, f2, f3)) != null;
    }



    public Block getBlock(int n, int n2, int n3) {
        return mc.theWorld.getBlockState(new BlockPos(n, n2, n3)).getBlock();
    }



    public float[] faceBlock(double d, double d2, double d3) {
        double d4 = d - mc.thePlayer.posX;
        double d5 = d3 - mc.thePlayer.posZ;
        double d6 = d2 - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5);
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        float f3 = mc.thePlayer.rotationYaw;
        float f4 = mc.thePlayer.rotationPitch;
        float[] fArray = new float[2];
        f3 += MathHelper.wrapAngleTo180_float(f - f3);
        fArray[0] = f3;
        f4 += MathHelper.wrapAngleTo180_float(f2 - f4);
        fArray[1] = f4;
        return fArray;
    }

    public Block getBlock(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    public void disable() {
        super.disable();
        this.isRunning = false;
        this.posZ = 0;
        this.posY = 0;
        this.posX = 0;
    }
}
