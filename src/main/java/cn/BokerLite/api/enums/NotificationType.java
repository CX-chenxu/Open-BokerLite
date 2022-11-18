package cn.BokerLite.api.enums;

import java.awt.*;

public enum NotificationType {
    SUCCESS(new Color(6348946).getRGB()),
    INFO(new Color(6590631).getRGB()),
    WARNING(new Color(0xFF7800).getRGB()),
    ERROR(new Color(0xFF2F2F).getRGB());

    private final int color;

    NotificationType(int color) {
        this.color = color;
    }

    public final int getColor() {
        return this.color;
    }
}
