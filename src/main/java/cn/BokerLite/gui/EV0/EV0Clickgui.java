package cn.BokerLite.gui.EV0;

import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.Skeet.Animation;
import cn.BokerLite.gui.Skeet.Direction;
import cn.BokerLite.gui.clickgui.components.Component;
import cn.BokerLite.gui.clickgui.components.SubWindow;
import cn.BokerLite.gui.configgui.implement.GuiInputBox;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.render.RenderUtil;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Update to 06/01/2022
 * @author qingjiu
 * Font: SFBold,SF
 * ui by Ev0 (csgo)
 */
public class EV0Clickgui extends GuiScreen {
    public static int mainx = 230 ;
    public static int mainy = 140;

    public int x,iconx;

    private final boolean head= true;

    private final List<CategoryRender> tabs = new ArrayList<>();

    private int x2;
    private int y2;
    private boolean dragging;
    private boolean settings = false;
    private boolean Loader = true;
    private Animation openingAnimation;
    private static final ArrayList<SubWindow> components = new ArrayList<>();
    private static GuiInputBox searchBox;
    private static GuiInputBox chatBox;

    private final Animation targetlistani = new EaseInOutQuad(300, 400, Direction.FORWARDS) ;
    //Bot

    public FakeEntityPlayer fakeEntityPlayer;

    public EV0Clickgui(){
        for(ModuleType category : ModuleType.values()) {
            tabs.add(new CategoryRender(category,x));
            x += FontLoaders.NLLogo20.getStringWidth(newcatename(category)) + 17;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        chatBox = new GuiInputBox(2, FontRenderer.F16, 0, height - 16, width, 16) {
        };
        chatBox.setPlaceholder("Chat or Command");
        chatBox.setMaxStringLength(256);
        chatBox.setCanLoseFocus(true);
        chatBox.setEnabled(true);
        openingAnimation = new EaseBackIn(400, .4f, 2f);
     //   super.initGui();
    }

    public String newcatename(ModuleType moduleCategory){
        if (moduleCategory.name().equals("Combat")){
            return "RAGE";
        }else if (moduleCategory.name().equals("Player")){
            return "PLAYER";
        }else if (moduleCategory.name().equals("Movement")){
            return "MOVE";
        }else if (moduleCategory.name().equals("Render")){
            return "VISUALS";
        }else if (moduleCategory.name().equals("World")){
            return "WORLD";
        }else if (moduleCategory.name().equals("Misc")){
            return "MISC";
        }else if (moduleCategory.name().equals("Exploit")){
            return "EXPLOIT";
        }
        return "";
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        chatBox.drawTextBox();
        for (SubWindow component : components) {
            GlStateManager.pushMatrix();
            component.drawComponent(mouseX - component.getX(), mouseY - component.getY(), partialTicks);
            GlStateManager.popMatrix();
        }

        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
     //       mc.displayGuiScreen(new GuiHudDesigner());
     //   Fonts.SFBOLD.SFBOLD_35.SFBOLD_35.drawString("Click Me open gui",9,height - 41, ColorValue.WHITE.getRGB(),true);

        //设置初始页面
        if (getSelectedTab() == null){
            for (CategoryRender categoryRender : tabs){
                if (Loader){
                    categoryRender.setSelected(true);
                    Loader = false;
                }
            }
        }

        ScaledResolution sr = new ScaledResolution(mc);


        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 120).getRGB());

        //二次元
     //   RenderUtil.drawImage(new ResourceLocation("Miku/miku3.png"),600,0,696/2,1280/2);

        // drawScreen2(mouseX,mouseY,partialTicks);

