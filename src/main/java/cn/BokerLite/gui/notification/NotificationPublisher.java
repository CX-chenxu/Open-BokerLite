package cn.BokerLite.gui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.gui.FontRenderer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public final class NotificationPublisher {

    public static final List<NotificationObject> NOTIFICATIONS = new CopyOnWriteArrayList<>();

    public static void publish(ScaledResolution sr) {
        if (NOTIFICATIONS.isEmpty()) {
            return;
        }
        int srScaledHeight = sr.getScaledHeight();
        int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 30;
        for (NotificationObject notification : NOTIFICATIONS) {
            Translate translate = notification.getTranslate();
            int width = notification.getWidth();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtils.animate(width, notification.scissorBoxWidth, 0.1);
                translate.interpolate(scaledWidth - width, y, 0.15);
            } else {
                notification.scissorBoxWidth = AnimationUtils.animate(0.0, notification.scissorBoxWidth, 0.1);
                if (notification.scissorBoxWidth < 1.0) {
                    NOTIFICATIONS.remove(notification);
                }
                y += 30;
            }
            // TODO: 修复抖动我也不知道为什么Notification一直瞎几把抖
            float translateX = translate.getX();
            float translateY = translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtils.prepareScissorBox((float) Math.ceil(scaledWidth - notification.scissorBoxWidth), translateY, scaledWidth, translateY + 30.0f);
            RenderUtils.drawRect(translateX, translateY, scaledWidth, translateY + 30.0f, -1879048192);
            RenderUtils.drawRect(translateX, translateY + 30.0f - 2.0f, translateX + width * ((long) notification.getTime() - notification.getTimer().getElapsedTime()) / notification.getTime(), translateY + 30.0f, notification.getType().getColor());
            FontRenderer.F16.drawString(notification.getTitle(), translateX + 4, translateY + 4, -1);
            FontRenderer.F16.drawString(notification.getContent(), translateX + 4, translateY + 17, -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            y -= 30;
        }
    }

    public static void queue(String title, String content, NotificationType type) {
        if (Client.nullCheck())
            return;
        Minecraft mc = Minecraft.getMinecraft();
        NOTIFICATIONS.add(new NotificationObject(title, content, type, mc.fontRendererObj));
    }
}