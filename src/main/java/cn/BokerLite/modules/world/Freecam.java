package cn.BokerLite.modules.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;

import java.util.UUID;

public class Freecam extends Module {
    private final Option<Boolean> autodisable = new Option<>("AutoDisable", "自动关闭", "AutoDisable", true);
    private static final Numbers speed = new Numbers("Speed", "速度", "Speed", 2.5D, 0.5D, 10.0D, 1.0D);
    public static EntityOtherPlayerMP en = null;
    private int[] lcc = new int[]{Integer.MAX_VALUE, 0};
    private EntityOtherPlayerMP entityOtherPlayerMP;
    private final float[] sAng = new float[]{0.0F, 0.0F};
    public Freecam() {
        super("Freecam", "自由视角", Keyboard.KEY_NONE, ModuleType.World, "Allows you Freecam",ModuleType.SubCategory.World_World);
    }
    
    
    @Override
    public void enable() {
        if(Client.nullCheck()) {
            return;
        }
        if (!mc.thePlayer.onGround) {
            this.disable();
        } else {
            en = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            en.copyLocationAndAnglesFrom(mc.thePlayer);
            this.sAng[0] = en.rotationYawHead = mc.thePlayer.rotationYawHead;
            this.sAng[1] = mc.thePlayer.rotationPitch;
            en.setVelocity(0.0D, 0.0D, 0.0D);
            en.setInvisible(true);
            mc.theWorld.addEntityToWorld(-8008, en);
            mc.setRenderViewEntity(en);
        }

        entityOtherPlayerMP = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69L, 96L), mc.thePlayer.getName()));
        entityOtherPlayerMP.inventory = mc.thePlayer.inventory;
        entityOtherPlayerMP.inventoryContainer = mc.thePlayer.inventoryContainer;
        entityOtherPlayerMP.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        entityOtherPlayerMP.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(this.entityOtherPlayerMP.getEntityId(), this.entityOtherPlayerMP);
    }
    
    @Override
    public void disable() {
        if (en != null) {
            mc.setRenderViewEntity(mc.thePlayer);
            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYawHead = this.sAng[0];
            mc.thePlayer.rotationPitch = this.sAng[1];
            mc.theWorld.removeEntity(en);
            en = null;
        }
        mc.theWorld.removeEntity(entityOtherPlayerMP);
        
        this.lcc = new int[]{Integer.MAX_VALUE, 0};
        int x = mc.thePlayer.chunkCoordX;
        int z = mc.thePlayer.chunkCoordZ;
        
        for(int x2 = -1; x2 <= 1; ++x2) {
            for(int z2 = -1; z2 <= 1; ++z2) {
                int a = x + x2;
                int b = z + z2;
                mc.theWorld.markBlockRangeForRenderUpdate(a * 16, 0, b * 16, a * 16 + 15, 256, b * 16 + 15);
            }
        }
        
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent e) {
        if(Client.nullCheck() || en == null)
            return;
        if (autodisable.getValue() && mc.thePlayer.hurtTime != 0) {
            this.disable();
        } else {
            mc.thePlayer.setSprinting(false);
            mc.thePlayer.moveForward = 0.0F;
            mc.thePlayer.moveStrafing = 0.0F;
            en.rotationYaw = en.rotationYawHead = mc.thePlayer.rotationYaw;
            en.rotationPitch = mc.thePlayer.rotationPitch;
            double s = 0.215D * speed.getValue();
            EntityOtherPlayerMP var10000;
            double rad;
            double dx;
            double dz;
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                rad = (double)en.rotationYawHead * 0.017453292519943295D;
                dx = -1.0D * Math.sin(rad) * s;
                dz = Math.cos(rad) * s;
                var10000 = en;
                var10000.posX += dx;
                var10000.posZ += dz;
            }
            
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                rad = (double)en.rotationYawHead * 0.017453292519943295D;
                dx = -1.0D * Math.sin(rad) * s;
                dz = Math.cos(rad) * s;
                var10000 = en;
                var10000.posX -= dx;
                var10000.posZ -= dz;
            }
            
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                rad = (double)(en.rotationYawHead - 90.0F) * 0.017453292519943295D;
                dx = -1.0D * Math.sin(rad) * s;
                dz = Math.cos(rad) * s;
                var10000 = en;
                var10000.posX += dx;
                var10000.posZ += dz;
            }
            
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                rad = (double)(en.rotationYawHead + 90.0F) * 0.017453292519943295D;
                dx = -1.0D * Math.sin(rad) * s;
                dz = Math.cos(rad) * s;
                var10000 = en;
                var10000.posX += dx;
                var10000.posZ += dz;
            }
            
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                var10000 = en;
                var10000.posY += 0.93D * s;
            }
            
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                var10000 = en;
                var10000.posY -= 0.93D * s;
            }
            
            mc.thePlayer.setSneaking(false);
            if (this.lcc[0] != Integer.MAX_VALUE && (this.lcc[0] != en.chunkCoordX || this.lcc[1] != en.chunkCoordZ)) {
                int x = en.chunkCoordX;
                int z = en.chunkCoordZ;
                mc.theWorld.markBlockRangeForRenderUpdate(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
            }
            
            this.lcc[0] = en.chunkCoordX;
            this.lcc[1] = en.chunkCoordZ;
        }
    }
    
    @SubscribeEvent
    public void m(MouseEvent e) {
        if (!Client.nullCheck() && e.button != -1) {
            e.setCanceled(true);
        }
    }
}