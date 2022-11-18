package cn.BokerLite.modules.render;


import cn.BokerLite.modules.value.ColorValue;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.render.SessionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Option;

import java.awt.*;

public class HUD extends Module {
    public static final Option<Boolean> waterMark = new Option<>("WaterMark", "水印", "WaterMark", true);
    public static final Option<Boolean> arrayList = new Option<>("ArrayList", "功能列表", "ArrayList", false);
    public static final Option<Boolean> potion = new Option<>("PotionHUD", "药水HUD", "PotionHUD", true);
    public static final Option<Boolean> rainbow = new Option<>("Rainbow", "彩虹模式", "Rainbow", true);
    public static final Option<Boolean> inv = new Option<>("InventoryHUD", "背包HUD", "InventoryHUD", false);
    public static final Option<Boolean> TextShdow = new Option<>("TextShdow", "TextShdow", "TextShdow", false);
    public static final Option<Boolean> TextRect = new Option<>("TextRect", "TextRect", "TextRect", false);
    public static Numbers x = new Numbers("X", "X", "X",3.0, 1.0, 1920.0, 5.0);
    public static Numbers y = new Numbers("Y", "Y","Y", 35.0,1.0,1080.0, 5.0);
    public static ColorValue Hudcolor = new ColorValue("Hudcolor",new Color(80,177,241).getRGB());
    public static ColorValue Icons = new ColorValue("ICONSColor",new Color(80,177,241).getRGB());
    public static ColorValue Text = new ColorValue("TextColor",new Color(80,177,241).getRGB());

    public static final Mode<Enum<WaterMark>> mode = new Mode<>("Notification", "Notification", WaterMark.values(), WaterMark.Onetap);
    public static  Mode<Enum<notification>> Notificationmode = new Mode<>("Notification", "Notification", notification.values(), notification.Boker);


    public HUD() {
        super("HUD", "界面显示", Keyboard.KEY_NONE, ModuleType.Render, "HUD Rendering",ModuleType.SubCategory.RENDER_OVERLAY);
        setState(true);
    }

    public enum WaterMark {
        NeverLose,
        Boker,
        Onetap
    }
    public enum notification{
        Boker,NeverLose
    }
}
