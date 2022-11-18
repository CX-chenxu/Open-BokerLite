
package cn.BokerLite.modules.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;

public class AimAssist extends Module {
    public static Numbers speed = new Numbers("Speed", "瞄准速度", "Speed", 45.0, 1.0, 100.0, 1.0);
    public static Numbers fov = new Numbers("FOV", "有效视野", "FOV", 90.0, 15.0, 180.0, 1.0);
    public static Numbers range = new Numbers("Range", "距离", "Range", 4.5, 3.0, 6.0, 0.1);
    public static Option<Boolean> clickAim = new Option<>("Click", "点击瞄准", "Click", true);

    public static Option<Boolean> weaponOnly = new Option<>("Weapon only", "仅武器", "Weapon only", false);

    public static Option<Boolean> locky = new Option<>("Blatant Lock", "暴力锁定", "Blatant Lock", false);

    public AimAssist() {
        super("AimAssist", "瞄准辅助", Keyboard.KEY_NONE, ModuleType.Combat, "Help your aim target",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    @SubscribeEvent
    public void update(TickEvent event) {
        if (state) {
            if (AimAssist.weaponOnly.getValue()) {
                if (AimAssist.mc.thePlayer.getCurrentEquippedItem() == null) {
                    return;
                }
                if (!(AimAssist.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) && !(AimAssist.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                    return;
                }
            }
            if (AimAssist.clickAim.getValue() && !mc.gameSettings.keyBindAttack.isKeyDown()) {
                return;
            }
            final Entity h = this.getTarget();
            if (!AimAssist.locky.getValue()) {
                if (h != null && (getRot(h) > 1.0 || getRot(h) < -1.0)) {
                    final boolean i = getRot(h) > 0.0;
                    final EntityPlayerSP thePlayer = AimAssist.mc.thePlayer;
                    thePlayer.rotationYaw += (float)(i ? (-(Math.abs(getRot(h)) / (101.0 - speed.getValue()))) : (Math.abs(getRot(h)) / (101.0 - speed.getValue())));
                }
            }
            else {
                getFoc(h, false);
            }
        }
    }


    public static double getRot(final Entity en) {
        return ((mc.thePlayer.rotationYaw - getRotion(en)) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static void getFoc(final Entity s, final boolean packet) {
        if (s != null) {
            final float[] t = getArray(s);
            if (t != null) {
                final float y = t[0];
                final float p = t[1] + 4.0f;
                if (!packet) {
                    mc.thePlayer.rotationYaw = y;
                    mc.thePlayer.rotationPitch = p;
                }
                else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(y, p, mc.thePlayer.onGround));
                }
            }
        }
    }

    public static float[] getArray(final Entity q) {
        if (q == null) {
            return null;
        }
        final double diffX = q.posX - mc.thePlayer.posX;
        double diffY;
        if (q instanceof EntityLivingBase) {
            final EntityLivingBase EntityLivingBase = (EntityLivingBase)q;
            diffY = EntityLivingBase.posY + EntityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        else {
            diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        final double diffZ = q.posZ - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
    }


    public static float getRotion(final Entity ent) {
        final double x = ent.posX - mc.thePlayer.posX;
        final double y = ent.posY - mc.thePlayer.posY;
        final double z = ent.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795;
        yaw = -yaw;
        double pitch = Math.asin(y / Math.sqrt(x * x + y * y + z * z)) * 57.2957795;
        pitch = -pitch;
        return (float)yaw;
    }

    public Entity getTarget() {
        Entity k = null;
        int f = fov.getValue().intValue();
        for (final Entity ent : AimAssist.mc.theWorld.loadedEntityList) {
            if (ent.isEntityAlive() && ent != AimAssist.mc.thePlayer && AimAssist.mc.thePlayer.getDistanceToEntity(ent) <= range.getValue() && ent instanceof EntityLivingBase) {
                if (state && AntiBot.isServerBot(ent)) {
                    return null;
                }
                if (!locky.getValue() && !isTarget(ent, (float)f)) {
                    return null;
                }
                if (ent.isInvisible()) {
                    return null;
                }
                k = ent;
                f = (int) getRot(ent);
            }
        }
        return k;
    }
    
    public static boolean isTarget(final Entity entity, float b) {
        b *= 0.5;
        final double v = ((AimAssist.mc.thePlayer.rotationYaw - getRotion(entity)) % 360.0 + 540.0) % 360.0 - 180.0;
        return (v > 0.0 && v < b) || (-b < v && v < 0.0);
    }
}
