package cn.BokerLite.utils.misc;

import net.minecraft.entity.player.EntityPlayer;

public class KillerData {
    public String playerName;
    public KillerData(EntityPlayer player){
        playerName = player.getName();
    }
}
