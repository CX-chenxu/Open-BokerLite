package cn.BokerLite.command.commands;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.utils.mod.Helper;


public class CommandBind extends Command {
    public CommandBind() {
        super("Bind", new String[]{"b"}, "Set Module Binds", ACType.Module);
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 2) {
            Module m = ModuleManager.getModule(args[0]);
            if (m != null) {
                int newKey = Keyboard.getKeyIndex(args[1].toUpperCase());
                m.setKey(newKey);
                Object[] arrobject = new Object[2];
                arrobject[0] = m.getName();
                arrobject[1] = newKey == 0 ? "none" : args[1].toUpperCase();
                Helper.sendMessage(String.format("Bound %s to %s", arrobject));
            } else {
                Helper.sendMessage(EnumChatFormatting.RED + args[0] + " not found!");
            }
        } else {
            Helper.sendMessage("Correct usage: .bind <module> <key>");
        }
        return null;
    }
}