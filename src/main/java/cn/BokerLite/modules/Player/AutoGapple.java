package cn.BokerLite.modules.Player;

import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.misc.InventoryUtils;
import cn.BokerLite.utils.packet.PacketUtil;
import cn.BokerLite.utils.timer.MSTimer;

public class AutoGapple extends Module {
    public static Numbers health = new Numbers("Health", "生命", "Health", 10.0, 1.0, 20.0, 1.0);
    public static Numbers delay = new Numbers("Delay", "间隔", "Delay", 150.0, 0.0, 1000.0, 10.0);
    private final Mode<Enum<agmode>> mode = new Mode<>("Mode", "Mode", agmode.values(), agmode.Auto);
    public Option<Boolean> Noab = new Option<>("NoAbsorption", "无伤害吸收", "NoAbsorption", true);

    public AutoGapple() {
        super("AutoGapple", "自动吃苹果", Keyboard.KEY_NONE, ModuleType.Player, "AutoGapple",ModuleType.SubCategory.PLayer_Player);
    }

    private final MSTimer timer = new MSTimer();

    private int eating = -1;

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (mc.thePlayer.getHealth() > health.getValue().floatValue() || !timer.hasTimePassed((long) delay.getValue().doubleValue()) || Noab.getValue() && mc.thePlayer.getAbsorptionAmount() > 0) {
            return;
        }

        if (mode.getValue()==agmode.Auto){
           int gappleInHotbar = InventoryUtils.findItem(36, 45, Items.golden_apple);
            if (gappleInHotbar != -1) {
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(gappleInHotbar - 36));
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                int i=0;
                while (i<35){
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(mc.thePlayer.onGround));
                    i++;
                }
                    
                }
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
           // Helper.sendMessage("Gapple eaten");
                timer.reset();
            } else if (mode.getValue()==agmode.Legitauto) {
            if (eating == -1) {
                int gappleInHotbar = InventoryUtils.findItem(36, 45, Items.golden_apple);
                if(gappleInHotbar == -1) {
                    return;
                }
                        PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(gappleInHotbar - 36));
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                eating = 0;
            } else if (eating > 35) {
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                timer.reset();
            }
        } else if (mode.getValue()==agmode.Head) {
            int headInHotbar = InventoryUtils.findItem(36, 45, Items.skull);
            if (headInHotbar != -1) {
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(headInHotbar - 36));
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                timer.reset();
            }
        }
    }
    @SubscribeEvent
    public void onPacket(PacketEvent e){
        Packet<?> packet = e.getPacket();
        if (eating != -1 && packet instanceof C03PacketPlayer) {
            eating++;
        } else if (packet instanceof S09PacketHeldItemChange || packet instanceof C09PacketHeldItemChange) {
            eating = -1;
        }
    }
    enum agmode{
        Auto,
        Legitauto,
        Head
    }
}
