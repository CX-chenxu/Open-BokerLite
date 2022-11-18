package cn.BokerLite.modules.move;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.mod.EntitySize;

import java.util.List;

public class HitBox extends Module {
    private final Numbers heights = new Numbers("Height", "高度", "Height", 2.0, 2.0, 5.0, 0.1);
    private final Numbers Widths = new Numbers("Width", "宽度", "Width", 1.0, 1.0, 5.0, 0.1);

    public HitBox() {
        super("HitBox", "碰撞箱修改", Keyboard.KEY_NONE, ModuleType.Combat, "Change hitbox",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    public static void setEntityBoundingBoxSize(Entity entity, float width, float height) {
        EntitySize size = getEntitySize();
        entity.width = size.width;
        entity.height = size.height;
        double d0 = (double) (width) / 2.0D;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0,
                entity.posY + (double) height, entity.posZ + d0));
    }
    
    public static void setEntityBoundingBoxSize(Entity entity) {
        EntitySize size = getEntitySize();
        entity.width = size.width;
        entity.height = size.height;
        double d0 = (double) (entity.width) / 2.0D;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0,
                entity.posY + (double) entity.height, entity.posZ + d0));
    }
    
    public static EntitySize getEntitySize() {
        return new EntitySize(0.6F, 1.8F);
    }
    
    public static List<EntityPlayer> getPlayersList() {
        return mc.theWorld.playerEntities;
    }
    
    public boolean check(EntityLivingBase entity) {
        if (entity instanceof EntityPlayerSP) {
            return false;
        }
        if (entity == mc.thePlayer) {
            return false;
        }
        return !entity.isDead;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        for (EntityPlayer player : getPlayersList()) {
            if (!check(player)) continue;
            float width = this.Widths.getValue().floatValue();
            float height = this.heights.getValue().floatValue();
            setEntityBoundingBoxSize(player, width, height);
        }
    }
    
    @Override
    public void enable() {
    
    }
    
    @Override
    public void disable() {
        for (EntityPlayer player : getPlayersList())
            setEntityBoundingBoxSize(player);
    }
    
}
