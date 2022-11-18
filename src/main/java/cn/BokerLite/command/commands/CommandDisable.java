package cn.BokerLite.command.commands;

import net.minecraft.util.EnumChatFormatting;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.utils.mod.Helper;

import java.util.Objects;

public class CommandDisable extends Command {
    public CommandDisable() {
        super("disable", new String[]{"turnoff", "close"}, "Disable a module", ACType.Module);
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            Objects.requireNonNull(ModuleManager.getModule(args[0])).setState(false);
            Helper.sendMessage(Objects.requireNonNull(ModuleManager.getModule(args[0])).getName() + " " + EnumChatFormatting.RED + "Disabled");
        } else {
            Helper.sendMessage("Correct usage: .disbale <module>");
        }
        return null;
    }
}