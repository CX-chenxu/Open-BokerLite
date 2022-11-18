package cn.BokerLite.modules.world;


import cn.BokerLite.modules.Player.TimerUtil;
import cn.BokerLite.modules.render.OreFinder;
import cn.BokerLite.utils.PlayerUtil;
import cn.BokerLite.utils.packet.PacketUtil;
import com.google.common.collect.Lists;
import com.jcraft.jogg.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.block.nBlockPos;
import cn.BokerLite.utils.movement.noslow.Tools;
import cn.BokerLite.utils.render.RenderHelper;

import java.util.*;

public class Xray extends Module {
    public static final Numbers Reach = new Numbers("Range", "透视范围", "Range", 20.0, 5.0, 30.0, 1.0);
    private final Option<Boolean> iron = new Option<>("Iron", "铁矿", "Iron", true);
    private final Option<Boolean> gold = new Option<>("Gold", "金矿", "Gold", true);
    private final Option<Boolean> coal = new Option<>("Coal", "Coal", "Coal", true);
    private final Option<Boolean> diamond = new Option<>("Diamond", "钻石矿", "Diamond", true);
    private final Option<Boolean> emerald = new Option<>("Emerald", "绿宝石矿", "Emerald", true);
    private final Option<Boolean> redstone = new Option<>("Redstone", "红石矿", "Redstone", true);
    private final Option<Boolean> lapis = new Option<>("Lapis", "青金石矿", "Lapis", true);
    private final List<BlockPos> xrayList = new ArrayList<>();
    private final TimerUtil timer = new TimerUtil();
    public int rangeInt = -1;
    public static List blockIdList = Lists.newArrayList(10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62, 73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154);
    List<Integer> KEY_IDS;
    public List<BlockPos> result;

    public static List<BlockPos> blockPosList;
    public static Numbers opacity = new Numbers("Opacity","Opacity","Opacity", 160.0D, 0.0D, 255.0D, 5.0D);

    public static Numbers delay = new Numbers("Delay","Delay","Delay", 10.0D, 0.5D, 30.0D, 0.5D);

    public Numbers depth = new Numbers("Test Depth","Test Depth", "Test Depth", 2d, 1d, 5d, 1d);
    public Numbers range = new Numbers("Range", "Range",  "Range", 32d, 20d, 100d, 1d);
    public OreFinder closeFinder;
    public static Option extreme = new Option("extreme","extreme", "extreme",  true);
    public static Option update = new Option("Update","Update", "Update",  true);


    public OreFinder farFinder;
    public static int alpha;

    Block[] _extreme_var0;

    public Xray() {
        super("Xray", "矿物透视", Keyboard.KEY_NONE, ModuleType.World, "Ore esp",ModuleType.SubCategory.World_World);
        this._extreme_var0 = new Block[]{Blocks.diamond_ore, Blocks.gold_ore};
    }

    private java.util.Timer t;
    public nBlockPos pos = new nBlockPos();
    public static List<BlockPos> toRender = new ArrayList<>();


    @Override
    public void enable() {
        blockIdList.clear();
        blockPosList.clear();
        mc.renderGlobal.loadRenderers();
        try {
            Iterator var1 = this.KEY_IDS.iterator();

            while(var1.hasNext()) {
                Object var2 = var1.next();
                Integer var3 = (Integer)var2;
                blockIdList.add(var3);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if ((Boolean)extreme.getValue() && mc.thePlayer.posY <= 25.0D) {
            this.doExtreme();
        }

        int var5 = (int) mc.thePlayer.posX;
        int var6 = (int) mc.thePlayer.posY;
        int var7 = (int) mc.thePlayer.posZ;
        mc.renderGlobal.markBlockRangeForRenderUpdate(var5 - 900, var6 - 900, var7 - 900, var5 + 900, var6 + 900, var7 + 900);

        super.enable();
        (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
    }
    public void doExtreme() {
        boolean var1 = false;
        boolean var2 = false;
        int var3 = ((Number)range.getValue()).intValue();

        for(int var4 = -var3; var4 <= var3; ++var4) {
            for(int var5 = -var3; var5 <= var3; ++var5) {
                for(int var6 = -var3; var6 <= var3; ++var6) {
                    EntityPlayerSP var7 = mc.thePlayer;
                    int var8 = (int)var7.posX + var4;
                    int var9 = (int)var7.posY + var5;
                    int var10 = (int)var7.posZ + var6;
                    new BlockPos(var8, var9, var10);
                    Block var12 = PlayerUtil.getBlock(var8, var9, var10);
                    Block var13 = mc.theWorld.getBlockState(new BlockPos(var8, var9, var10)).getBlock();
                    if (mc.thePlayer.getDistance(var8, var9, var10) <= depth.getValue() || var13 instanceof BlockAir) {
                        boolean var15 = false;
                        Block[] var14;
                        Block[] var16 = var14 = this._extreme_var0;
                        int var17 = var14.length;

                        for(int var18 = 0; var18 < var17; ++var18) {
                            Block var19 = var16[var18];
                            if (var13.equals(var19)) {
                                var15 = true;
                                break;
                            }
                        }

                        var15 = var15 && (var13.getBlockHardness(mc.theWorld, BlockPos.ORIGIN) != -1.0F || mc.playerController.isInCreativeMode());
                        boolean var22 = false;
                        Iterator var20 = blockPosList.iterator();

                        while(var20.hasNext()) {
                            BlockPos var21 = (BlockPos)var20.next();
                            if (var8 == var21.getX() && var9 == var21.getY() && (double)var10 == (double)var21.getZ()) {
                                var22 = true;
                                break;
                            }
                        }

                        BlockPos var23 = new BlockPos(var8, var9, var10);
                        if (var15 && !var22) {
                            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, var23, EnumFacing.UP));
                            if (!blockPosList.contains(var23)) {
                                blockPosList.add(var23);
                            }
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void onEventRE(TickEvent var1) {

        if ((Boolean) extreme.getValue()) {
            System.out.println("DS");
            this.doExtreme();
        }


        long time = System.currentTimeMillis();
        int r = rangeInt != -1 ? rangeInt : range.getValue().intValue();
        List<BlockPos> tmp = new ArrayList<>();
        for (int x = -r; x <= r; x++)
            for (int y = 1; y <= 128; y++)
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, y, mc.thePlayer.posZ + z);
                    if (isTarget(pos) && oreTest(pos, depth.getValue().intValue())) {
                        blockPosList.add(pos);

                    }
                }

        // ChatUtils.debug("Using time:" + (System.currentTimeMillis() - time));
    

    }



    public boolean isTarget(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return diamond.getValue();
        } else if (Blocks.lapis_ore.equals(block)) {
            return lapis.getValue();
        } else if (Blocks.iron_ore.equals(block)) {
            return iron.getValue();
        } else if (Blocks.gold_ore.equals(block)) {
            return gold.getValue();
        } else if (Blocks.coal_ore.equals(block)) {
            return coal.getValue();
        } else if (Blocks.emerald_ore.equals(block)) {
            return emerald.getValue();
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return redstone.getValue();
        }
        return false;
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
                xrayList.clear();
                int ra = Reach.getValue().intValue();

                for(int y = ra; y >= -ra; --y) {
                    for(int x = -ra; x <= ra; ++x) {
                        for(int z = -ra; z <= ra; ++z) {
                            if (Tools.isPlayerInGame()) {
                                BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double)x, Module.mc.thePlayer.posY + (double)y, Module.mc.thePlayer.posZ + (double)z);
                                Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                                if (iron.getValue() && bl.equals(Blocks.iron_ore) || gold.getValue() && bl.equals(Blocks.gold_ore) || diamond.getValue() && bl.equals(Blocks.diamond_ore) || emerald.getValue() && bl.equals(Blocks.emerald_ore) || lapis.getValue() && bl.equals(Blocks.lapis_ore) || redstone.getValue() && bl.equals(Blocks.redstone_ore)) {
                                    xrayList.add(p);
                                }
                            }
                        }
                    }
                }

            }
        };
    }

