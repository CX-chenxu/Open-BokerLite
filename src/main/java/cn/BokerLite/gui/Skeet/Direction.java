//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.BokerLite.gui.Skeet;

public enum Direction {
    FORWARDS,
    BACKWARDS;

    Direction() {
    }

    public Direction opposite() {
        return this == FORWARDS ? BACKWARDS : FORWARDS;
    }
}
