package cn.BokerLite.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Option;

import java.lang.reflect.Field;

public class FastPlace extends Module {
    private final Option<Boolean> blockOnly = new Option<>("BlockOnly", "仅方块", "BlockOnly", false);
    
    public FastPlace() {
        super("FastPlace", "快速放置",Keyboard.KEY_NONE, ModuleType.World, "Make you place the blocks and RightClick faster",ModuleType.SubCategory.World_World);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent event) {
        if (!this.state)
            return;
        if (this.blockOnly.getValue()) {
            if (mc.thePlayer.getCurrentEquippedItem() == null) {
                return;
            }
            if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                return;
            }
        }
        try {
            final Field rightClickDelay = Minecraft.class.getDeclaredField("field_71467_ac");
            rightClickDelay.setAccessible(true);
            rightClickDelay.set(FastPlace.mc, 0);
        } catch (Exception d) {
            try {
                final Field e = Minecraft.class.getDeclaredField("rightClickDelayTimer");
                e.setAccessible(true);
                e.set(FastPlace.mc, 0);
            } catch (Exception f) {
                this.disable();
            }
        }
    }
    
}
