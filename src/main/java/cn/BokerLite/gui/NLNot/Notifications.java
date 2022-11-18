package cn.BokerLite.gui.NLNot;


import cn.BokerLite.gui.EV0.Settings.DecelerateAnimation;
import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.Direction;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.render.ColorUtils;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;


public class Notifications {
    private final float spacing = 10;




    private final float widthSpacing = 25;
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();
    Animation downAnimation = null;
    public void drawNotifications() {

        int count = 0;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        for (Notification notification : notifications) {
            if (notification.timerUtil.hasTimeElapsed((long) notification.getMaxTime(), false)) {
                if (notification.getAnimation() != null) {
                    if (notification.getAnimation().isDone()) {
                        notifications.remove(notification);
                        downAnimation = new DecelerateAnimation(225, 1, Direction.FORWARDS);
                        continue;
                    }
                } else vanish(notification);
            } else {
                if (notification.getAnimation() != null) {
                    if (notification.getAnimation().isDone()) notification.stopAnimation();
                }
            }

            float notifWidth = notification.getWidth() + widthSpacing;
            float notifX = sr.getScaledWidth() - (notifWidth + 5);
            if (count == 0) notification.notificationY = sr.getScaledHeight();
            notification.notificationY = notifications.get(Math.max(count - 1, 0)).notificationY - spacing - notification.getHeight();

            if (notification.isAnimating()) notifX += notifWidth * notification.getAnimation().getOutput();

            if (downAnimation != null) {
                if (downAnimation.isDone()) {
                    downAnimation = null;
                    return;
                }

                float newY = sr.getScaledHeight() - (spacing + notification.getHeight()) * (count + 2);
                notification.notificationY = (float) (newY + ((notification.getHeight() + spacing) * downAnimation.getOutput()));
            }

            notificationDraw(notifX, notification.notificationY, notifWidth, notification.getHeight(), notification);

            count++;
        }
    }

    public void notificationDraw(float x, float y, float width, float height, Notification notification) {
        int color = -1;
        String iconText = "";
        float yOffset = 8;
        float xOffset = 5;
        switch (notification.getNotificationType()) {
            case SUCCESS:
                iconText = FontLoaders.CHECKMARK;
                break;
            case WARNING:
                iconText = FontLoaders.WARNING;
                break;
            case DISABLE:
                iconText = FontLoaders.XMARK;
                yOffset = 9;
                break;
            case INFO:
                iconText = FontLoaders.INFO;
                xOffset = 7;
                break;
        }

        Color baseColor = new Color(3,11,23, 110);
        Color colorr = ColorUtils.interpolateColorC(baseColor, new Color(ColorUtils.applyOpacity(baseColor.getRGB(), .3f)), 0.5F);

        RenderUtil.drawRect(x, y, x+400, y+30,colorr);

        //背景
        RenderUtil.drawArc(x + width - 17,y +14,10,new Color(HUD.Icons.getValue()).getRGB(),180,720,5);

        //画圆
        RenderUtil.drawArc(x + width - 17,y +14,10,new Color(HUD.Icons.getValue()).getRGB(),180,notification.getArcAnimation() == null ? 180 : (720 - 180) * notification.getArcAnimation().getOutput(),5);

        // 取一位小数
        DecimalFormat df = new DecimalFormat("0.0");

        //绘制秒数
        FontLoaders.NL16.drawCenteredString( df.format((notification.getArcAnimation().getOutput()* notification.getMaxTime() /1000)) + "s"  , x + width - 17,y +12,-1);


        notification.titleFont.drawString(notification.getTitle(), x + 28, y + 3, -1);
        notification.iconFont.drawString(iconText, x + xOffset, y + yOffset, new Color(HUD.Icons.getValue()).getRGB());
        notification.descriptionFont.drawString(notification.getDescription(), x + 28, y + 16, -1);
    }


    public static void post(NotificationType type, String title, String description) {
        post(new Notification(type, title, description));
    }

    public static void post(NotificationType type, String title, String description, float time) {
        post(new Notification(type, title, description, time));
    }

    private static void post(Notification notification) {
        notifications.add(notification);
        notification.setArcAnimation(new DecelerateAnimation((int) notification.getMaxTime(), 1, Direction.FORWARDS));
        notification.startAnimation(new DecelerateAnimation(225, 1, Direction.BACKWARDS));
    }

    public static void vanish(Notification notification) {
        notification.startAnimation(new DecelerateAnimation(225, 1, Direction.FORWARDS));
    }




}
