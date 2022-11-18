package cn.BokerLite.modules.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.utils.timer.MSTimer;

public class AutoPlay extends Module {
  private final Mode<AutoPlayMode> mode = new Mode<>("Mode","Mode", AutoPlayMode.values(), AutoPlayMode.Paper);
    public AutoPlay() {
        super("AutoPlay", "自动再来一局",Keyboard.KEY_NONE, ModuleType.Player, "AutoPlay",ModuleType.SubCategory.PLayer_assist);
    }
    @SubscribeEvent
    public void onDick(TickEvent e){
            if(Client.nullCheck())
                return;
            int slot=findPaperSlot();
            if (slot!=-1){
                if (delaytimer.hasTimePassed(1800)){
              // Helper.sendMessage("Sending You to next Game...");
                    //Why does it spams message?
                Minecraft.getMinecraft().thePlayer.inventory.currentItem=slot-36;
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()));
                mc.thePlayer.swingItem();
                delaytimer.reset();
            }

        }

    }
    MSTimer delaytimer=new MSTimer();
    private int findPaperSlot() {
        try {
            for(int i=36;i<45;i++) {
            	ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && mode.getValue().isPaper(stack)) {
                    return i;
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
    enum AutoPlayMode {
        Paper {
			@Override
			public boolean isPaper(ItemStack stack) {
				// TODO Auto-generated method stub
				return stack.getItem() == Items.paper && (stack.getDisplayName().toLowerCase().contains("again") || stack.getDisplayName().contains("一局"));
			}
		};
        public abstract boolean isPaper(ItemStack stack);
    }
}