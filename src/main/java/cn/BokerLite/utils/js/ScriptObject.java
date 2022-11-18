package cn.BokerLite.utils.js;

import net.minecraft.client.Minecraft;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.mod.MessageUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.Objects;

public class ScriptObject {
    public String name, author, version, category;
    public ScriptModule scriptModule;
    public ScriptCommand scriptCommand;
    public Invocable invoke;

    public ScriptObject(File scriptFile) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
        String scriptContent = FileUtils.readFile(scriptFile);
        invoke = (Invocable) scriptEngine;

        try {
            scriptEngine.eval(scriptContent);
        } catch (ScriptException ignored) {
        }

        this.name = (String) scriptEngine.get("name");
        this.author = (String) scriptEngine.get("author");
        this.version = (String) scriptEngine.get("version");
        this.category = (String) scriptEngine.get("moduleType");
        String type = (String) scriptEngine.get("scriptType");

        if (type == null) {
            type = "Module";
        }

        if ("Command".equals(type)) {
            this.registerCommand(name, invoke);
        }

        ModuleType modCategory = null;
        try {
            modCategory = ModuleType.valueOf(this.category);
        } catch (Exception e) {
            if (scriptCommand == null) {
                MessageUtils.send("ScriptManager", type + " is not a ModuleType", NotificationType.ERROR);
                return;
            }
        }

        if ("Module".equals(type)) {
            this.registerModule(name, modCategory, invoke);
        }

        if (scriptCommand == null) {
            manager.put("values", new JSValue(scriptModule));
        }

        manager.put("mc", Minecraft.getMinecraft());
        manager.put("Helper", Helper.INSTANCE);
        manager.put("Message", MessageUtils.INSTANCE);
        manager.put("Font16", FontRenderer.F16);
        manager.put("Font18", FontRenderer.F18);
        manager.put("Font22", FontRenderer.F22);


        try {
            scriptEngine.eval(scriptContent);
        } catch (ScriptException e) {
            MessageUtils.send("ScriptManager", "Failed to load script!", NotificationType.ERROR);
        }
    }

    public void registerCommand(String commandName, Invocable invocable) {
        for (Command c : CommandManager.getCommands()) {
            if (Objects.equals(c.getName(), commandName)) {
                MessageUtils.send("L", "主播你的Command怎么几把和现有的一模一样是不是几把想谋权篡位", NotificationType.ERROR);
                return;
            }
        }

        scriptCommand = new ScriptCommand(commandName, invocable);
        CommandManager.JScommands.add(scriptCommand);
        CommandManager.registerCommand(scriptCommand);
    }

    public void registerModule(String moduleName, ModuleType category, Invocable invocable) {
        for (Module m : ModuleManager.getModules()) {
            if (Objects.equals(m.getName(), moduleName)) {
                MessageUtils.send("L", "主播你的Module怎么几把和现有的一模一样是不是几把想谋权篡位", NotificationType.ERROR);
                return;
            }
        }


        scriptModule = new ScriptModule(moduleName, category, invocable);
        ModuleManager.getJSModules().add(scriptModule);
        ModuleManager.registerModule(scriptModule);
    }
}
