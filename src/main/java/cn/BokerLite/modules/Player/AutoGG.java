package cn.BokerLite.modules.Player;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.resource.Spams;

import java.util.Random;

public class AutoGG extends Module {
    private final Option<Boolean> ad = new Option<>("AD", "广告", "AD", false);
    public AutoGG() {
        super("AutoGG", "自动发送GG",Keyboard.KEY_NONE, ModuleType.World, "Auto Send GG when game over.",ModuleType.SubCategory.PLayer_assist);
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        String title = event.message.getFormattedText();
        if(title == null)
            return;
        if((title.startsWith("§6§l") && title.endsWith("§r")) || (title.startsWith("§c§lYOU") && title.endsWith("§r")) || (title.startsWith("§c§lGame") && title.endsWith("§r")) || (title.startsWith("§c§lWITH") && title.endsWith("§r")) || (title.startsWith("§c§lYARR") && title.endsWith("§r")))
            mc.thePlayer.sendChatMessage("GG! " + (ad.getValue() ? Spams.spams[new Random().nextInt(Spams.spams.length)] : ""));
    }
}
