package cn.BokerLite.utils;

import cn.BokerLite.utils.timer.TimerUtils;

public final class DelayTimer {
    private long prevTime;
    private final TimerUtils timerUtils = new TimerUtils(true);

    public boolean delay(int tick) {
        int aps = 20 / tick;
        return timerUtils.hasReached(50 * aps);
    }
    public boolean isDelayComplete(double d) {
        return this.hasPassed(d);
    }
    public boolean hasPassed(double milli) {
        return System.currentTimeMillis() - this.prevTime >= milli;
    }
    public void reset() {
        timerUtils.reset();
    }

}
