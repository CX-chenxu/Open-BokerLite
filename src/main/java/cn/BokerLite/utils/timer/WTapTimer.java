package cn.BokerLite.utils.timer;

public class WTapTimer {
    private long start;
    private long lasts;

    public WTapTimer(long lasts) {
        this.lasts = lasts;
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed() {
        return System.currentTimeMillis() >= start + lasts;
    }

    public void setCooldown(long time) {
        this.lasts = time;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.start;
    }

    public long getTimeLeft() {
        return lasts - (System.currentTimeMillis() - start);
    }
}