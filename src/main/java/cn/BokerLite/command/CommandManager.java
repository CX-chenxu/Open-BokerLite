package cn.BokerLite.command;

import cn.BokerLite.api.event.ForgeEventManager;
import cn.BokerLite.command.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager {
    public static final ArrayList<Command> commands = new ArrayList<>();
    public static final ArrayList<Command> JScommands = new ArrayList<>();
    static {
        commands.add(new CommandBind());
        commands.add(new CommandDisable());
        commands.add(new CommandEnable());
        commands.add(new IRC());
        commands.add(new Config());
        commands.add(new CommandHelp());
        commands.add(new CommandRename());
        commands.add(new CommandTP());
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static Optional<Command> getCommand(String name) {
        return CommandManager.commands.stream().filter(c2 -> {
            boolean isAlias = false;
            String[] arrstring = c2.getAlias();
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String str = arrstring[n2];
                if (str.equalsIgnoreCase(name)) {
                    isAlias = true;
                    break;
                }
                ++n2;
            }
            return c2.getName().equalsIgnoreCase(name) || isAlias;
        }).findFirst();
    }

    public static void registerCommand(Command command) {
        commands.add(command);
    }

    public void init() {
    	ForgeEventManager.EVENT_BUS.register(this);
    }
}