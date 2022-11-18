package cn.BokerLite.command.commands;


import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;


import java.io.IOException;

public class IRC extends Command {
    public IRC(){

        super("IRC", new String[]{"irc"}, "Set Module Binds", ACType.Module);

    }

    @Override
    public String execute(String[] args) {
        return null;
    }
}


