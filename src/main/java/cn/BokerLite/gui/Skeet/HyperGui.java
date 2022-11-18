//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.BokerLite.gui.Skeet;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


import cn.BokerLite.Client;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.components.Component;
import cn.BokerLite.gui.clickgui.components.SubWindow;
import cn.BokerLite.gui.configgui.implement.GuiInputBox;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.render.ColorUtils;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;


public class HyperGui extends GuiScreen {
    public static int mainx = 240;
    public static int mainy = 90;
    private static final ArrayList<SubWindow> components = new ArrayList<>();
    private static GuiInputBox searchBox;
    private static GuiInputBox chatBox;
    private int x2;
    private int y2;
    private boolean dragging;
    private final List<TypeScreen> types = new ArrayList();

    public HyperGui() {
        int x = 0;
        ModuleType[] var2 = ModuleType.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ModuleType category = var2[var4];
            this.types.add(new TypeScreen(category, x));
            x +=  28;
        }

    }

    public void initGui() {
        chatBox = new GuiInputBox(2, FontRenderer.F16, 0, height - 16, width, 16) {
        };
        chatBox.setPlaceholder("Chat or Command");
        chatBox.setMaxStringLength(256);
        chatBox.setCanLoseFocus(true);
        chatBox.setEnabled(true);
        super.initGui();
    }

    public String newcatename(ModuleType moduleCategory) {
        return moduleCategory.name();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        chatBox.drawTextBox();
        for (SubWindow component : components) {
            GlStateManager.pushMatrix();
            component.drawComponent(mouseX - component.getX(), mouseY - component.getY(), partialTicks);
            GlStateManager.popMatrix();
        }

        if (this.getSelectedTab() == null && !this.types.isEmpty()) {
            this.types.get(0).setSelected(true);
        }

        if (this.dragging) {
            mainx = this.x2 + mouseX;
            mainy = this.y2 + mouseY;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 120).getRGB());
    	Color baseColor = new Color(3,11,23, 240);

   		Color colorr = ColorUtils.interpolateColorC(baseColor, new Color(ColorUtils.applyOpacity(baseColor.getRGB(), .3f)), 0.5F);
   	 RenderUtil.drawRect((float) ((float) mainx-1.5), (float) mainy, mainx, mainy+ 350.0F,new Color(5,23,37, 255).getRGB());
   	 RenderUtil.drawRect((float) mainx-100, (float) mainy, (float) (mainx-1.5), mainy+ 350.0F,colorr.getRGB());
    //    RenderUtil.drawRect((float)this.mainx-80, (float)this.mainy, this.mainx, this.mainy+350.0F,  new Color(28, 28, 28, 180).getRGB());
        RenderUtil.drawRect((float) mainx, (float) mainy, mainx+350.0F, mainy+350.0F,  new Color(9,8,14).getRGB());
        RenderUtil.drawRect((float) mainx+2, (float) mainy+30, mainx+348.0F, mainy+31.0F,  new Color(5,23,37).getRGB());
        //RenderUtil.drawRect((float)(this.mainx + 348), (float)(this.mainy + 130), this.mainx+ 2.0F,this.mainy+ 20.0F,  new Color(28, 28, 28, 220).getRGB());
      //  RenderUtil.drawRect((float)this.mainx, (float)(this.mainy + 300), this.mainx+350.0F,this.mainy+ 20.0F,new Color(28, 28, 28, 220).getRGB());
        float var10002 = (float)(mainx + 3);
        FontLoaders.NL35.drawString("BOKERLITE", (float)(mainx -94 ), (float)(mainy + 15),  (new Color(24,114,165)).getRGB());
        FontLoaders.NL35.drawString("BOKERLITE", (float)(mainx -95 ), (float)(mainy + 15),  (new Color(255, 255, 255)).getRGB());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        FontLoaders.NL18.drawString("Users:", (float)(mainx -95 ), (float)(mainy + 332),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("Time:"+sdf.format(date), (float)(mainx -95 ), (float)(mainy + 342),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString(Client.username, (float)(mainx -65 ), (float)(mainy + 332),  new Color(76, 255, 32,255).getRGB());

    //    Client.INSTANCE.getFontManager().check22.drawString("X", var10002, (float)(this.mainy + 300 + 8), (new Color(Hud.colorValue.getValue())).getRGB());
        var10002 = (float)(mainx + 13);

        this.types.forEach((e) -> {
            e.draw(mouseX, mouseY);
        });
        FontLoaders.NL18.drawString("Combat", (float)(mainx -90 ), (float)(mainy + 42),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("Render", (float)(mainx -90 ), (float)(mainy + 70),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("Movement", (float)(mainx -90 ), (float)(mainy + 98),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("Player", (float)(mainx -90 ), (float)(mainy + 126),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL18.drawString("World", (float)(mainx -90 ), (float)(mainy + 154),  (new Color(130,140,150)).getRGB());
        FontLoaders.NL24.drawString("Rage", (float)(mainx -70 ), (float)(mainy + 55),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.icon24.drawString("a", (float)(mainx -90 ), (float)(mainy + 55),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("Overlay ", (float)(mainx -70 ), (float)(mainy + 83),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.icon24.drawString("c", (float)(mainx -90 ), (float)(mainy + 84),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("Main", (float)(mainx -70 ), (float)(mainy + 111),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.icon24.drawString("b", (float)(mainx -90 ), (float)(mainy + 111),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("Assist", (float)(mainx -70 ), (float)(mainy + 139),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.icon24.drawString("k", (float)(mainx -90 ), (float)(mainy + 139),  (new Color(28,133,192)).getRGB());
        FontLoaders.NL24.drawString("Visuals", (float)(mainx -70 ), (float)(mainy + 167),  (new Color(255, 255, 255)).getRGB());
        FontLoaders.icon24.drawString("e", (float)(mainx -90 ), (float)(mainy + 167),  (new Color(28,133,192)).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
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
        if (mouseButton == 0) {
            Iterator var4 = this.types.iterator();

            label35:
            while(true) {
                TypeScreen typeScreen;
                do {
                    if (!var4.hasNext()) {
                        break label35;
                    }

                    typeScreen = (TypeScreen)var4.next();
                } while(!typeScreen.isHovered(mouseX, mouseY));

                Iterator var6 = this.types.iterator();

                while(var6.hasNext()) {
                    TypeScreen other = (TypeScreen)var6.next();
                    other.setSelected(false);
                }

                typeScreen.setSelected(true);
            }
        }

        TypeScreen selectedTab = this.getSelectedTab();
        if (selectedTab != null) {
            selectedTab.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (RenderUtil.isHovering((float) mainx, (float) mainy, 500.0F, 35.0F, mouseX, mouseY)) {
            this.x2 = mainx - mouseX;
            this.y2 = mainy - mouseY;
            this.dragging = true;
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.types.forEach((e) -> {
            e.mouseReleased(mouseX, mouseY, state);
        });
        if (state == 0) {
            this.dragging = false;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.types.forEach((e) -> {
            e.keyTyped(typedChar, keyCode);
        });
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
        super.keyTyped(typedChar, keyCode);
    }

    public TypeScreen getSelectedTab() {
        return this.types.stream().filter(TypeScreen::isSelected).findAny().orElse(null);
    }
}
