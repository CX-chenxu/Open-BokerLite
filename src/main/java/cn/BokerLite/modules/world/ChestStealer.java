package cn.BokerLite.modules.world;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.utils.timer.TimerUtil;

public class ChestStealer extends Module {
    private final Numbers delay = new Numbers("Delay", "间隔", "delay", 50.0, 0.0, 1000.0, 10.0);
    private final TimerUtil timer = new TimerUtil();
    
    public ChestStealer() {
        super("ChestStealer","自动搜箱子", Keyboard.KEY_NONE, ModuleType.World, "Auto Steal a chest when you open it",ModuleType.SubCategory.World_World);
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (!this.getState())
            return;
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            int i = 0;
            while (i < container.getLowerChestInventory().getSizeInventory()) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null && this.timer.hasReached(this.delay.getValue())) {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                    this.timer.reset();
                }
                ++i;
            }
            if (this.isEmpty()) {
                mc.thePlayer.closeScreen();
            }
        }
    }
    
    private boolean isEmpty() {
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            int i = 0;
            while (i < container.getLowerChestInventory().getSizeInventory()) {
                ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() != null) {
                    return false;
                }
                ++i;
            }
        }
        return true;
    }
    
}
