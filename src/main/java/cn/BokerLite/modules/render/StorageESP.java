package cn.BokerLite.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Colors;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.render.RenderHelper;

import java.awt.*;

public class StorageESP extends Module {

    private static Minecraft mc;

    static {
        StorageESP.mc = Minecraft.getMinecraft();
    }

    private final Option<Boolean> Chest = new Option<>("Chest", "箱子", "Chest", true);
    private final Option<Boolean> EnderChest = new Option<>("EnderChest", "末影箱", "EnderChest", false);

    public StorageESP() {
        super("StorageESP", "箱子ESP", Keyboard.KEY_NONE, ModuleType.Render, "Chest and Ore Renderer ESP",ModuleType.SubCategory.RENDER_OVERLAY);
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent ev) {
        for (final TileEntity te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntityChest && this.Chest.getValue()) {
                int rgb = (new Color(0,125,227)).getRGB();
            RenderHelper.drawBlockESP(te.getPos(), rgb);
            }
            if (te instanceof TileEntityEnderChest && this.EnderChest.getValue()) {
                int rgb = (new Color(0,125,227)).getRGB();
                RenderHelper.drawBlockESP(te.getPos(), rgb);
            }
        }
    }
}
