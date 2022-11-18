package cn.BokerLite.command.commands;


import cn.BokerLite.Client;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.gui.NLNot.NotificationType;
import cn.BokerLite.gui.NLNot.Notifications;
import cn.BokerLite.utils.mod.Helper;

import java.util.Objects;

import static cn.BokerLite.modules.ModuleManager.*;

public class Config extends Command
{

    
    public Config() {
        super("config", new String[] { "cfg", "loadconfig", "preset" }, "config", ACType.Module);

    }

    
    @Override
    public String execute(final String[] args) {
        if (Objects.equals(args[0], "load")) {
            readSettings();
            Notifications.post(NotificationType.SUCCESS,"Config Loadding","Success");
        }
        if (Objects.equals(args[0], "save")) {
            Client.INSTANCE.shutDown();
            Notifications.post(NotificationType.SUCCESS,"Save Loadding","Success");
        }
        return null;
    }
}
