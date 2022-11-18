package cn.BokerLite.utils.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public final class PacketUtil {
    public static ArrayList<Packet<?>> noEventPacket = new ArrayList<>();
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacketWithoutEvent(final Packet<?> packet) {
        noEventPacket.add(packet);
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    @SuppressWarnings("unused")
    public void sendPacket(final Packet<?> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
