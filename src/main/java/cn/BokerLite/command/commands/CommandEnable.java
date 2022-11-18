package cn.BokerLite.command.commands;

import net.minecraft.util.EnumChatFormatting;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.utils.mod.Helper;

import java.util.Objects;

public class CommandEnable extends Command {
    public CommandEnable() {
        super("enable", new String[]{"turnon", "open"}, "Enable a module", ACType.Module);
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            Objects.requireNonNull(ModuleManager.getModule(args[0])).setState(true);
            Helper.sendMessage(Objects.requireNonNull(ModuleManager.getModule(args[0])).getName() + " " + EnumChatFormatting.GREEN + "Enabled");
        } else {
            Helper.sendMessage("Correct usage: .enable <module>");
        }
        return null;
    }
}