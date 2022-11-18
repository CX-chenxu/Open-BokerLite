package cn.BokerLite.modules.Player;

public class TimerUtil {
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public void setTime(long l) {
        this.lastMS = l;
    }

    public boolean hasTimeElapsed(long l, boolean bl) {
        if (System.currentTimeMillis() - this.lastMS > l) {
            if (bl) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean delay(Double d) {
        return (double)(this.getTime() - this.lastMS) >= d;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public boolean delay(float f) {
        return (float)(this.getTime() - this.lastMS) >= f;
    }

    public boolean hasTimeElapsed(long l) {
        return System.currentTimeMillis() - this.lastMS > l;
    }
}
