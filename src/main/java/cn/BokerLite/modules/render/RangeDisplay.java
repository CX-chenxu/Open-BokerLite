package cn.BokerLite.modules.render;

import cn.BokerLite.modules.combat.LegitAura;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.render.GLUtils;

import java.awt.*;

public class RangeDisplay extends Module {
    public RangeDisplay() {
        super("RangeDisplay", "范围显示", Keyboard.KEY_NONE, ModuleType.Render, "Rendering Killaura Range",ModuleType.SubCategory.RENDER_SELF);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event)
    {
        if(Client.nullCheck())
            return;
        drawCircle(mc.thePlayer, event.partialTicks, LegitAura.reach.getValue());
    }

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GLUtils.startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(3);

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;
        float r, g, b;
        r = 0.003921569f * (float) Color.RED.getRed();
        g = 0.003921569f * (float) Color.RED.getGreen();
        b = 0.003921569f * (float) Color.RED.getBlue();

        for (int i = 0; i <= 90; ++i) {
            GL11.glColor3f(r, g, b);
            GL11.glVertex3d(x + rad * Math.cos((double) i * 6.283185307179586 / 8.0), y, z + rad * Math.sin((double) i * 6.283185307179586 / 8.0));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GLUtils.endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);

    }
}
