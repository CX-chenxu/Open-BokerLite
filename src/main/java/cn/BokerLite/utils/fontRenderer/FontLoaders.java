/*
 * Decompiled with CFR 0_132.
 */
package cn.BokerLite.utils.fontRenderer;

import cn.BokerLite.utils.fontRenderer.CFont.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FontLoaders {
    public final static String
            BUG = "a",
            LIST = "b",
            BOMB = "c",
            EYE = "d",
            PERSON = "e",
            WHEELCHAIR = "f",
            SCRIPT = "g",
            SKIP_LEFT = "h",
            PAUSE = "i",
            PLAY = "j",
            SKIP_RIGHT = "k",
            SHUFFLE = "l",
            INFO = "m",
            SETTINGS = "n",
            CHECKMARK = "o",
            XMARK = "p",
            TRASH = "q",
            WARNING = "r",
            FOLDER = "s",
            LOAD = "t",
            SAVE = "u";
    public static CFontRenderer NL14 = new CFontRenderer(FontLoaders.getNL(14), true, true);
    public static CFontRenderer NL24 = new CFontRenderer(FontLoaders.getNL(24), true, true);
    public static CFontRenderer NL22 = new CFontRenderer(FontLoaders.getNL(22), true, true);
    public static CFontRenderer NL20 = new CFontRenderer(FontLoaders.getNL(20), true, true);
    public static CFontRenderer NL18 = new CFontRenderer(FontLoaders.getNL(18), true, true);
    public static CFontRenderer NL16 = new CFontRenderer(FontLoaders.getNL(16), true, true);
    public static CFontRenderer NLLogo20 = new CFontRenderer(FontLoaders.getNL2(20), true, true);
    public static CFontRenderer NLLogo18 = new CFontRenderer(FontLoaders.getNL2(18), true, true);
    public static CFontRenderer NLLogo22 = new CFontRenderer(FontLoaders.getNL2(22), true, true);
    public static CFontRenderer NL45 = new CFontRenderer(FontLoaders.getNL2(45), true, true);
    public static CFontRenderer NL40 = new CFontRenderer(FontLoaders.getNL2(40), true, true);
    public static CFontRenderer NL35 = new CFontRenderer(FontLoaders.getNL2(35), true, true);
    public static CFontRenderer icon35 = new CFontRenderer(FontLoaders.geticon(35), true, true);
    public static CFontRenderer icon24 = new CFontRenderer(FontLoaders.geticon(24), true, true);
    public static CFontRenderer icon26 = new CFontRenderer(FontLoaders.geticon(26), true, true);
    public static CFontRenderer iconFont35 = new CFontRenderer(FontLoaders.geticon2(35), true, true);
    public static CFontRenderer icon28 = new CFontRenderer(FontLoaders.geticon(28), true, true);
    public static CFontRenderer NLose35 = new CFontRenderer(FontLoaders.getNL(35), true, true);





    public static Font getNL(int size) {
        Font font;
        try {
            InputStream is = (Files.newInputStream(Paths.get("C:/Program Files/BokerLite/tahoma.ttf")));
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font geticon(int size) {
        Font font;
        try {
            InputStream is = (Files.newInputStream(Paths.get("C:/Program Files/BokerLite/icon.ttf")));
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font geticon2(int size) {
        Font font;
        try {
            InputStream is = (Files.newInputStream(Paths.get("C:/Program Files/BokerLite/icon2.ttf")));
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public static Font getNL2(int size) {

        Font font;
        try {
            InputStream is = (Files.newInputStream(Paths.get("C:/Program Files/BokerLite/neverlose.ttf")));
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Load Default font");
            font = new Font("default", 0, size);
        }
        return font;
    }

}

