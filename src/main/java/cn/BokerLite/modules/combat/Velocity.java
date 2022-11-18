package cn.BokerLite.modules.combat;

import cn.BokerLite.Client;
import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.Player.Scaffold;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.reflect.ReflectionUtil;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


public class Velocity extends Module {
    public static final Mode<Enum<AMode>> mode = new Mode<>("Mode", "mode", AMode.values(), AMode.Blatant);
    private final Numbers horizontal = new Numbers("Horizontal", "水平击退", "Horizontal", 1.0, 0.0, 100.0, 1.0);
    private final Numbers vertical = new Numbers("Vertical", "垂直击退", "Vertical", 1.0, 0.0, 100.0, 1.0);
    private final Numbers chance = new Numbers("Chance", "概率", "Chance", 100.0, 0.0, 100.0, 1.0);
    private final Option<Boolean> Targeting = new Option<>("On Targeting", "仅攻击时", "On Targeting", false);
    private final Option<Boolean> NoSword = new Option<>("No Sword", "仅不持剑时", "No Sword", false);
    public Velocity() {
        super("AntiKB", "击退降低", Keyboard.KEY_NONE, ModuleType.Combat, "Reduces your knockback",ModuleType.SubCategory.COMBAT_LEGIT);
    }
    @EventHandler
    public void onPacket(EventPacketRecieve e) {
        if (mode.getValue() == AMode.Blatant) {
            if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
                e.setCancelled(true);




            }
        }
    }
    @SubscribeEvent
    public void onEvent(TickEvent.PlayerTickEvent event){
        if (mode.getValue() == AMode.Legit) {
            if (Client.nullCheck())
                return;
            if (mc.thePlayer.maxHurtResistantTime != mc.thePlayer.hurtResistantTime || mc.thePlayer.maxHurtResistantTime == 0) {
                return;
            }

            Double random = Math.random();
            random *= 100.0;

            if (random < this.chance.getValue().intValue()) {
                float hori = this.horizontal.getValue().floatValue();
                hori /= 100.0f;
                float verti = this.vertical.getValue().floatValue();
                verti /= 100.0f;
                mc.thePlayer.motionX *= hori;
                mc.thePlayer.motionZ *= hori;
                mc.thePlayer.motionY *= verti;
            } else {
                mc.thePlayer.motionX *= 1.0f;
                mc.thePlayer.motionY *= 1.0f;
                mc.thePlayer.motionZ *= 1.0f;
            }
        }
    }
    public enum AMode {
        Legit,
        Blatant
    }
}
