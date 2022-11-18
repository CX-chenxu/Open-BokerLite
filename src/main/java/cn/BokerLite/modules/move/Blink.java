package cn.BokerLite.modules.move;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.reflect.ReflectionUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class Blink extends Module {
    private EntityOtherPlayerMP blinkEntity;
    private final ArrayList<Packet<?>> packetList = new ArrayList<>();
    public Blink() {
        super("Blink", "数据包阻塞",Keyboard.KEY_NONE, ModuleType.Player,"Packet blink",ModuleType.SubCategory.PLayer_assist);
    }

    @Override
    public void enable() {
        if (mc.thePlayer == null) {
            return;
        }
        this.blinkEntity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69L, 96L), mc.thePlayer.getName()));
        this.blinkEntity.inventory = mc.thePlayer.inventory;
        this.blinkEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
        this.blinkEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        this.blinkEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(this.blinkEntity.getEntityId(), this.blinkEntity);
    }

    @EventHandler
    public void onPacketSend(EventPacketSend event) {
        if (event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C0APacketAnimation
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            this.packetList.add(event.getPacket());
            event.setCancelled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
    	GL11.glLineWidth(4F);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
//    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	RenderManager renderManager = mc.getRenderManager();
    	double x = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosX", "field_78725_b");
        double y = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosY", "field_78726_c");
        double z = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosZ","field_78728_n");
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_LINE_STRIP);
    	double lastX, lastY, lastZ, dist;
    	lastX = lastY = lastZ = dist = 0;
    	for (Packet<?> packet : this.packetList) {
    		if(packet instanceof C03PacketPlayer && (packet.getClass() == C03PacketPlayer.C04PacketPlayerPosition.class || packet.getClass() == C03PacketPlayer.C06PacketPlayerPosLook.class)) {
    			C03PacketPlayer p = (C03PacketPlayer) packet;
    			int color = Color.HSBtoRGB((float) (dist / 5D) % 1F, 1F, 1F);
    	        float r = (color >> 16 & 0xFF) / 255.0f,
    	        	  g = (color >> 8 & 0xFF) / 255.0f,
    	        	  b = (color & 0xFF) / 255.0f;
    	        double diffX = p.getPositionX() - lastX,
    	        	   diffY = p.getPositionY() - lastY,
    	        	   diffZ = p.getPositionZ() - lastZ;
    	        dist += Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    	        lastX = p.getPositionX();
    	        lastY = p.getPositionY();
    	        lastZ = p.getPositionZ();
    	        GL11.glColor4f(r, g, b, 1F);
    			GL11.glVertex3d(lastX - x, lastY - y, lastZ - z);
    		}
    	}
    	GL11.glEnd();
    	GL11.glShadeModel(GL11.GL_FLAT);
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glLineWidth(1F);
    }

    @Override
    public void disable() {
        for (Packet<?> packet : this.packetList) {
            mc.getNetHandler().addToSendQueue(packet);
        }
        this.packetList.clear();
        mc.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
    }
}