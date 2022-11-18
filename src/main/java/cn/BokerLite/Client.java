package cn.BokerLite;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.api.enums.Type;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.ForgeEventManager;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandBind2;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.command.commands.AntiDump;
import cn.BokerLite.gui.BokerNot.NotificationsManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.NLNot.Notifications;
import cn.BokerLite.gui.alt.gui.AltManager;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.gui.clickgui.components.ColorPicker;
import cn.BokerLite.gui.keybindgui.KeyBindManager;
import cn.BokerLite.gui.notification.NotificationPublisher;

import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.render.Health;
import cn.BokerLite.modules.render.InventoryHUD;
import cn.BokerLite.modules.render.Notification;
import cn.BokerLite.modules.value.*;

import cn.BokerLite.utils.Addon;
import cn.BokerLite.utils.FileManager;
import cn.BokerLite.utils.HUD.AnimationUtils;
import cn.BokerLite.utils.fontRenderer.CFont.CFontRenderer;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.mod.MessageUtils;
import cn.BokerLite.utils.packet.PacketManager;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


public class Client {
    public static int THEME_RGB_COLOR = new Color(36, 240, 0).getRGB();
    public  static String CLIENT_VERSION = "102922";


    public final static Type CLIENT_TYPE = Type.Beta;



    public static Client INSTANCE;


    public static String username = "李晨阳他爹";
    public  static Minecraft mc = Minecraft.getMinecraft();
    public static boolean state = false;
    public static boolean customFontSupport = false;
    public double textX, textY;
    public int textHeight;
    public boolean textHidden;
    public static boolean chinese = false;
    public static final KeyBindManager keyBindManager = new KeyBindManager();
    public String sessionToken; // TODO: 使用注入器登录并且把token传过来

    public static Colors colorTheme = new Colors("Theme", new Color(0x00A6FF));
    public static Labels<String> watermark = new Labels<>("Watermark", "BokerLite");
    public static Labels<String> tag = new Labels<>("Tag", "李晨阳");
    public static final Option<Boolean> font = new Option<>("Custom Font", "自定义字体", "Custom Font", true);
    public static final Option<Boolean> thud = new Option<>("Show Target", "显示目标", "Show Target", false);
    public static final Option<Boolean> norenderer = new Option<>("No Renderer", "无视觉", "No Renderer", false);
    public static final Option<Boolean> players = new Option<>("Players", "玩家", "Players", true);
    public static final Option<Boolean> animals = new Option<>("Animals", "动物", "Animals", true);
    public static final Option<Boolean> mobs = new Option<>("Mobs", "生物", "Mobs", true);
    public static final Option<Boolean> item = new Option<>("Items", "物品", "Items", false);
    public static final Option<Boolean> invis = new Option<>("Invisibles", "隐身实体", "Invisibles", false);
    public static Option<Boolean> blur = new Option<>("Blur", "模糊背景", "Blur", false);
    public static Option<Boolean> blatant = new Option<>("Blatant Mode", "暴力模式", "Blatant", false);
    public static Option<Boolean> debug = new Option<>("Debug", "调试", "Debug", false);
    public static Option<Boolean> bg = new Option<>("Background", "ClickGUI背景", "Background", true);

    public float hue = 0;
    public static ModuleManager modulemanager;

    public Boolean stateP = false;

    public Client()  {
        native0();
    }

