package cn.BokerLite.modules.Player.disabler;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.mod.MessageUtils;

public class NCP extends Module {
    public NCP() {
        super("NCPDisabler", "NCP关闭器", Keyboard.KEY_NONE, ModuleType.Player, "No Chest Plus Shutdown",ModuleType.SubCategory.PLayer_Player);
    }

    @Override
    public void enable() {
        super.enable();
        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(Float.MAX_VALUE, Float.MAX_VALUE, true));
        MessageUtils.send("Disabler", "NoCheatPlus Disabler Packet is sent.", NotificationType.SUCCESS);
        this.setState(false);
    }
}
