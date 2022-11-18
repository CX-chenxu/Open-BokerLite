package cn.BokerLite.modules.render;

import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.world.Xray;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;


import java.util.ArrayList;
import java.util.List;

public class OreFinder extends Thread {
    public Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<BlockPos> diamond = new ArrayList<>();
    public static Xray blockESP ;
    public Numbers range, depth;
    public List<BlockPos> result;


    public int rangeInt = -1;

    public boolean isRunning = false;

    public OreFinder(Numbers range, Numbers depth) {
        this.range = range;
        this.depth = depth;
    }

    public OreFinder(int range, Numbers depth) {
        this.rangeInt = range;
        this.depth = depth;
    }


    @Override
    public void run() {
        while (ModuleManager.getModule(Xray.class).getState() && mc.theWorld != null) {
            System.out.println("1");
            isRunning = true;
            long time = System.currentTimeMillis();
            int r = rangeInt != -1 ? rangeInt : range.getValue().intValue();
            List<BlockPos> tmp = new ArrayList<>();
            for (int x = -r; x <= r; x++)
                for (int y = 1; y <= 128; y++)
                    for (int z = -r; z <= r; z++) {
                        BlockPos pos = new BlockPos(mc.thePlayer.posX + x, y, mc.thePlayer.posZ + z);
                        if (blockESP.isTarget(pos) && blockESP.oreTest(pos, depth.getValue().intValue())) {
                            tmp.add(pos);
                            if (mc.theWorld.getBlockState(pos).getBlock() == Blocks.diamond_ore && !diamond.contains(pos)) {
                                diamond.add(pos);

                            }
                        }
                    }
            result = tmp;
           // ChatUtils.debug("Using time:" + (System.currentTimeMillis() - time));
        }
        isRunning = false;
    }
}
