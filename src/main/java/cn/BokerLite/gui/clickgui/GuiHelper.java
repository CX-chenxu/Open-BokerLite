package cn.BokerLite.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiHelper {
    public static Minecraft mc = Minecraft.getMinecraft();

    // 打开GUISCREEN用这个可以绕过网易Clickgui检查
    public static void displayGuiScreen(GuiScreen guiScreenIn) {
        mc.currentScreen = guiScreenIn;
        if (guiScreenIn != null) {
            try {
                mc.setIngameNotInFocus();
                ScaledResolution scaledresolution = new ScaledResolution(mc);
                int i = scaledresolution.getScaledWidth();
                int j = scaledresolution.getScaledHeight();
                mc.currentScreen.mc = Minecraft.getMinecraft();
                mc.currentScreen.height = j;
                mc.currentScreen.width = i;
                mc.currentScreen.initGui();
                mc.skipRenderWorld = false;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
