package cn.BokerLite.utils.mod;

import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.gui.notification.NotificationPublisher;


public enum MessageUtils {
    INSTANCE;

    public static void send(String title, String message, NotificationType type) {
        if (Client.nullCheck())
            return;
        NotificationPublisher.queue(title, message, type);
    }

    public void error(String title, String message) {
        send(title, message, NotificationType.ERROR);
    }

    public void success(String title, String message) {
        send(title, message, NotificationType.SUCCESS);
    }
}