        RenderUtil.scale(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, (float) openingAnimation.getOutput() + .6f, () -> drawScreen2(mouseX,mouseY,partialTicks));



        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public void drawScreen2(int mouseX, int mouseY, float partialTicks) {


        //移动面板
        if(dragging) {
            mainx = x2 + mouseX;
            mainy = y2 + mouseY;
        }

        //动画结束关闭窗口
        if (openingAnimation.isDone() && openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }

        //背景
        RenderUtil.drawRoundedRect(mainx+ 1,mainy - 1.5f, mainx+ 1+418, mainy - 1.5f+3,1, new Color(HUD.Hudcolor.getValue()).getRGB());
        RenderUtil.drawRect(mainx,mainy,mainx + 420,mainy+ 290,new Color(26,28,30).getRGB());

        //line 51,50,52
      //  RenderUtil.drawImage(new ResourceLocation("Miku/logo.png"),mainx + 8,mainy +4,18,20);
        RenderUtil.drawRect(mainx,mainy + 30,mainx + 420,mainy+ 31,new Color(51,50,52).getRGB());

        //绘制用户头像
        //获取头像
        /*
        if (head) {
            try {
                Minecraft.getMinecraft().getTextureManager().loadTexture(
                        new ResourceLocation("nb"),
                        new DynamicTexture(ImageIO.read(new URL("https://q.qlogo.cn/headimg_dl?dst_uin="+"3428128796"+"&spec=100"))));
                head =false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RenderUtil.arc(mainx + 404, mainy + 16 , 0, 360, 10.5f, 0xff636b6d);
        RenderUtil.drawCircleWithTexture(mainx + 404, mainy+ 16, 0, 360, 10,new ResourceLocation("nb"), -1);

         */


    if (targetlistani.isDone() && targetlistani.getDirection().equals(Direction.BACKWARDS) && targetlistani!= null){
        settings = false;
        }



        RenderUtil.drawRect(mainx+ 32,mainy + 3,mainx + 33,mainy+ 27,new Color(51,50,52).getRGB());

        RenderUtil.drawRect(mainx+ 104,mainy + 31,mainx + 121,mainy+ 290,new Color(51,50,52).getRGB());

        //覆盖颜色
        RenderUtil.drawRect(mainx+ 105,mainy + 31,mainx + 420,mainy+ 290,new Color(16,16,16).getRGB());

        tabs.forEach(e -> e.drawScreen(mouseX,mouseY));



        //绘制Config和Entityutils 的设置框

        //背景

    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //执行关闭窗口动画
        if (keyCode == 1) {
            openingAnimation.setDirection(Direction.BACKWARDS);
        }
        tabs.forEach(e -> e.keyTyped(typedChar,keyCode));
        for (Component component : components) {
            component.keyTyped(typedChar, keyCode);
        }
        if (chatBox != null) {
            chatBox.textboxKeyTyped(typedChar, keyCode);
        }
        assert chatBox != null;
        if (chatBox.isFocused()) {
            if (typedChar == '\r') {
                String msg = chatBox.getText();
                if (!Objects.equals(msg, "")) {
                    if (msg.startsWith(".")) {
                        if (msg.length() > 1) {
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
                    } else {
                        mc.thePlayer.sendChatMessage(chatBox.getText());
                    }
                }
                chatBox.setText("");
            } else if (typedChar == '\t') {
                String msg = chatBox.getText();
                if (!Objects.equals(msg, "")) {
                    if (msg.startsWith(".")) {
                        String raw = msg.substring(1);
                        if (!raw.contains(" ")) {
                            StringBuilder ac = new StringBuilder();
                            for (Command c : CommandManager.getCommands()) {
                                ac.append(".").append(c.getName()).append(", ");
                            }
                            Helper.sendMessage(ac.toString());
                        } else {
                            String[] splited = raw.split(" ");
                            for (Command c : CommandManager.getCommands()) {
                                if (Objects.equals(c.getName(), splited[0])) {
                                    switch (CommandManager.getCommand(splited[0]).get().getAC()) {
                                        case Module: {
                                            StringBuilder ac = new StringBuilder();
                                            for (Module m : ModuleManager.getModules()) {
                                                ac.append(".").append(m.getName()).append(", ");
                                            }
                                            Helper.sendMessage(ac.toString());
                                        }

                                        case Player: {
                                            StringBuilder ac = new StringBuilder();
                                            for (NetworkPlayerInfo npi : mc.getNetHandler().getPlayerInfoMap()) {
                                                ac.append(".").append(npi.getGameProfile().getName()).append(", ");
                                            }
                                            Helper.sendMessage(ac.toString());
                                        }
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
      //  super.keyTyped(typedChar,keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (int i = components.size() - 1; i >= 0; i--) {
            SubWindow window = components.get(i);
            if (mouseX > window.getX() && mouseX < window.getX() + window.getWidth() &&
                    mouseY > window.getY() && mouseY < window.getY() + window.getHeight()) {
                window.mouseClicked(mouseX, mouseY, mouseButton);
                components.add(window);
                components.remove(i--);
                break;
            }
        }

        chatBox.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0){

            //选择面板
            for (CategoryRender categoryRender : tabs){
                if(categoryRender.isHovered(mouseX, mouseY)) {
                    for(CategoryRender other : tabs) {
                        //判断是否是当前已经所选
                        other.setSelected(false);
                    }
                    categoryRender.setSelected(true);
                }
            }
            //移动面板
            if(isHovered(mouseX, mouseY)) {
                this.x2 = mainx - mouseX;
                this.y2 = mainy - mouseY;
                this.dragging = true;
            }
            /*
            if (settings) {
                if (DrRenderUtil.isHovering(mainx + 428, mainy + 35, 75, 90, mouseX, mouseY)) {
                    EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                }
                if (DrRenderUtil.isHovering(mainx + 427 + 83, mainy + 35, 75, 90, mouseX, mouseY)) {
                    EntityUtils.targetMobs = !EntityUtils.targetMobs;
                }
                if (DrRenderUtil.isHovering(mainx + 428, mainy + 133, 75, 90, mouseX, mouseY)) {
                    EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                }
                if (DrRenderUtil.isHovering(mainx + 427 + 83, mainy + 133, 75, 90, mouseX, mouseY)) {
                    EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                }
            }

             */

        }
        /*
        if (mouseButton == 1 || mouseButton == 0){
            if (RenderUtil.isHovering(mainx , mainy,32,27,mouseX,mouseY)){
                if (settings){
                    targetlistani.setDirection(Direction.BACKWARDS);
                }
                if (!settings) {
                    settings = true;
                    targetlistani.setDirection(Direction.FORWARDS);
                }
              //  settings = !settings;
            }
        }

         */

        //判断是否是已经选择的
        CategoryRender selectedTab = getSelectedTab();
        if(selectedTab != null) selectedTab.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) {
            this.dragging = false;



        }
        tabs.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
    }
    public
    CategoryRender getSelectedTab() {
        return tabs.stream().filter(CategoryRender::isSelected).findAny().orElse(null);
    }


    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=mainx && mouseX <= mainx+ 420 && mouseY >= mainy && mouseY <= mainy + 31;
    }

}
