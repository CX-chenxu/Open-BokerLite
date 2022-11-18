package cn.BokerLite.modules.value;

import java.awt.*;

public class Colors extends Value<Color> {
    Color color;

    public Colors(String displayName, Color color) {
        super(displayName, displayName);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    public static int getColor(int brightness, int alpha)
    {
        return getColor(brightness, brightness, brightness, alpha);
    }
    public static int getColor(int red, int green, int blue, int alpha)
    {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    public void setColor(Color c) {
        this.color = c;
    }
}

