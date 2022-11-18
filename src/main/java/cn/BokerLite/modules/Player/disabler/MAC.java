package cn.BokerLite.modules.Player.disabler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.mod.MessageUtils;
import cn.BokerLite.utils.reflect.ReflectionUtil;

public class MAC extends Module {
    public Option<Boolean> resetVL = new Option<>("ResetVL", "重置VL", "ResetVL", false);
    public MAC() {
        super("MACDisabler", "MAC关闭器", Keyboard.KEY_NONE, ModuleType.Player, "Margele anticheat shutdown",ModuleType.SubCategory.PLayer_Player);
    }

    @Override
    public void disable() {
        super.disable();
        if (Client.INSTANCE.isMargeleAntiCheatDetected()) {
            this.setState(true);
            MessageUtils.send("Smart ass", "主播你几把不开Disabler等着被反作弊暴打是吧", NotificationType.ERROR);
        }
    }

    @SubscribeEvent
    public void resetVL(TickEvent.PlayerTickEvent event){
        if(resetVL.getValue()){
            try {
                ReflectionUtil.setFieldValue(Class.forName("cn.margele.netease.clientside.MargeleAntiCheat"),0,"cheatingVl");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void getVL(TickEvent e) {
        try {
            this.setSuffix("VL: " + ReflectionUtil.getFieldValue(Class.forName("cn.margele.netease.clientside.MargeleAntiCheat"), "cheatingVl"));
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @SubscribeEvent
    public void dc1(FMLNetworkEvent.ServerDisconnectionFromClientEvent e) {
        e.setCanceled(true);
    }

    @SubscribeEvent
    public void dc2(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        e.setCanceled(true);
    }

}
