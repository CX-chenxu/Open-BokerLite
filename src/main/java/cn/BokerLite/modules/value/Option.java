package cn.BokerLite.modules.value;

public class Option<T>
        extends Value<T> {
    public String ch;

    public Option(String displayName, String ch, String name, T enabled) {
        super(displayName, name);
        this.ch = ch;
        this.setValue(enabled);
    }

}

