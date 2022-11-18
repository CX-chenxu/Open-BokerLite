package cn.BokerLite.utils.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import cn.BokerLite.Client;

public enum Helper {
    INSTANCE;

    public static Minecraft mc;

    public static void sendMessage(String s) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[BokerLite Client] " + s));
    }

    public void message(String s) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[BokerLite Client] " + s));
    }

    public static boolean onServer(String server) {
        return !mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
    }

    public boolean nullCheck() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer == null || mc.theWorld == null;
    }

    public static void sendMessageWithoutPrefix(String string) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(string));
    }
    public static void debug(String string){
        if (!Client.debug.getValue().booleanValue()){
            return;
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[debug] " + string));
    }
}