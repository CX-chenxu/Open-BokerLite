package cn.BokerLite.modules;

import cn.BokerLite.api.EventBus;
import cn.BokerLite.gui.BokerNot.Notification;
import cn.BokerLite.gui.BokerNot.NotificationsManager;
import cn.BokerLite.gui.NLNot.NotificationType;
import cn.BokerLite.gui.NLNot.Notifications;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.utils.FileManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;

import cn.BokerLite.api.event.ForgeEventManager;
import cn.BokerLite.modules.value.Value;
import cn.BokerLite.utils.mod.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public boolean state = false;
    public int key;
    public List<Value<?>> values = new ArrayList<>();
    public boolean noToggle = false;

    public boolean remove;
    
    public String description;
    public ModuleType type;
    public ModuleType.SubCategory subCategory;
    public boolean enabledOnStartup= false;
    private String name;
    public ModuleType moduleType;
    public String suffix = "";
    public double animx = 0;
    public double animy = 0;
    public String chinese;
    public boolean arraylistremove = true;

    public Module(String name,String chinese, int key, ModuleType moduleType, String description,ModuleType.SubCategory subCategory) {
        this.name = name;
        this.chinese = chinese;
        this.key = key;
        this.subCategory =subCategory;
        this.moduleType = moduleType;
        this.description = description;
        this.remove = false;

    }
    public boolean wasArrayRemoved() {
        return this.arraylistremove;
    }

    public void setArrayRemoved(boolean arraylistremove) {
        this.arraylistremove = arraylistremove;
    }
    public void setRemoved(boolean remove) {
        this.remove = remove;
    }
    public Module(String name, String chinese, ModuleType moduleType, String description,ModuleType.SubCategory subCategory) {
		this(name, chinese, Keyboard.KEY_NONE, moduleType, description,subCategory);
	}
    public void setAnimx(double aanimx) {
        this.animx = aanimx;
    }



    public void setAnimy(double aanimy) {
        this.animy = aanimy;
    }

    public double getAnimy() {
        return this.animy;
    }

    public ModuleType.SubCategory getSubCategory(){
        return this.subCategory;
    }
	public void addValues(Value<?>... values) {
        
        this.values.addAll(Arrays.asList(values));
    }
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
    }
    public List<Value<?>> getValues() {
        return this.values;
    }

    public double getAnimx() {
        return this.animx;
    }
    public void toggle() {
        if (!noToggle) {
            if (HUD.Notificationmode.getValue() == HUD.notification.NeverLose) {
                Notifications.post((!this.getState() ? NotificationType.SUCCESS : NotificationType.DISABLE), getName(), (!this.getState() ? "Enabled" : "Disabled"));
            } else if (HUD.Notificationmode.getValue() == HUD.notification.Boker) {
                NotificationsManager.addNotification(
                        new Notification(this.getName() + " "  + (!this.getState() ?  ChatFormatting.GREEN+"Enabled" : ChatFormatting.RED+"Disabled"),
                                (!this.getState() ?  Notification.Type.Info : Notification.Type.Error), 1));

            }
            if (this.state) {
                    // Enable Message
               //     MessageUtils.send(ChatFormatting.AQUA+"Module", this.getName() + ChatFormatting.RED+" Disabled", NotificationType.ERROR);
                    EventBus.getInstance().unregister(this);
                    ForgeEventManager.EVENT_BUS.unregister(this);
                 //   System.out.println("Dis");


                } else {
                    // Disabled Message
                //    MessageUtils.send(ChatFormatting.AQUA+"Module", this.getName() +ChatFormatting.GREEN+ " Enabled", NotificationType.SUCCESS);
                    EventBus.getInstance().register(this);
                //    System.out.println("En");
                    ForgeEventManager.EVENT_BUS.register(this);

                }
        }
        mc.thePlayer.playSound("random.click", 0.3f, 0.5f);
        this.setState(!this.state);
    }
    
    public void enable() {
    
    }
    
    public void disable() {
    
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
        String content = "";


        Module m;
        for(Iterator var4 = ModuleManager.getModules().iterator(); var4.hasNext(); content = content + String.format("%s:%s%s", m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator())) {
            m = (Module)var4.next();
        }

        FileManager.save("Binds.txt", content, false);
    }

    public boolean getState() {
        return state;
    }
    
    public void setState(boolean state) {
        if (!Client.nullCheck())
            mc.thePlayer.playSound("random.click", 0.3f, 0.5f);
        if (this.state == state) {
            return;
        }

        this.state = state;
        try {
            if (state) {
                EventBus.getInstance().register(this);
            	ForgeEventManager.EVENT_BUS.register(this);
                enable();
            } else {
                EventBus.getInstance().register(this);
            	ForgeEventManager.EVENT_BUS.unregister(this);
                disable();
            }
        } catch (Exception ex) {
            //
        }
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public boolean wasRemoved() {
        return this.remove;
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = " " + suffix;
    }
}
