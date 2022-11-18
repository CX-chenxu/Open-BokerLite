package cn.BokerLite.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.misc.Location;
import cn.BokerLite.utils.mod.Particles;
import cn.BokerLite.utils.reflect.ReflectionUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DMGParticle extends Module {
    private final HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
    private final List<Particles> particles = new ArrayList<>();

    public DMGParticle() {
        super("DMGParticle", "伤害显示", Keyboard.KEY_NONE, ModuleType.Render, "Damage Particle",ModuleType.SubCategory.RENDER_WORLD);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        EntityLivingBase entity = (EntityLivingBase) e.entity;
        if (entity == mc.thePlayer) {
            return;
        }
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put(entity, entity.getHealth());
        }
        float floatValue = this.healthMap.get(entity);
        float health = entity.getHealth();
        if (floatValue != health) {
            String text;
            if (floatValue - health < 0.0f) {
                text = "\247a" + roundToPlace((floatValue - health) * -1.0f, 1);
            } else {
                text = "\247e" + roundToPlace(floatValue - health, 1);
            }
            Location location = new Location(entity);
            location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
            location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
            location.setZ(location.getZ() - 0.5 + new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5) * 0.1);
            this.particles.add(new Particles(location, text));
            this.healthMap.remove(entity);
            this.healthMap.put(entity, entity.getHealth());
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        RenderManager renderManager = mc.getRenderManager();
        try {
            for (Particles p : this.particles) {
                if (p.ticks < Minecraft.getDebugFPS()) {
                    double x = p.location.getX();
                    mc.getRenderManager();
                    double n = x - (double) ReflectionUtil.getFieldValue(renderManager, "renderPosX", "field_78725_b");
                    double y = p.location.getY();
                    mc.getRenderManager();
                    double n2 = y - (double) ReflectionUtil.getFieldValue(renderManager, "renderPosY", "field_78726_c");
                    double z = p.location.getZ();
                    mc.getRenderManager();
                    double n3 = z - (double) ReflectionUtil.getFieldValue(renderManager, "renderPosZ", "field_78728_n");
                    GlStateManager.pushMatrix();
                    GlStateManager.enablePolygonOffset();
                    GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                    GlStateManager.translate((float) n, (float) n2, (float) n3);
                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                    float textY;
                    if (mc.gameSettings.thirdPersonView == 2) {
                        textY = -1.0f;
                    } else {
                        textY = 1.0f;
                    }
                    GlStateManager.rotate(mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
                    final double size = 0.03;
                    GlStateManager.scale(-size, -size, size);
                    enableGL2D();
                    disableGL2D();
                    GL11.glDepthMask(false);
                    mc.fontRendererObj.drawStringWithShadow(p.text, (float) (-(mc.fontRendererObj.getStringWidth(p.text) / 5)), (float) ((float) (-(mc.fontRendererObj.FONT_HEIGHT - 1)) + p.ticks / (Minecraft.getDebugFPS() / 50.0)), 0);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDepthMask(true);
                    GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                    GlStateManager.disablePolygonOffset();
                    GlStateManager.popMatrix();
                    p.ticks++;
                } else {
                    particles.remove(new Particles(p.location, p.text));
                }
            }
        } catch (NullPointerException | ConcurrentModificationException ex) {
            //
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent eventUpdate) {
        try {
            this.particles.forEach(this::lambda$onUpdate$0);
        } catch (ConcurrentModificationException ex) {
            //
        }
    }

    private void lambda$onUpdate$0(Particles update) {
        ++update.ticks;
        if (update.ticks <= 10) {
            update.location.setY(update.location.getY() + update.ticks * 0.005);
        }
        if (update.ticks > 20) {
            this.particles.remove(update);
        }
    }
}