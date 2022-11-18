package cn.BokerLite.modules.combat;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.reflect.ReflectionUtil;
import cn.BokerLite.utils.timer.CPSDelay;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends Module {
    private final Option<Boolean> left = new Option<>("Left Clicker", "左键连点", "Left Clicker", true);
    private final Option<Boolean> right = new Option<>("Right Clicker", "右键连点", "Right Clicker", false);
    private final Numbers maxCps = new Numbers("Left MaxCPS", "左键最大CPS", "LMaxCPS", 14.0, 1.0, 20.0, 0.1);
    private final Numbers minCps = new Numbers("Left MinCPS", "左键最小CPS", "LMinCPS", 10.0, 1.0, 20.0, 0.1);
    private final Numbers RmaxCps = new Numbers("Right MaxCPS", "右键最大CPS", "RMaxCPS", 14.0, 1.0, 20.0, 0.1);
    private final Numbers RminCps = new Numbers("Right MinCPS", "右键最大CPS", "RMixCPS", 10.0, 1.0, 20.0, 0.1);
    private final Numbers jitter = new Numbers("Jitter", "抖动", "Jitter", 0.0, 0.0, 3.0, 0.1);
    private final Option<Boolean> blockHit = new Option<>("BlockHit", "格挡攻击", "BlockHit", false);
    private final Option<Boolean> autoUnBlock = new Option<>("AutoUnblock", "自动取消格挡", "AutoUnblock", false);
    private final Option<Boolean> weaponOnly = new Option<>("Weapon Only", "仅武器", "Weapon Only", false);

    private final CPSDelay cpsDelay = new CPSDelay();
    private final Random random = new Random();

    public AutoClicker() {
        super("AutoClicker", "自动点击器", Keyboard.KEY_NONE, ModuleType.Combat, "Auto Click When Hold Mouse",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        this.setSuffix("CPS"+minCps.getValue() + "-"+maxCps.getValue());
        if (!state)
            return;
        if (mc.currentScreen == null && Mouse.isButtonDown(0)) {
            if (!left.getValue())
                return;
            if (this.weaponOnly.getValue()) {
                if (mc.thePlayer.getCurrentEquippedItem() == null) {
                    return;
                }
                if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                    return;
                }
            }
            if (!blockHit.getValue() && mc.thePlayer.isUsingItem()) return;

            if (cpsDelay.shouldAttack(minCps.getValue().intValue() == maxCps.getValue().intValue() ? maxCps.getValue().intValue() : ThreadLocalRandom.current().nextInt(minCps.getValue().intValue(), maxCps.getValue().intValue()))) {
                ReflectionUtil.setFieldValue(Minecraft.getMinecraft(), 0, "leftClickCounter", "field_71429_W");
                clickMouse();

                if (autoUnBlock.getValue()) {
                    if (Mouse.isButtonDown(1)) {
                        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            if (mc.thePlayer.isBlocking()) {
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                                mc.playerController.onStoppedUsingItem(mc.thePlayer);
                                mc.thePlayer.setItemInUse(mc.thePlayer.getItemInUse(),0);
                            } else {
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                            }
                        }
                    }
                }
            }
            if (jitter.getValue() > 0.0) {
                final double a = jitter.getValue() * 0.45;
                if (this.random.nextBoolean()) {
                    final EntityPlayerSP thePlayer = AutoClicker.mc.thePlayer;
                    thePlayer.rotationYaw += (float)(this.random.nextFloat() * a);
                }
                else {
                    final EntityPlayerSP thePlayer2 = AutoClicker.mc.thePlayer;
                    thePlayer2.rotationYaw -= (float)(this.random.nextFloat() * a);
                }
                if (this.random.nextBoolean()) {
                    final EntityPlayerSP thePlayer3 = AutoClicker.mc.thePlayer;
                    thePlayer3.rotationPitch += (float)(this.random.nextFloat() * (a * 0.45));
                }
                else {
                    final EntityPlayerSP thePlayer4 = AutoClicker.mc.thePlayer;
                    thePlayer4.rotationPitch -= (float)(this.random.nextFloat() * (a * 0.45));
                }
            }
        }

        if (mc.currentScreen == null && Mouse.isButtonDown(1)) {
            if(!right.getValue())
                return;
            if (cpsDelay.shouldAttack(RminCps.getValue().intValue() == RmaxCps.getValue().intValue() ? RmaxCps.getValue().intValue() : ThreadLocalRandom.current().nextInt(RminCps.getValue().intValue(), RmaxCps.getValue().intValue() + 1))) {
                try {
                    final Field rightClickDelay = Minecraft.class.getDeclaredField("field_71467_ac");
                    rightClickDelay.setAccessible(true);
                    rightClickDelay.set(mc, 0);
                } catch (Exception d) {
                    try {
                        final Field ex = Minecraft.class.getDeclaredField("rightClickDelayTimer");
                        ex.setAccessible(true);
                        ex.set(mc, 0);
                    } catch (Exception f) {
                        this.disable();
                    }
                }
            }
        }
    }


    public static void clickMouse()
    {
        int leftClickCounter = (int) ReflectionUtil.getFieldValue(Minecraft.getMinecraft(),"leftClickCounter","field_71429_W");
        if (leftClickCounter <= 0)
        {
            Minecraft.getMinecraft().thePlayer.swingItem();
            if (Minecraft.getMinecraft().objectMouseOver == null)
            {
                if (Minecraft.getMinecraft().playerController.isNotCreative())
                {
                    ReflectionUtil.setFieldValue(Minecraft.getMinecraft(),10,"leftClickCounter","field_71429_W");
                }
            }
            else
            {
                switch (Minecraft.getMinecraft().objectMouseOver.typeOfHit)
                {
                    case ENTITY:
                        try {
                            Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().objectMouseOver.entityHit);
                        } catch (NullPointerException exception) {
                            exception.printStackTrace();
                        }
                        break;

                    case BLOCK:
                        BlockPos blockpos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();

                        if (Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            Minecraft.getMinecraft().playerController.clickBlock(blockpos, Minecraft.getMinecraft().objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (Minecraft.getMinecraft().playerController.isNotCreative())
                        {
                            ReflectionUtil.setFieldValue(Minecraft.getMinecraft(),10,"leftClickCounter","field_71429_W");
                        }
                }
            }
        }
    }
}
