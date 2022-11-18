package cn.BokerLite.command.commands;

import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.utils.mod.Helper;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("Help", new String[]{"list"}, "Get Help", ACType.None);
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
            for (Command c : CommandManager.getCommands()) {
                Helper.sendMessage(c.getName() + " - " + c.getHelp());
            }
        } else {
            Helper.sendMessage("invalid syntax Valid .help");
        }
        return null;
    }
}