    public static void re(BlockPos bp,
                           boolean shade) {
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
            final float[] color = getColor(bp);

            GL11.glColor4d(color[0],color[1], color[2], 255);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
            if (shade) {
                RenderHelper.dbb(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), color[0],color[1], color[2]);
            }

            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }

    private int[] c(Block b) {
        int red = 0;
        int green = 0;
        int blue = 0;
        if (b.equals(Blocks.iron_ore)) {
            red = 255;
            green = 255;
            blue = 255;
        } else if (b.equals(Blocks.gold_ore)) {
            red = 255;
            green = 255;
        } else if (b.equals(Blocks.diamond_ore)) {
            green = 220;
            blue = 255;
        } else if (b.equals(Blocks.emerald_ore)) {
            red = 35;
            green = 255;
        } else if (b.equals(Blocks.lapis_ore)) {
            green = 50;
            blue = 255;
        } else if (b.equals(Blocks.redstone_ore)) {
            red = 255;
        } else if (b.equals(Blocks.mob_spawner)) {
            red = 30;
            blue = 135;
        }

        return new int[]{red, green, blue};
    }
    public static float[] getColor(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return new float[]{0, 1, 1};
        } else if (Blocks.lapis_ore.equals(block)) {
            return new float[]{0, 0, 1};
        } else if (Blocks.iron_ore.equals(block)) {
            return new float[]{1, 1, 1};
        } else if (Blocks.gold_ore.equals(block)) {
            return new float[]{1, 1, 0};
        } else if (Blocks.coal_ore.equals(block)) {
            return new float[]{0, 0, 0};
        } else if (Blocks.emerald_ore.equals(block)) {
            return new float[]{0, 1, 0};
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return new float[]{1, 0, 0};
        }
        return new float[]{0, 0, 0};
    }
    public Boolean oreTest(BlockPos origPos, double depth) {
        Collection<BlockPos> posesNew = new ArrayList<>();
        Collection<BlockPos> posesLast = new ArrayList<>(Collections.singletonList(origPos));
        Collection<BlockPos> finalList = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            for (BlockPos blockPos : posesLast) {
                posesNew.add(blockPos.up());
                posesNew.add(blockPos.down());
                posesNew.add(blockPos.north());
                posesNew.add(blockPos.south());
                posesNew.add(blockPos.west());
                posesNew.add(blockPos.east());
            }
            for (BlockPos pos : posesNew) {
                if (posesLast.contains(pos)) {
                    posesNew.remove(pos);
                }
            }
            posesLast = posesNew;
            finalList.addAll(posesNew);
            posesNew = new ArrayList<>();
        }

        List<Block> legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air, Blocks.flowing_water);

        return finalList.stream().anyMatch(blockPos -> legitBlocks.contains(mc.theWorld.getBlockState(blockPos).getBlock()));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {


        for (BlockPos blockPos : blockPosList) {
            final float[] color = getColor(blockPos);
           re(blockPos,true);
        }


    }
    private void renderBlock(BlockPos pos) {
        if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.air) return;
        double x = (double) pos.getX() - mc.getRenderManager().viewerPosX;
        double y = (double) pos.getY() - mc.getRenderManager().viewerPosY;
        double z = (double) pos.getZ() - mc.getRenderManager().viewerPosZ;
        final float[] color = getColor(pos);
        drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2], 0.5f, 3);
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}
