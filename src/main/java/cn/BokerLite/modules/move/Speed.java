package cn.BokerLite.modules.move;

import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.MoveUtils;
import cn.BokerLite.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.utils.movement.MovementUtils;
import cn.BokerLite.utils.timer.TimerUtil;

import java.util.Objects;

import static cn.BokerLite.Client.getTimer;

public class Speed extends Module {
    public static double movementSpeed;
    private final Mode<Enum<SpeedMode>> mode = new Mode<>("Mode", "mode", SpeedMode.values(), SpeedMode.FastHop);
    public TimerUtil timer = new TimerUtil();
    private final Option<Boolean> Timerb = new Option<>("Timer","", "Timer",false);
    private final Numbers timerv = new Numbers("TimerSpeed", "Timer速度", "TimerSpeed", 1.2, 0.0, 3.0, 0.1);
    boolean antiCheatDetecting;
    private int i;
    private boolean wasTimerFastYport = false;
    private final boolean wasTimerVulcanHop = false;
    private double speed;
    private int stage;
    private double lastDist;
    private int ticks = 0;

    public Speed() {
        super("Speed", "速度", Keyboard.KEY_NONE, ModuleType.Movement, "Make you move quickly",ModuleType.SubCategory.MOVEMENT_MAIN);
    }

    public static double getNormalSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }

        return 0;
    }

    public static void setSpeed(double speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.movementInput.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.movementInput.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.movementInput.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw * ((float) Math.PI / 180);
    }

    @Override
    public void disable() {
        if(mode.getValue()==SpeedMode.Legit) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        }
        try {
            Objects.requireNonNull(getTimer()).timerSpeed = 1.0f;
        } catch (NullPointerException e) {
            // fuck
        }
    }

    @Override
    public void enable() {

        this.lastDist = 0.0;
        this.speed = MoveUtils.getSpeed();
        this.stage = 2;antiCheatDetecting = false;
    }

    public static net.minecraft.util.Timer getTimer() {
        return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
    }

    public static void resetTimer() {
        try {
            getTimer().timerSpeed = 1.0F;
        } catch (NullPointerException ignored) {
        }
    }

    @SubscribeEvent
    public void onTimer(TickEvent.PlayerTickEvent event) {
        if (!(mc.currentScreen instanceof ClickGui)&& Timerb.getValue()&& GameSettings.isKeyDown(mc.gameSettings.keyBindSprint)) {
            getTimer().timerSpeed = timerv.getValue().floatValue();
        } else {
            resetTimer();
        }

    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if(mode.getValue()==SpeedMode.Legit) {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0) {
                mc.thePlayer.setSprinting(true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
            } else if (mc.thePlayer.moveForward <= 0) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            }
        }

    }

        @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        this.setSuffix(mode.getValue().toString());

        if (mode.getValue() == SpeedMode.Boosting) {
            if (this.i == 0) {
                this.i = mc.thePlayer.ticksExisted;
            }
            try {
                Objects.requireNonNull(getTimer()).timerSpeed = (float) 2.0;
                if (this.i == mc.thePlayer.ticksExisted - 15.0) {
                    getTimer().timerSpeed = 1.0f;
                    this.disable();
                }
            } catch (NullPointerException e) {
                //
            }
        }
        if(mode.getValue() == SpeedMode.HypixelHop){
            try {
                final double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                final double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (this.stage > 0 && !Speed.mc.thePlayer.isInWater()) {
                    if (this.stage == 1 && Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                        ++this.stage;
                    }
                    if (this.stage == 2 && Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                        Speed.mc.thePlayer.jump();
                    }
                    else if (this.stage >= 3 && Speed.mc.thePlayer.isCollidedVertically) {
                        this.speed = this.getBaseSpeed();
                        this.lastDist = 0.0;
                        this.stage = 0;
                    }
                }
                else {
                    this.stage = 0;
                }
                if (this.stage < 1) {
                    ++this.stage;
                    this.lastDist = 0.0;
                }
                int amplifier = -1;
                if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    amplifier = Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                }
                if (this.stage == 2 && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) && Speed.mc.thePlayer.isCollidedVertically && Speed.mc.thePlayer.onGround) {
                    this.speed *= 1.785 + 0.04 * (amplifier + 1);
                }
                else if (this.stage == 3) {
                    final float diff = (float)(0.72 * (this.lastDist - this.getBaseSpeed() * (isOnIce() ? 1.1 : 1.0)));
                    this.speed = this.lastDist - diff;
                }
                else {
                    if ((Speed.mc.theWorld.getCollidingBoundingBoxes((Entity)Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                        this.stage = ((Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                    }
                    this.speed = MoveUtils.calculateFriction(this.speed, this.lastDist, this.getBaseSpeed());
                }
                this.speed = Math.max(this.speed, this.getBaseSpeed() * 0.85);
                ++this.stage;
                if (MoveUtils.isMoving()) {
                    MoveUtils.strafe(this.speed);
                }
                else {
                    MoveUtils.strafe(0.0);
                    this.stage = 0;
                }
                this.speed = this.getBaseSpeed();
            }
            catch (Exception ex) {}
        }
        if (mode.getValue() == SpeedMode.FastHop) {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0 && mc.thePlayer.onGround) {
                mc.thePlayer.setSprinting(true);
                mc.thePlayer.jump();
            }
        } else if (mode.getValue() == SpeedMode.SlowHop) {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0 && mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                setSpeed(0.26 + (getNormalSpeedEffect() * 2));
            } else {
                setSpeed(0);
            }
            movementSpeed = 0.26;
        } else if (mode.getValue() == SpeedMode.BunnyHop) {
            if (isToJump() && Minecraft.getMinecraft().thePlayer.moveForward != 0 && (Minecraft.getMinecraft().thePlayer.posY % 1 == 0))
                Minecraft.getMinecraft().thePlayer.jump();
        } else if (mode.getValue()==SpeedMode.FastYport) {
            ticks++;
            if (wasTimerFastYport) {
                getTimer().timerSpeed = 1.00f;
                wasTimerFastYport = false;
            }
            mc.thePlayer.jumpMovementFactor = 0.0245f;
            if (!mc.thePlayer.onGround && ticks > 3 && mc.thePlayer.motionY > 0) {
                mc.thePlayer.motionY = -0.27;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindJump));
            if (MovementUtils.getSpeed() < 0.215f && !mc.thePlayer.onGround) {
                MovementUtils.strafe(0.215f);
            }
            if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                ticks = 0;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(),false);
                mc.thePlayer.jump();
                if (!mc.thePlayer.isAirBorne) {
                    return ;//Prevent flag with Fly
                }
                getTimer().timerSpeed = 1.4f;
                wasTimerFastYport = true;
                if(MovementUtils.getSpeed() < 0.48f) {
                    MovementUtils.strafe(0.48f);
                }else{
                    MovementUtils.strafe((float) (MovementUtils.getSpeed()*0.985));
                }
            }else if (!MovementUtils.isMoving()) {
                getTimer().timerSpeed = 1.00f;
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }
        } else if (mode.getValue() == SpeedMode.Hypixel) {


            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindJump));

            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                if (TargetStrafe.canStrafe()) {
                    TargetStrafe.strafe(  Math.max(0.275, MoveUtils.defaultSpeed() * 0.9));
                } else {
                    MovementUtils.strafe((float) Math.max(0.275, MoveUtils.defaultSpeed() * 0.9));
                }

            }
            if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(),false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                if (!mc.thePlayer.isAirBorne) {
                    return ;//Prevent flag with Fly
                }

            }

        }}
    private double getBaseSpeed() {
        final EntityPlayerSP player = Speed.mc.thePlayer;
        double base = 0.2895;
        final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
        final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
        if (moveSpeed != null) {
            base *= 1.0 + 0.19 * (moveSpeed.getAmplifier() + 1);
        }
        if (moveSlowness != null) {
            base *= 1.0 - 0.13 * (moveSlowness.getAmplifier() + 1);
        }
        if (player.isInWater()) {
            base *= 0.5203619984250619;
            final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier((Entity)Speed.mc.thePlayer);
            if (depthStriderLevel > 0) {
                final double[] DEPTH_STRIDER_VALUES = { 1.0, 1.4304347400741908, 1.7347825295420374, 1.9217391028296074 };
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }
        }
        else if (player.isInLava()) {
            base *= 0.5203619984250619;
        }
        return base;
    }

    public static boolean isOnIce() {
        final Block blockUnder = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, StrictMath.floor(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY) - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }
    public boolean isToJump() {
        return mc.thePlayer != null && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder();
    }

    enum SpeedMode {
        FastHop,
        Legit,
        Boosting,
        BunnyHop,
        SlowHop,
        FastYport,HypixelHop,

      Hypixel
    }

}