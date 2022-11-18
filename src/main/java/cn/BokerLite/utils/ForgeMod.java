package cn.BokerLite.utils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import cn.BokerLite.Client;

@Mod(modid="iliiilliiillii", name="iliiilliiillii", version="iliiilliiillii", acceptedMinecraftVersions="[1.8.9]", clientSideOnly = true)
public class ForgeMod {
    @Mod.EventHandler
    public void Mod(FMLPreInitializationEvent event) {
        new Client(); // 开发环境不需要token
    }
}
