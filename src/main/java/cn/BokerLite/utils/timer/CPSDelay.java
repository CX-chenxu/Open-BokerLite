package cn.BokerLite.utils.timer;

public final class CPSDelay {
    private final TimerUtils timerUtils = new TimerUtils(true);

    public boolean shouldAttack(int cps) {
        int aps = 20 / cps;
        return timerUtils.hasReached(50 * aps);
    }

    public void reset() {
        timerUtils.reset();
    }
}
