package cn.BokerLite.gui.alt.gui;

import java.util.ArrayList;
import java.util.List;

public class AltManager {
    static List<Alt> alts;
    static Alt lastAlt;

    public static void init() {
        AltManager.setupAlts();
    }

    public static void setupAlts() {
        alts = new ArrayList<>();
    }

    public static List<Alt> getAlts() {
        return alts;
    }

    public static Alt getLastAlt() {
        return lastAlt;
    }

    public static void setLastAlt(Alt alt) {
        lastAlt = alt;
    }
}
