package cn.BokerLite.modules.world;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Colors;
import cn.BokerLite.utils.block.nBlockPos;
import cn.BokerLite.utils.movement.noslow.Tools;
import cn.BokerLite.utils.render.RenderHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class BedESP extends Module {
    private final List<BlockPos> esps = new ArrayList<>();
    public nBlockPos pos = new nBlockPos();
    private java.util.Timer t;



    public BedESP() {
        super("BedESP", "床透视", Keyboard.KEY_NONE, ModuleType.World, "BedESP",ModuleType.SubCategory.World_World);
    }

    public static void re(BlockPos bp, int color, boolean shade) {
        if (bp != null) {
            double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
            double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
            double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            GL11.glColor4d(r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
            if (shade) {
                RenderHelper.dbb(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), r, g, b);
            }

            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void enable() {
        super.enable();
        (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
    }

    @Override
    public void disable() {
        super.disable();
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }
    }

    private TimerTask t() {
        return new TimerTask() {
            public void run() {
                esps.clear();
                int ra = 30;

                for (int y = ra; y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            if (Tools.isPlayerInGame()) {
                                BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                                Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                                if (bl.equals(Blocks.bed)) {
                                    esps.add(p);
                                }
                            }
                        }
                    }
                }

            }
        };
    }

    private void dr(BlockPos p) {
        int[] rgb = this.c();
        if (rgb[0] + rgb[1] + rgb[2] != 0) {
            re(p, (new Color(rgb[0], rgb[1], rgb[2])).getRGB(), true);
        }

    }

    private int[] c() {

        return new int[]{0,125,227};
    }
}
