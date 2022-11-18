package cn.BokerLite.command.commands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import cn.BokerLite.api.enums.ACType;
import cn.BokerLite.command.Command;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.movement.Teleport;
import cn.BokerLite.utils.packet.PacketUtil;

import java.util.ArrayList;

public class CommandTP extends Command {
    public CommandTP() {
        super("teleport", new String[]{"teleport", "tip"}, "Teleport", ACType.Player);
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 0) {
            if (args.length == 3) {
                final double x = Double.parseDouble(args[0]);
                final double y = Double.parseDouble(args[1]);
                final double z = Double.parseDouble(args[2]);

                Teleport.tpToLocation(
                        500, 9.5, 9.0,
                        new ArrayList<>(), new ArrayList<>(),
                        new BlockPos(x, y, z)
                );

               // mc.thePlayer.setPositionAndUpdate(x, y, z);
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x,y,z,true));

                Helper.sendMessage("Teleported to the coordinates " + x + " " + y + " " + z + ".");
            } else {
                final EntityPlayer player = mc.theWorld.playerEntities
                        .stream()
                        .filter(entity -> entity.getGameProfile().getName().equalsIgnoreCase(args[0]))
                        .findFirst()
                        .orElse(null);

                if (player != null) {
                    final double x = player.posX;
                    final double y = player.posY;
                    final double z = player.posZ;

                    Teleport.tpToLocation(
                            500, 9.5, 9.0,
                            new ArrayList<>(), new ArrayList<>(),
                            new BlockPos(x, y, z)
                    );
                    //mc.thePlayer.setPositionAndUpdate(x, y, z);
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x,y,z,true));
                    Helper.sendMessage("Successfully teleported to " + player.getName() + ".");
                } else {
                    Helper.sendMessage("Could not locate the player given!");
                }
            }
        } else {
            Helper.sendMessage("Correct usage: .tip <x> <y> <z> | .tp <player>");
        }
        return null;
    }
}