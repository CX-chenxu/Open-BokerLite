package cn.BokerLite.modules.Player;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.friend.FriendManager;

public class MCF extends Module {
    public MCF() {
        super("MCF", "中键好友",Keyboard.KEY_NONE, ModuleType.Player, "Add Friend targte mind clicked",ModuleType.SubCategory.PLayer_Player);
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (Mouse.isButtonDown(2)) {
            if (mc.objectMouseOver.entityHit != null) {
                Entity ent = mc.objectMouseOver.entityHit;
                String name = ent.getName();
                if (!FriendManager.isFriend(name)) {
                    FriendManager.addFriend(name);
                } else {
                    FriendManager.removeFriend(name);
                }
            }
        }
    }
}
