package cn.BokerLite.utils.js;

import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.mod.Helper;

@SuppressWarnings("unused")
public class JSValue {
    private final Module mod;

    public JSValue(Module mod) {
        this.mod = mod;
    }

    public Numbers addNumberValue(String name, double value, double min, double max) {
        Numbers num = new Numbers(name, name, name, value, min, max, 0.1);
        try {
            mod.addValues(num);
        } catch (Exception e) {
            Helper.sendMessage(e.getMessage());
        }
        return num;
    }

    public Option<Boolean> addBooleanValue(String name, boolean state) {
        Option<Boolean> bool = new Option<>(name, name, name, state);
        try {
            mod.addValues(bool);
        } catch (Exception e) {
            Helper.sendMessage(e.getMessage());
        }
        return bool;
    }
}
