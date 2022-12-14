package cn.BokerLite.utils.timer;

public class TimeHelper {
    private long lastMs;

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMs > delay;
    }

    public boolean delay(double delay) {
        return System.currentTimeMillis() - this.lastMs > delay;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long) i;
    }
    public boolean isDelayComplete(Double d) {
        return (double)(System.currentTimeMillis() - this.lastMs) > d;
    }

}

