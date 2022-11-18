package cn.BokerLite.modules.value;

public class Labels<T> extends Value<T> {
    String text;

    public Labels(String displayName, String text) {
        super(displayName, displayName);
        this.text = text;
    }

    public String getString() {
        return text;
    }

    public void setString(String str) {
        this.text = str;
    }
}

