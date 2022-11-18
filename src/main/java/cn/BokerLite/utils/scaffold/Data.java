package cn.BokerLite.utils.scaffold;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Data implements Comparable<Data> {
    public BlockPos blockPos;
    public EnumFacing enumFacing;
    public double cost;

    public Data(BlockPos blockPos, EnumFacing enumFacing, double cost) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.cost = cost;
    }

    @Override
    public int compareTo(Data data) {
        return (this.cost - data.cost) > 0 ? -1 : (this.cost - data.cost) < 0 ? 1 : 0;
    }

}