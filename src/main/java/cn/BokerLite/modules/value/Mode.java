package cn.BokerLite.modules.value;

public class Mode<V extends Enum<?>>
        extends Value<V> {
    private final V[] modes;
    public boolean openList;

    public Mode(String displayName, String name, V[] modes, V value) {
        super(displayName, name);
        this.modes = modes;
        this.setValue(value);
    }

    public V[] getModes() {
        return this.modes;
    }

    public void setMode(String mode) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(mode)) {
                this.setValue(e);
            }
            ++n2;
        }
    }
    public V getModeGet(int i) {
        return this.modes[i];
    }
    public String getModeAsString() {
        return this.getValue().name();
    }

    public int getModeListinde(String string) {
        V[] arrV = this.modes;
        int n = arrV.length;

        for(int n2 = 0; n2 < n; ++n2) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(string)) {
                return n2;
            }
        }

        return 0;
    }

}

