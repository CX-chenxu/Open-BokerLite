package cn.BokerLite.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;

import java.util.List;
import java.util.Random;

public class Reach extends Module {
    public static Numbers MinReach = new Numbers("MinReach", "最小攻击距离", "MinReach", 3.5, 0.0, 6.0, 0.1);
    public static Numbers MaxReach = new Numbers("MaxReach", "最大攻击距离", "MaxReach", MinReach.getValue() + 1.0, 0.1, 6.0, 0.1);
    private final Option<Boolean> RandomReach = new Option<>("Random Reach", "随机Reach", "Random Reach", true);
    private final Option<Boolean> weaponOnly = new Option<>("Weapon Only", "仅武器", "Weapon Only", false);
    private final Option<Boolean> movingOnly = new Option<>("Moving Only", "仅移动", "Moving Only", false);
    private final Option<Boolean> sprintOnly = new Option<>("Sprint Only", "仅疾跑", "Sprint Only", false);
    private final Option<Boolean> hitThroughBlocks = new Option<>("HitThroughBlocks", "穿墙攻击", "HitThroughBlocks", false);

    public Reach() {
        super("Reach", "攻击距离提升", Keyboard.KEY_NONE, ModuleType.Combat, "Make you can attack far target",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    public static Object[] doReach(final double reachValue, final double AABB) {
        final Entity target = mc.getRenderViewEntity();
        Entity entity = null;
        if (target == null || mc.theWorld == null) {
            return null;
        }
        mc.mcProfiler.startSection("pick");
        final Vec3 targetEyes = target.getPositionEyes(0.0f);
        final Vec3 targetLook = target.getLook(0.0f);
        final Vec3 targetVector = targetEyes.addVector(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue);
        Vec3 targetVec = null;
        final List<Entity> targetHitbox = mc.theWorld.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().addCoord(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue).expand(1.0, 1.0, 1.0));
        double reaching = reachValue;
        for (final Entity targetEntity : targetHitbox) {
            if (targetEntity.canBeCollidedWith()) {
                final float targetCollisionBorderSize = targetEntity.getCollisionBorderSize();
                AxisAlignedBB targetAABB = targetEntity.getEntityBoundingBox().expand(targetCollisionBorderSize, targetCollisionBorderSize, targetCollisionBorderSize);
                targetAABB = targetAABB.expand(AABB, AABB, AABB);
                final MovingObjectPosition tagetPosition = targetAABB.calculateIntercept(targetEyes, targetVector);
                if (targetAABB.isVecInside(targetEyes)) {
                    if (0.0 < reaching || reaching == 0.0) {
                        entity = targetEntity;
                        targetVec = ((tagetPosition == null) ? targetEyes : tagetPosition.hitVec);
                        reaching = 0.0;
                    }
                } else if (tagetPosition != null) {
                    final double targetHitVec = targetEyes.distanceTo(tagetPosition.hitVec);
                    if (targetHitVec < reaching || reaching == 0.0) {
                        if (targetEntity == target.ridingEntity) {
                            if (reaching == 0.0) {
                                entity = targetEntity;
                                targetVec = tagetPosition.hitVec;
                            }
                        } else {
                            entity = targetEntity;
                            targetVec = tagetPosition.hitVec;
                            reaching = targetHitVec;
                        }
                    }
                }
            }
        }
        if (reaching < reachValue && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        mc.mcProfiler.endSection();
        if (entity == null || targetVec == null) {
            return null;
        }
        return new Object[]{entity, targetVec};
    }
    
    @SubscribeEvent
    public void onMove(final MouseEvent ev) {
        if (this.weaponOnly.getValue()) {
            if (mc.thePlayer.getCurrentEquippedItem() == null) {
                return;
            }
            if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                return;
            }
        }
        if (this.movingOnly.getValue() && mc.thePlayer.moveForward == 0.0 && mc.thePlayer.moveStrafing == 0.0) {
            return;
        }
        if (this.sprintOnly.getValue() && !mc.thePlayer.isSprinting()) {
            return;
        }
        if (!this.hitThroughBlocks.getValue() && mc.objectMouseOver != null) {
            final BlockPos blocksReach = mc.objectMouseOver.getBlockPos();
            if (blocksReach != null && mc.theWorld.getBlockState(blocksReach).getBlock() != Blocks.air) {
                return;
            }
        }
        double Reach;
        if (this.RandomReach.getValue()) {
            Reach = getRandomDoubleInRange(MinReach.getValue(), MaxReach.getValue()) + 0.1;
        } else {
            Reach = MinReach.getValue();
        }
        final Object[] reachs = doReach(Reach, 0.0);
        if (reachs == null) {
            return;
        }
        mc.objectMouseOver = new MovingObjectPosition((Entity) reachs[0], (Vec3) reachs[1]);
        mc.pointedEntity = (Entity) reachs[0];
    }

    @SubscribeEvent
    public void onUpdate(TickEvent e) {
        if (this.RandomReach.getValue()) {
            this.setSuffix(MaxReach.getValue() + "");
        } else {
            this.setSuffix(MinReach.getValue() + "");
        }
    }
    
}

