package cn.BokerLite.modules.value;

import com.google.gson.JsonElement;

public abstract class Value<V> {
    private final String displayName;
    private String name;
    private V value;

    public Value(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V getValue() {
        return this.value;
    }


    public void setValue(V value) {
        this.value = value;
    }
}

