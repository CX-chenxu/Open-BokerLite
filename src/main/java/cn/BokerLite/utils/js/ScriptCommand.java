package cn.BokerLite.utils.js;

import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.command.Command;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.mod.MessageUtils;

import javax.script.Invocable;
import javax.script.ScriptException;

public class ScriptCommand extends Command {
    private final Invocable invoke;
    private final String name;

    public ScriptCommand(String scriptCommandName, Invocable invokable) {
        super(scriptCommandName, new String[]{}, "", ACType.Player);
        this.name = scriptCommandName;
        this.invoke = invokable;
    }

    @Override
    public String execute(String[] args) {
        try {
            invoke.invokeFunction("execute", (Object) args);
        } catch (ScriptException e) {
            Helper.sendMessage(e.getMessage());
        } catch (NoSuchMethodException e) {
            JSManager.unloadScript(this.name);
            MessageUtils.send("ScriptManager", name + " have no execute fuction. unloaded", NotificationType.WARNING);
        }
        return null;
    }
}
