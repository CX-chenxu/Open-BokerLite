package cn.BokerLite.modules;

import cn.BokerLite.gui.clickgui.components.ModuleButton;
import cn.BokerLite.gui.clickgui.components.ModuleWindow;
import cn.BokerLite.modules.Player.disabler.HypixelTimer;
import cn.BokerLite.modules.move.*;
import cn.BokerLite.modules.combat.*;
import cn.BokerLite.modules.render.*;
import cn.BokerLite.modules.Player.*;
import cn.BokerLite.modules.Player.disabler.MAC;
import cn.BokerLite.modules.Player.disabler.NCP;

import cn.BokerLite.modules.value.*;
import cn.BokerLite.modules.world.SafeWalk;
import cn.BokerLite.modules.world.*;
import cn.BokerLite.utils.FileManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    static ArrayList<Module> modules = new ArrayList<>();
    static ArrayList<Module> jsModules = new ArrayList<>();

    public static ArrayList<Module> getModules() {
        return modules;
    }
    private boolean enabledNeededMod = true;

    public ModuleManager() {

    }

    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }

    public static Module getModule(Class<?> name) {
        for (Module m : modules) {
            if (m.getClass() == name)
                return m;
        }
        return null;
    }
    
    static {
        registerModule(new AimAssist());
        registerModule(new AntiBot());

        registerModule(new AutoClicker());

        registerModule(new Reach());

        registerModule(new Sprint());
        registerModule(new LegitAura());
        registerModule(new Teams());
        registerModule(new Velocity());
        registerModule(new WTap());



        registerModule(new AntiVoid());

        registerModule(new Blink());

        registerModule(new Criticals());
        registerModule(new Fly());
        registerModule(new HitBox());
        registerModule(new InvMove());
        registerModule(new ClickGui());
        registerModule(new KeepSprint());
        registerModule(new LiquidWalk());

        registerModule(new NoFall());
        registerModule(new NoSlowDown());
        registerModule(new LongJump());
        registerModule(new Regen());
        registerModule(new Speed());
        registerModule(new Strafe());
        registerModule(new LegitScaffold());

        registerModule(new Step());

        registerModule(new TargetStrafe());

        registerModule(new TPAura());

        registerModule(new RangeDisplay());
        registerModule(new BedESP());
        registerModule(new BlockOverlay());
        registerModule(new Chams());
        //registerModule(new ClickGui());
        registerModule(new DMGParticle());
        registerModule(new ESP());
        registerModule(new Freecam());
        registerModule(new LegitSpeed());
        registerModule(new FullBright());
        registerModule(new Health());
        registerModule(new HUD());
        registerModule(new ItemESP());

        registerModule(new NameTags());
        registerModule(new Notification());
        registerModule(new Projectiles());
        registerModule(new StorageESP());

        registerModule(new Tracers());
        registerModule(new Xray());
        //registerModule(new BigGod());
        registerModule(new MAC());
        registerModule(new Scaffold());
        registerModule(new AutoArmor());
        registerModule(new AntiAFK());
        registerModule(new AutoMLG());

        registerModule(new AutoTool());
        registerModule(new ChestStealer());
        registerModule(new FastPlace());
        registerModule(new Fucker());
        registerModule(new InvCleaner());
        registerModule(new MCF());
        registerModule(new MurderMystery());
        registerModule(new Panic());
        registerModule(new SpeedMine());
        registerModule(new Timer());
        registerModule(new AutoGG());
        registerModule(new NCP());
        registerModule(new HypixelTimer());
        registerModule(new Nuker());

        registerModule(new NameProtect());
        registerModule(new AutoPlay());
        registerModule(new AutoGapple());
        registerModule(new SafeWalk());

    }

    @SuppressWarnings("rawtypes")
	public static void registerModule(Module module) {
        for (final Field field : module.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object obj = field.get(module);
                if (obj instanceof Value) module.getValues().add((Value) obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        modules.add(module);
        ModuleWindow window = module.getModuleType().getWindow();
        window.addComponent(new ModuleButton(module, window));
        window.components.sort((o1, o2) -> {
            if (!(o1 instanceof ModuleButton && o2 instanceof ModuleButton)) {
                throw new IllegalArgumentException("!!!");
            }
            return ((ModuleButton) o1).getModule().getName().compareTo(((ModuleButton) o2).getModule().getName());
        });
    }
    public static List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getModuleType() != t) continue;
            output.add(m);
        }
        return output;
    }

    @SuppressWarnings("rawtypes")
    public static void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModule(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex(bind.toUpperCase()));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModule(v);
            if (m == null) continue;
            m.setState(true);
        }

        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModule(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }

                if (value instanceof Mode) {
                    ((Mode<?>)value).setMode(v.split(":")[2]);
                }

            }
        }
    }
    public static void applyConfig(String config) {
        String name = config.split(":")[0];
        String values = config.split(":")[1];
        Module m = ModuleManager.getModule(name);
        if (m == null) return;
        for (Value value : m.getValues()) {
            if (!value.getName().equalsIgnoreCase(values)) continue;
            if (value instanceof Option) {
                value.setValue(Boolean.parseBoolean(config.split(":")[2]));
                continue;
            }
            if (value instanceof Numbers) {
                value.setValue(Double.parseDouble(config.split(":")[2]));
                continue;
            }
            ((Mode) value).setMode(config.split(":")[2]);
        }
    }
    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setState(true);
            }
        }

    }

    public static ArrayList<Module> getJSModules() {
        return jsModules;
    }

    public static void unload(Module m) {
        modules.remove(m);
    }
}
