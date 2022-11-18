package cn.BokerLite.command.commands;

import net.minecraft.util.EnumChatFormatting;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.modules.Player.NameProtect;
import cn.BokerLite.utils.mod.Helper;

public class CommandRename extends Command {
    public CommandRename() {
        super("rename", new String[]{"rn"}, "Rename NameProtect Fake Name", ACType.Player);
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            NameProtect.name = args[0];
            Helper.sendMessage("Name change to " + EnumChatFormatting.GREEN + args[0]);
        } else {
            Helper.sendMessage("Correct usage: .rename <New>");
        }
        return null;
    }
}