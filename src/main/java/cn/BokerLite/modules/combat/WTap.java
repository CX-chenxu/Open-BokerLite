package cn.BokerLite.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.timer.WTapTimer;

import java.util.concurrent.ThreadLocalRandom;

public class WTap extends Module {
    public static boolean comboing;
    public static boolean hitCoolDown;
    public static boolean Hited;
    public static boolean waitingForPostDelay;
    public static int hitTimeout;
    private static int hitsWaited;
    public static WTapTimer actionTimer = new WTapTimer(0), postDelayTimer = new WTapTimer(0);

    private final Option<Boolean> playerOnly = new Option<>("PlayerOnly", "仅玩家", "PlayerOnly", false);
    private final Numbers ticks = new Numbers("Ticks", "刻", "Tick", 22.0, 1.0, 55.0, 1.0);
    private final Numbers hits = new Numbers("Hits", "攻击次数", "Hits", 1.0, 1.0, 10.0, 1.0);
    private final Numbers delay = new Numbers("Delay", "间隔", "Delay", 22.0, 1.0, 55.0, 1.0);
    private final Numbers range = new Numbers("Reach", "距离", "Reach", 4.5, 3.0, 6.0, 1.0);
    private final Numbers chance = new Numbers("Chance", "概率", "Chance", 100.0, 1.0, 100.0, 1.0);

    public WTap() {
        super("WTap", "自动WTap", Keyboard.KEY_NONE, ModuleType.Combat, "Auto WTap",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        try {
            if (Client.nullCheck() && !this.state)
                return;

            if (waitingForPostDelay) {
                if (postDelayTimer.hasTimeElapsed()) {
                    waitingForPostDelay = false;
                    comboing = true;
                    startCombo();
                    actionTimer.start();
                }
                return;
            }

            if (comboing) {
                if (actionTimer.hasTimeElapsed()) {
                    comboing = false;
                    finishCombo();
                    return;
                } else {
                    return;
                }
            }


            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && Mouse.isButtonDown(0)) {
                Entity target = mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    return;
                }

                if (mc.thePlayer.getDistanceToEntity(target) <= range.getValue()) {
                    if ((target.hurtResistantTime >= 10)) {
                        if (playerOnly.getValue()) {
                            if (!(target instanceof EntityPlayer)) {
                                return;
                            }
                        }
                        if (AntiBot.isServerBot(target)) {
                            return;
                        }
                        if (hitCoolDown && !Hited) {
                            hitsWaited++;
                            if (hitsWaited >= hitTimeout) {
                                hitCoolDown = false;
                                hitsWaited = 0;
                            } else {
                                Hited = true;
                                return;
                            }
                        }

                        if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
                            return;

                        if (!Hited) {
                            hitTimeout = hits.getMinimum().intValue();
                            hitCoolDown = true;
                            hitsWaited = 0;

                            actionTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(ticks.getValue(), ticks.getValue() + 1));

                            if (delay.getMaximum() != 0) {
                                postDelayTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(delay.getValue(), delay.getValue() + 1));
                                postDelayTimer.start();
                                waitingForPostDelay = true;
                            } else {
                                comboing = true;
                                startCombo();
                                actionTimer.start();
                            }
                            Hited = true;
                        }
                    } else {
                        Hited = false;
                    }
                }
            }
        } catch (NullPointerException ex) {
            //
        }
    }

    private static void finishCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }

    private static void startCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
        }
    }
}