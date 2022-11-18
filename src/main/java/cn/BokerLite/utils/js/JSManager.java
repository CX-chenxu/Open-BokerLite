package cn.BokerLite.utils.js;

import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.clickgui.components.ModuleButton;
import cn.BokerLite.gui.clickgui.components.ModuleWindow;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.mod.MessageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSManager {
    public static List<ScriptObject> scripts;

    public static void loadScripts() {
        for (Module m : ModuleManager.getJSModules()) {
            ModuleManager.unload(m);
        }

        for (Command c : CommandManager.JScommands) {
            CommandManager.commands.remove(c);
        }

        for (ModuleType r : ModuleType.values()) {
            r.getWindow().components.clear();
        }

        for (Module m : ModuleManager.getModules()) {
            ModuleWindow window = m.getModuleType().getWindow();
            window.addComponent(new ModuleButton(m, window));
            window.components.sort((o1, o2) -> {
                if (!(o1 instanceof ModuleButton && o2 instanceof ModuleButton)) {
                    throw new IllegalArgumentException("!!!");
                }
                return ((ModuleButton) o1).getModule().getName().compareTo(((ModuleButton) o2).getModule().getName());
            });
        }

        File clientDir = new File(System.getProperty("user.home"), "BokerLite");
        File scriptDir = new File(clientDir, "JS");

        if (!scriptDir.exists())
            scriptDir.mkdir();

        File[] scriptsFiles = scriptDir.listFiles((dir, name) -> name.endsWith(".js"));

        if (scriptsFiles == null) {
            return;
        }

        scripts = new ArrayList<>();
        for (File scriptFile : scriptsFiles) {
            ScriptObject script = new ScriptObject(scriptFile);
            scripts.add(script);
        }
        MessageUtils.send("ScriptManager", "loaded " + scripts.size() + " js!", NotificationType.SUCCESS);
    }

    public static void unloadScript(String scriptName) {
        Command removeCommand = null;
        Module removeModule = null;
        ScriptObject removeScript = null;

        for (Command command : CommandManager.commands) {
            if (command.getName().equals(scriptName)) {
                removeCommand = command;
            }
        }

        for (Module mod : ModuleManager.getModules()) {
            if (mod.getName().equals(scriptName)) {
                removeModule = mod;
            }
        }

        for (ScriptObject script : scripts) {
            if (script.name.equals(scriptName)) {
                removeScript = script;
            }
        }

        if (removeCommand != null) {
            CommandManager.getCommands().remove(removeCommand);
        }
        if (removeModule != null) {
            ModuleManager.getModules().remove(removeModule);
        }
        if (removeScript != null) {
            scripts.remove(removeScript);
        }
    }

    public boolean isScriptEnabled(ScriptObject script) {
        for (Object value : ModuleManager.getModules()) {
            if (value instanceof ScriptObject) {
                if (value.equals(script)) {
                    return true;
                }
            }
        }

        for (Object value : CommandManager.getCommands()) {
            if (value instanceof ScriptObject) {
                if (value.equals(script)) {
                    return true;
                }
            }
        }
        return false;
    }
}