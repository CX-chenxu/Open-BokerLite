package cn.BokerLite.command;

import net.minecraft.client.Minecraft;
import cn.BokerLite.api.enums.ACType;

public abstract class Command {
    private final String name;
    private final String[] alias;
    private final String help;

    private final ACType ac;
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String name, String[] alias, String help, ACType ac) {
        this.name = name.toLowerCase();
        this.help = help;
        this.alias = alias;
        this.ac = ac;
    }

    public abstract String execute(String[] var1);

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getHelp() {
        return this.help;
    }

    public ACType getAC() {
        return ac;
    }
}