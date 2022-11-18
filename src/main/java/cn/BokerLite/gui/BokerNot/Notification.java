package cn.BokerLite.gui.BokerNot;




import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.RenderUtil;
import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;


import java.awt.*;
import java.util.Locale;

public class Notification {
    public float x;
    public float width, height;
    public String name;
    public float lastTime;
    public TimerUtil timer;
    public Type type;
    public boolean setBack;
    private float fy, cy = 0;
    private final TimerUtil anitimer = new TimerUtil();
    private final AnimationUtils animationUtils = new AnimationUtils();
    private final AnimationUtils animationUtils2 = new AnimationUtils();


    public Notification(String name, Type type) {
        this.name = name;
        this.type = type;
        this.lastTime = 1.5f;
        this.width = FontLoaders.NL16.getStringWidth(name) + 25;
        this.height = 20;
    }

    public Notification(String name, Type type, float lastTime) {
        this.name = name;
        this.type = type;
        this.lastTime = lastTime;
        this.width = FontLoaders.NL16.getStringWidth(name) + 25;
        this.height = 20;

    }

    public void render(float y) {
        fy = y;
        if (cy == 0) {
            cy = fy + 25;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy-20, (float) ((sr.getScaledWidth_double() - x) + width), cy + height-10, new Color(30, 30, 30, 150));
        if (type == Type.Alert){
            RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) (sr.getScaledWidth_double() - x) + width, cy + height-10, new Color(180, 180, 180));
        } else if (type == Type.Info) {
            RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) (sr.getScaledWidth_double() - x) + width, cy + height-10, new Color(41,68,40));
        } else if (type == Type.Error) {
            RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) (sr.getScaledWidth_double() - x) + width, cy + height-10, new Color(57,28,37));

        }

        if (timer != null) {
            if (type == Type.Alert){
                RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) ((sr.getScaledWidth_double() - x+14) + (this.timer.getTime() - timer.lastMS) / (lastTime * 1000) * width), cy + height-10, new Color(0, 110, 255));
            } else if (type == Type.Info) {
                RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) ((sr.getScaledWidth_double() - x+14) + (this.timer.getTime() - timer.lastMS) / (lastTime * 1000) * width), cy + height-10, new Color(101,193,123));
            } else if (type == Type.Error) {
                RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x+12), cy + height - 1-10, (float) ((sr.getScaledWidth_double() - x+14) + (this.timer.getTime() - timer.lastMS) / (lastTime * 1000) * width), cy + height-10, new Color(184,64,73));

            }

        }
        FontLoaders.NL18.drawString("Module", (sr.getScaledWidth() - x) + 15, cy + 7-20, new Color(59, 226, 229).getRGB());

        FontLoaders.NL18.drawString(name, (sr.getScaledWidth() - x) + 14, cy + 7-10, -1);
    }

    public void update() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (timer == null && Math.abs(x - width) < 0.1f) {
            timer = new TimerUtil();
            timer.reset();
        }
        if (anitimer.delay(10)) {
            cy = animationUtils.animate(fy, cy, 0.1f);

            if (!setBack) {
                x = animationUtils2.animate(width, x, 0.1f);
            } else {
                x = animationUtils2.animate(0, x, 0.1f);
            }
            anitimer.reset();
        }
    }

    public enum Type {
        Info,
        Alert,
        Error
    }

}
