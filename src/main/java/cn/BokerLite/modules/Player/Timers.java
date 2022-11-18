package cn.BokerLite.modules.Player;


public final class Timers {
final Nuker this$0;
private long time;

public boolean hasTicksElapsed(int n) {
        return this.time() >= (long)(1000 / n - 50);
        }

public void reset() {
        this.time = System.nanoTime() / 1000000L;
        }

public boolean hasTimeElapsed(long l) {
        return this.time() >= l;
        }

public Timers(Nuker nuker) {
        this.this$0 = nuker;
        this.time = System.nanoTime() / 1000000L;
        }

public long time() {
        return System.nanoTime() / 1000000L - this.time;
        }

public boolean hasTicksElapsed(int n, int n2) {
        return this.time() >= (long)(n / n2 - 50);
        }

public boolean hasTimeElapsed(long l, boolean bl) {
        if (this.time() >= l) {
        if (bl) {
        this.reset();
        }
        return true;
        }
        return false;
        }
        }