    public void native0(){
        this.sessionToken = sessionToken;
        if (state) return;
        state = true;
        initManager();
        PacketManager.INSTANCE.init();
        AltManager.init();
        FileManager.init();
        FileManager.dir = new File(mc.mcDataDir, "BokerLite");
        FileManager.dir.mkdir();
        if (!Client.nullCheck()) {
            PacketManager.INSTANCE.onJoinServer(null);
        }
        INSTANCE = this;
        MessageUtils.send("Inject Succeeded", "Press RSHIFT to open clickgui.", NotificationType.SUCCESS);
        ForgeEventManager.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandBind2(Client.INSTANCE));
        Display.setTitle("BokerLite Creaked By 李晨阳他爹");
    }

    public static void RenderRotate(float yaw) {
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        new Client();
    }
    public static ModuleManager getModuleManager() {
        return modulemanager;
    }
    public void shutDown() {
        System.out.println("OK");
        System.out.println(mc.mcDataDir);
        String values = "";
        new ModuleManager();
        for (Module m : ModuleManager.getModules()) {

            for (Value v : m.getValues()) {


                if (v instanceof ColorValue) {
                }else {
                    values = values + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
                }

            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        new ModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.getState()) continue;
            enabled = enabled + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
        String Hiddens = "";
        for (Module m : ModuleManager.getModules()) {
            if(m.wasRemoved())Hiddens = Hiddens + m.getName() + System.lineSeparator();
        }
        FileManager.save("Hidden.txt", Hiddens, false);
    }


    private static int getModuleNameWidth(Module module) {
        return mc.fontRendererObj.getStringWidth(module.getName());
    }

    public boolean isMargeleAntiCheatDetected() {
        try {
            Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer sb = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void uninject() {

        mc.thePlayer.closeScreen();
        ArrayList<Module> modules = new ArrayList<>(ModuleManager.getModules());
        for (Module m : modules) {
            if (m != null) {
                m.setState(false);
            }
        }
        Client.state = false;
        if (Client.INSTANCE != null) {
            MinecraftForge.EVENT_BUS.unregister(Client.INSTANCE);
            PacketManager.INSTANCE.uninject();
            Client.INSTANCE = null;
        }
        System.gc();
    }


    @SubscribeEvent
    public void clickGuiColor(TickEvent.ClientTickEvent e) {
    	ColorPicker.update();
    	THEME_RGB_COLOR = ClickGui.cp.getColor();
}
    @SubscribeEvent
    public void packet(TickEvent.ClientTickEvent e) {
        if(!INSTANCE.stateP) {

            System.out.println("OK");
            INSTANCE.stateP=true;
        }



    }

    @SubscribeEvent
    public void onNoRender(RenderGameOverlayEvent event) {
        if (!norenderer.getValue())
            return;
        if (!(mc.currentScreen == null))
            return;
        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
            event.setCanceled(true);
    }


    public static int rainbowDraw(long speed, long delay) {
        long time = System.currentTimeMillis() + delay;
        return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0F / (float) speed), 1.0F, 1.0F).getRGB();
    }

    public static Timer getTimer() {
        return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
    }

    private void initManager() {
        PacketManager.INSTANCE.init();
    }

    @EventHandler
    public void onCommand(EventPacketRecieve e) {
        Packet<?> p = e.getPacket();
        if (p instanceof C01PacketChatMessage) {
            String msg = ((C01PacketChatMessage) p).getMessage();
            if (msg.startsWith(".")) {
                if (msg.length() > 1) {
                    e.setCancelled(true);
                    String[] args = msg.trim().substring(1).split(" ");
                    Optional<Command> possibleCmd = CommandManager.getCommand(args[0]);
                    if (possibleCmd.isPresent()) {
                        String result = possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
                        if (result != null && !result.isEmpty()) {
                            Helper.sendMessage(result);
                        }
                    } else {
                        Helper.sendMessage("Command not found Try .help to find help.");
                    }
                }
            }
        }
    }

    public static boolean nullCheck() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer == null || mc.theWorld == null;
    }

    public int invX;
    public int invY = 20;

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
//        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
//            GuiHelper.displayGuiScreen(new today.getvapu.gui.clickgui.ClickGui());
//            return;
//        }
//        if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
//            GuiHelper.displayGuiScreen(new ConfigGui());
//            return;
//        } 迁移至modules render类
        for (Module m : ModuleManager.getModules()) {
            if (Keyboard.isKeyDown(m.key)) {
                if (m.key == Keyboard.KEY_NONE)
                    return;
                m.toggle();
            }
        }
    }

    private int width;
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (Client.nullCheck())
            return;
        Notifications not = new Notifications();
        not.drawNotifications();

        if (ModuleManager.getModule(Notification.class).getState()) {
            NotificationPublisher.publish(new ScaledResolution(mc));
        }
        NotificationsManager.renderNotifications();
        NotificationsManager.update();
        if (ModuleManager.getModule(Health.class).getState()) {
            if (mc.thePlayer.getHealth() >= 0.0f && mc.thePlayer.getHealth() < 10.0f) {
                this.width = 3;
            }
            if (mc.thePlayer.getHealth() >= 10.0f && mc.thePlayer.getHealth() < 100.0f) {
                this.width = 5;
            }
            FontRenderer.F16.drawStringWithShadow(String.valueOf((int) mc.thePlayer.getHealth()), (new ScaledResolution(mc).getScaledWidth() / 2 - this.width), (int) ((new ScaledResolution(mc).getScaledHeight() / 2 - 5) - (float) 5.0D - (float) 7.0), -1);
        }

        if (!mc.gameSettings.showDebugInfo && ModuleManager.getModule("HUD").getState()) {

            if (HUD.waterMark.getValue()) {
                if(HUD.inv.getValue()){
                    InventoryHUD.drawHUD(HUD.x.getValue().intValue(),HUD.y.getValue().intValue());
                }

                ScaledResolution sr = new ScaledResolution(mc);
                String xd = "\u00a77Build - \u00a7f" + CLIENT_VERSION + " \u00a77 - User -" +  "\u00a7a " + Client.username;
                String xd1 = "\u00a77Level - " + EnumChatFormatting.DARK_GREEN  + "User";
                FontRenderer.F22.drawStringWithShadow(xd1, (int) (sr.getScaledWidth() - FontRenderer.F22.getStringWidth(xd1)+ 10.0F), (sr.getScaledHeight() - 24), Colors.getColor(255, 220));
                FontRenderer.F22.drawStringWithShadow(xd, (int) (sr.getScaledWidth() - FontRenderer.F22.getStringWidth(xd) + 20.0F), (sr.getScaledHeight() - 13), Colors.getColor(255, 220));
                if (HUD.mode.getValue() == HUD.WaterMark.Boker) {
                    DateFormat dft = new SimpleDateFormat("hh:mma");
                    Date time = Calendar.getInstance().getTime();
                    String rendertime = dft.format(time);
                    //	RenderUtil.drawBlurRect(1.0F, 3.0F, FontRenderer.F18.getStringWidth("Boker") + 95.0f, 12.0F, new Color(26, 25, 25, 150).getRGB());
                    FontRenderer.F18.drawString("BokerLite", 2.0f, 6.0f,  new Color(232, 44, 44, 255).getRGB());
                    FontRenderer.F18.drawString("BY-李晨阳", 48.0f, 6.0f,  new Color(23, 234, 234, 255).getRGB());
                    RenderUtil.drawRect(1.0F, 15.0F,  75.0f, 45.0F, new Color(26, 25, 25, 120).getRGB());
                    String health = "" + (int)mc.thePlayer.getHealth();
                    float health1 = mc.thePlayer.getHealth();
                    if (health1 > 20.0f) {
                        health1 = 20.0f;
                    }
                    int red = (int)Math.abs(health1 * 5.0f * 0.01f * 0.0f + (1.0f - health1 * 5.0f * 0.01f) * 255.0f);
                    int green = (int)Math.abs(health1 * 5.0f * 0.01f * 255.0f + (1.0f - health1 * 5.0f * 0.01f) * 0.0f);
                    Color customColor = new Color(red, green, 0).brighter();
                    FontRenderer.F16.drawString("X:"+ (int)mc.thePlayer.posX, 2.5f, 19.0f,  new Color(255, 255, 255,255).getRGB());
                    FontRenderer.F16.drawString("Y:"+ (int)mc.thePlayer.posY, 2.5f, 28.0f,  new Color(255, 255, 255,255).getRGB());
                    FontRenderer.F16.drawString("Z:"+ (int)mc.thePlayer.posZ, 2.5f, 36.0f,  new Color(255, 255, 255,255).getRGB());
                    FontRenderer.F16.drawString(health, 48f,  37.0f, customColor.getRGB());
                    FontRenderer.F16.drawString(":", 44f,  36.0f,new Color(255, 255, 255,255).getRGB());
                    FontRenderer.F16.drawString("H", 35,37,  -1);

                    FontRenderer.F16.drawString(rendertime, 35f, 19.0f,  new Color(255, 255, 255,255).getRGB());
                    FontRenderer.F16.drawString("User", 35f, 28.0f,  new Color(76, 255, 32,255).getRGB());
                } else if (HUD.mode.getValue() == HUD.WaterMark.Onetap) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String text = String.format(" | %s | %sFPS | Users | %s",  Client.username, Minecraft.getDebugFPS(), sdf.format(date));
                    RenderUtil.drawRect(2, 2, FontRenderer.F18.getStringWidth("BokerLite"+text) + 6, FontRenderer.F18.getFontHeight() + 4, new Color(0, 0, 0, 144).getRGB());
                    RenderUtil.drawRect(2, 1, 2 + FontRenderer.F18.getStringWidth("BokerLite"+text) + 4, 2,  new Color(HUD.Icons.getValue()).getRGB());
                  //  RenderUtil.drawShadow(2, 2, FontLoaders.F18.getStringWidth(text) + 2, FontLoaders.F18.getHeight() + 2);
                    FontRenderer.F18.drawString(text, FontRenderer.F18.getStringWidth("BokerLite")+4, 4, -1);
                    FontRenderer.F18.drawString("okerLite", 10, 4, -1);
                    FontLoaders.NLLogo20.drawStringWithShadow("B",4,5,new Color(HUD.Icons.getValue()).getRGB());

                }else if (HUD.mode.getValue() == HUD.WaterMark.NeverLose){
                    CFontRenderer fontLogo = FontLoaders.NL18;
                    CFontRenderer array = FontLoaders.NLLogo22;
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String text = String.format("%s  %sfps  Users  %s", "Roxy", Minecraft.getDebugFPS(), sdf.format(date));
                    RenderUtil.drawRoundedRect(2, 2,  fontLogo.getStringWidth("BokerLite       "+text)+2,array.getHeight() + 5,2, new Color(9,8,14, 255).getRGB());
                    array.drawString("BokerLite", 5, 4, (new Color(24,114,165)).getRGB());
                    array.drawString("BokerLite", 4, 4, -1);
                    fontLogo.drawString(text,fontLogo.getStringWidth("BokerLite      ")+4,6,-1);
                }
            }
        }

        if (HUD.potion.getValue())
            drawPotionStatus(new ScaledResolution(mc));


        if (!HUD.arrayList.getValue())
            return;
        if (ClickGui.tw.isHidden())
            return;
        ArrayList<Module> sorted = new ArrayList<>();
        for (Module m : ModuleManager.getModules()) {
            if ((!m.getState()  && m.getAnimx() == 0) || m.wasRemoved())
                continue;
            sorted.add(m);
        }
   //     sorted.sort((o1, o2) -> FontRenderer.F18.getStringWidth(o2.getName() + (o2.getSuffix() != "" ? " " : "") + o2.getSuffix()) - FontRenderer.F18.getStringWidth(o1.getName() + (o1.getSuffix() != "" ? " " : "") + o1.getSuffix()));
        sorted.sort((o1,
                     o2) -> FontRenderer.F18
                .getStringWidth(o2.getSuffix().isEmpty() ? o2.getName()
                        : String.format("%s %s", o2.getName(), o2.getSuffix()))
                - FontRenderer.F18.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName()
                : String.format("%s %s", o1.getName(), o1.getSuffix())));
        int y = 1;
        int rainbowTick = 0;
        int max = RenderUtil.width();
        int[] counter = new int[1];
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (HUD.arrayList.getValue()) {
            for (Module m : sorted) {
                int countMod = 0;
                String newstr = chinese ? m.chinese : m.getName() + "" + EnumChatFormatting.GRAY + m.getSuffix();
                int nextIndex = sorted.indexOf(m) + 1;
                Module nextModule = null;
                if (sorted.size() > nextIndex) {
                    nextModule = this.getNextEnabledModule(sorted, nextIndex);
                }
                if (m.getState()) {
                    m.setArrayRemoved(false);
                    if (mc.thePlayer.ticksExisted >= 30) {
                        m.setAnimy(AnimationUtils.animation(12, 0, m.getAnimy(), 2 * (200d / Math.max(Minecraft.getDebugFPS(), 1)), AnimationUtils.AnimationTypes.slowDown));
                        m.setAnimx(AnimationUtils.animation(0, FontRenderer.F18.getStringWidth(newstr), m.getAnimx(), 10 * (200d / Math.max(Minecraft.getDebugFPS(), 1)), AnimationUtils.AnimationTypes.slowDown));
                    } else {
                        m.setAnimx(FontRenderer.F18.getStringWidth(newstr));
                    }
                } else {
                    if (m.getAnimx() <= 0) {
                        m.setRemoved(true);
                    } else {
                        if (mc.thePlayer.ticksExisted >= 30) {
                            m.setAnimy(AnimationUtils.animation(0, 12, m.getAnimy(), 2 * (200d / Math.max(Minecraft.getDebugFPS(), 1)), AnimationUtils.AnimationTypes.slowDown));
                            m.setAnimx(AnimationUtils.animation(FontRenderer.F18.getStringWidth(newstr), 0, m.getAnimx(), 10 * (200d / Math.max(Minecraft.getDebugFPS(), 1)), AnimationUtils.AnimationTypes.slowDown));
                        } else {
                            m.setAnimx(0);
                        }
                    }
                }
                countMod = countMod + 1;
                int rainbowCol = rainbow(System.nanoTime() * 5L, (float) counter[0], 1.0F).getRGB();

                Color col = new Color(rainbowCol);

                final int rainbowCol1 = (new Color(0, col.getGreen() / 3 + 40, col.getGreen() / 2 + 100)).getRGB();
                final int c = rainbowCol1;

                final Color col1 = new Color(c);

                //Rect
                int Ranbow = (new Color(0, col.getGreen() / 3 + 40, col.getGreen() / 2 + 100)).getRGB();

                double x = RenderUtil.width() - m.getAnimx();

                Color rainbow = HUD.rainbow.getValue() ? new Color(Color.HSBtoRGB((float) ((double) mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f)) : new Color(THEME_RGB_COLOR);
                if(!HUD.TextRect.getValue()) {
                    if(!HUD.TextShdow.getValue()) {
                        FontRenderer.F18.drawString(newstr, (float) (x + 2)
                                , y + 1, new Color(HUD.Text.getValue()).getRGB());
                    }else {
                        FontRenderer.F18.drawStringWithShadow(newstr, (int) (x + 2)
                                , y + 1, new Color(HUD.Text.getValue()).getRGB());
                    }
                }else {
                //    Gui.drawRect(scaledResolution.getScaledWidth() - 1, y - 2, scaledResolution.getScaledWidth(), y + 9, (Boolean) this.arrayrainbow.getValueState() ? RenderUtil.rainbow(countMod * rainbow) : Ranbow);

                    if (m.getSuffix() != "") {
                        Gui.drawRect((int) (x - 2),y, scaledResolution.getScaledWidth() , (int) (y + 12.5), (new Color(0, 0, 0, 120)).getRGB());
                    } else {
                        Gui.drawRect((int) (x - 2),y, scaledResolution.getScaledWidth(), (int) (y + 12.5), (new Color(0, 0, 0, 120)).getRGB());
                    }
                    if(!HUD.TextShdow.getValue()) {
                        FontRenderer.F18.drawString(newstr, (float) (x + 2)
                                , y + 1, new Color(HUD.Text.getValue()).getRGB());
                    }else {
                        FontRenderer.F18.drawStringWithShadow(newstr, (int) (x + 2)
                                , y + 1, new Color(HUD.Text.getValue()).getRGB());
                    }

                }

                if (++rainbowTick > 50) {
                    rainbowTick = 0;
                }
                counter[0]++;
                if(!HUD.TextRect.getValue()) {
                    y += 10 - m.getAnimy();
                }else {
                    y += 12 - m.getAnimy();
                }

            }
        }


    }
    private Module getNextEnabledModule(List<Module> modules, int startingIndex) {
        int modulesSize = modules.size();
        for (int i = startingIndex; i < modulesSize; ++i) {
            Module module = modules.get(i);
            if (!module.getState())
                continue;
            return module;
        }
        return null;
    }
    public int getWidth() {
        return ModuleManager.getModules().stream().mapToInt(Client::getModuleNameWidth).max().getAsInt();
    }
    @SubscribeEvent
    public void onRenderInv(RenderGameOverlayEvent.Text e){
        if(!ClickGui.iw.isHidden()) {
            if (mc.currentScreen instanceof ClickGui)
                return;
            GL11.glPushMatrix();
            RenderUtil.drawBordered(Client.INSTANCE.invX, Client.INSTANCE.invY, (20 * 9) + 2, (20 * 3) + 2, 1, new Color(0, 0, 0, 140).getRGB(), Client.THEME_RGB_COLOR);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < 27; i++) {
                ItemStack[] itemStack = mc.thePlayer.inventory.mainInventory;
                int offsetX = invX + 2 + (i % 9) * 20;
                int offsetY = invY + (i / 9) * 20;
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack[i + 9], offsetX, offsetY);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack[i + 9], offsetX, offsetY, null);
            }
            GL11.glPopMatrix();
        }
    }
    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0E-9F + count) * 4.0E8F) / 1.75000003E10F * 3.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 0.5F, 1.0F))), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
    }


    private void drawPotionStatus(ScaledResolution sr) {
        int y = -25;
        for (PotionEffect effect : Module.mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName());
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = PType + " II";
                    break;
                }
                case 2: {
                    PType = PType + " III";
                    break;
                }
                case 3: {
                    PType = PType + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = PType + "§7:§6 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "§7:§c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + "§7:§7 " + Potion.getDurationString(effect);
            }
            int ychat = Module.mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
            FontRenderer.F16.drawString(PType, sr.getScaledWidth() - FontRenderer.F16.getStringWidth(PType) - 2, sr.getScaledHeight() - FontRenderer.F16.getFontHeight() + y - 12 - ychat, potion.getLiquidColor());
            y -= 10;
        }
    }


    private static final int SIZE = 128;
    private static final int BGCOLOR = 0xAA<<24;
    public static int radarX = 16;
    public static int radarY = 16;


    private Entity[] getAllMatchedEntity() {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        Entity player = mc.thePlayer;
        if(world != null) {
            ArrayList<Entity> entities = new ArrayList<>(world.loadedEntityList.size());
            double max = (SIZE * SIZE) * 2;
            for (Entity entity : world.loadedEntityList) {
                if(entity == player)
                    continue;
                double dx = player.posX - entity.posX,
                       dz = player.posZ - entity.posZ;
                double distance = dx * dx + dz * dz;
                if(distance > max)
                    continue;
                if(entity instanceof EntityPlayer && !players.getValue())
                    continue;
                if(entity instanceof EntityMob && !mobs.getValue())
                    continue;
                if(entity instanceof EntityAnimal && !animals.getValue())
                    continue;
                if(entity instanceof EntityItem && !item.getValue())
                    continue;
                if(entity.isInvisible() && !invis.getValue())
                    continue;
                entities.add(entity);
            }
            return entities.toArray(new Entity[0]);
        }
        return new Entity[0];
    }

    public static float tx = 1F;
    public static float ty = 1F;


}
