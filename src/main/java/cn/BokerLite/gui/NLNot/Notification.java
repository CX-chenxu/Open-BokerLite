package cn.BokerLite.gui.NLNot;




import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.TimerUtil;
import cn.BokerLite.utils.fontRenderer.CFont.CFontRenderer;
import cn.BokerLite.utils.fontRenderer.FontLoaders;

/**
 * @author qingjiu
 * 22/08/2022
 */
public class Notification {

    private final NotificationType notificationType;
    private final String title, description;
    private final float height = 28, time;
    public float notificationY;
    public CFontRenderer descriptionFont = FontLoaders.NL16;
    public CFontRenderer titleFont = FontLoaders.NLLogo22;
    public CFontRenderer iconFont = FontLoaders.iconFont35;
    public final TimerUtil timerUtil;
    private Animation animation;

    private Animation arcAnimation = null;

    public Notification(NotificationType type, String title, String description) {
        this.title = title;
        this.description = description;
        this.time = 1500; // 1.5 seconds

        timerUtil = new TimerUtil();
        this.notificationType = type;
    }

    public Notification(NotificationType type, String title, String description, float time) {
        this.title = title;
        this.description = description;
        this.time = (long) (time * 1000);
        timerUtil = new TimerUtil();
        this.notificationType = type;
    }




    public NotificationType getNotificationType() {
        return notificationType;
    }

    public float getWidth() {
        return 50 + (float) Math.max(descriptionFont.getStringWidth(description), titleFont.getStringWidth(title));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getHeight() {
        return height;
    }

    public float getMaxTime() {
        return time;
    }

    public void startAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setArcAnimation(Animation arcAnimation) {
        this.arcAnimation = arcAnimation;
    }

    public Animation getArcAnimation() {
        return arcAnimation;
    }

    public void stopAnimation() {
        this.animation = null;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isAnimating() {
        return animation != null && !animation.isDone();
    }

}
