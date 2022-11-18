package cn.BokerLite.utils.packet;

import cn.BokerLite.api.EventBus;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.api.event.ForgeEventManager;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.utils.reflect.ReflectionUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;


public class PacketManager {
    public static final PacketManager INSTANCE = new PacketManager();

    private PacketManager() {}

    public void init() {
        ForgeEventManager.EVENT_BUS.register(this);


    }

    public void uninject() {
        ForgeEventManager.EVENT_BUS.unregister(this);


        Minecraft mc = Minecraft.getMinecraft();
        NetworkManager netMgr = (NetworkManager) ReflectionUtil.getFieldValue(mc, "myNetworkManager", "field_71453_ak");
        if (netMgr != null)
            netMgr.channel().pipeline().remove("BokerLite");
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        NetworkManager netMgr;
        if (e == null) {
            netMgr = mc.thePlayer.sendQueue.getNetworkManager();
        } else
            netMgr = e.manager;
        if (netMgr != null)
            netMgr.channel().pipeline().addBefore("packet_handler", "BokerLite", new PacketListener());
    }
}

class PacketListener extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Packet packet = (Packet) msg;
            EventPacketRecieve packetSend = new EventPacketRecieve((Packet<?>) msg);
            EventBus.getInstance().call(packetSend);

       //     System.out.println("OK");
            if (!packetSend.isCancelled()) {
               super.channelRead(ctx, msg);
            //    System.out.println("1");

        }
   //     super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        final Packet packet = (Packet) msg;
        //System.out.println(1);
        if (PacketUtil.noEventPacket.contains(msg)) {

            super.write(ctx, msg, promise);
            PacketUtil.noEventPacket.remove(msg);
            return;
        }
        EventPacketSend packetSend = new EventPacketSend((Packet<?>) msg);
        EventBus.getInstance().call(packetSend);
        //    System.out.println("OK");
            if (!packetSend.isCancelled()) {
                super.write(ctx, msg, promise);
          //      System.out.println("1");
            }


     //   super.write(ctx, msg, promise);
    }

}