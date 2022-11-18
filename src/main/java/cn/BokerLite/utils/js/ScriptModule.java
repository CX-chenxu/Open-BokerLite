package cn.BokerLite.utils.js;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.mod.Helper;

import javax.script.Invocable;
import javax.script.ScriptException;


public class ScriptModule extends Module {
    private final Invocable invoke;

    public ScriptModule(String moduleName, ModuleType category, Invocable invocable) {
        super(moduleName, moduleName, Keyboard.KEY_NONE, category, "Script Module",ModuleType.SubCategory.PLayer_Player);
        this.invoke = invocable;
        try {
            invoke.invokeFunction("onLoad");
        } catch (ScriptException e) {
            Helper.sendMessage(e.getMessage());
        } catch (NoSuchMethodException e) {
            // empty catch block
        }
    }

    @SubscribeEvent
    public void EventAttack(AttackEntityEvent event) {
        try {
            invoke.invokeFunction("onAttack", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventChat(ClientChatReceivedEvent event) {
        try {
            invoke.invokeFunction("onChatReceived", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventKey(InputEvent.KeyInputEvent event) {
        try {
            invoke.invokeFunction("onKey", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventPacket(PacketEvent event) {

            try {
                invoke.invokeFunction("onPacketSend", event);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                this.unload();

        }
    }

    @SubscribeEvent
    public void EventPacketReceive(PacketEvent event) {

            try {
                invoke.invokeFunction("onPacketReceive", event);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                this.unload();
            }

    }

    @SubscribeEvent
    public void EventUpdate(TickEvent.PlayerTickEvent event) {
        try {
            invoke.invokeFunction("onUpdate", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventPreUpdate(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            try {
                invoke.invokeFunction("onPreUpdate", event);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                this.unload();
            }
    }

    @SubscribeEvent
    public void EventPostUpdate(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            try {
                invoke.invokeFunction("onPostUpdate", event);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                this.unload();
            }
    }

    @SubscribeEvent
    public void EventRender3D(RenderWorldLastEvent event) {
        try {
            invoke.invokeFunction("onRender3D", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventRender2D(RenderGameOverlayEvent.Text event) {
        try {
            invoke.invokeFunction("onRender2D", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @SubscribeEvent
    public void EventTick(TickEvent event) {
        try {
            invoke.invokeFunction("onTick", event);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @Override
    public void enable() {
        try {
            invoke.invokeFunction("enable");
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    @Override
    public void disable() {
        try {
            invoke.invokeFunction("disable");
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            this.unload();
        }
    }

    private void unload() {
    	MinecraftForge.EVENT_BUS.unregister(this);
    }

}
