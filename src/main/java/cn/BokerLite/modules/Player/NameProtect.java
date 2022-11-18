package cn.BokerLite.modules.Player;

import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

public class NameProtect extends Module {
   public static String name = "BokerLite";
    public NameProtect() {
        super("NameProtect", "名字保护",Keyboard.KEY_NONE, ModuleType.Player, "Protect your name.",ModuleType.SubCategory.PLayer_Player);
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        event.message = new ChatComponentText(event.message.getFormattedText().replaceAll(mc.thePlayer.getName(),name));
    }
}
