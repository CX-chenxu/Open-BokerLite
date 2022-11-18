package cn.BokerLite.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.components.Button;
import cn.BokerLite.gui.clickgui.components.Component;
import cn.BokerLite.gui.clickgui.components.Label;
import cn.BokerLite.gui.clickgui.components.*;
import cn.BokerLite.gui.clickgui.windows.*;
import cn.BokerLite.gui.configgui.implement.GuiInputBox;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.utils.friend.FriendManager;
import cn.BokerLite.utils.js.JSManager;
import cn.BokerLite.utils.js.ScriptObject;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.mod.MessageUtils;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ClickGui extends GuiScreen {
    public static final TextGuiWindow tw = new TextGuiWindow();
    public static final TargetInfoWindow thw = new TargetInfoWindow();
    public static final InventoryWindow iw = new InventoryWindow();
    public static final SettingWindow sw = new SettingWindow();
    public static final RadarWindow rw = new RadarWindow();
    public static final FriendWindow fw = new FriendWindow();

    public static final ScriptWindow scw = new ScriptWindow();
    public static final ColorPicker cp = new ColorPicker(Client.colorTheme, sw);
    private static String history = "";
    private static final ArrayList<SubWindow> components = new ArrayList<>();
    private static GuiInputBox searchBox;

    private static GuiInputBox chatBox;
    public static GuiInputBox usingInpuBox;

    private static boolean filter = true;

    private static boolean chat = true;

    static {
        init();
    }


    public ClickGui() {
    }

    public static void drawCenteredStrings(String text, int x, int y, int color) {
        FontRenderer.F16.drawString(text, (x - FontRenderer.F16.getStringWidth(text) / 2.0f), y, color);
    }

    public static void init() {
        components.clear();
        MainWindow mw = new MainWindow();
        components.add(mw);
        components.add(tw);
        components.add(thw);
        components.add(iw);
        components.add(sw);
        components.add(rw);
        components.add(fw);

        components.add(scw);
        int x = 24;
        for (ModuleType type : ModuleType.values()) {
            ModuleWindow window = type.getWindow();
            window.setX(x);
            window.setY(22);
            x += window.getWidth() + 8;
            components.add(window);
            mw.addComponent(new Button(type.getName(), type.getChinese(), mw) {
                public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                    window.toggleHidden();
                }

                public boolean isEnabled() {
                    return !window.isHidden();
                }
            });
        }
        mw.addComponent(new Button("TextGui", "文字GUI", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                tw.toggleHidden();
            }

            public boolean isEnabled() {
                return !tw.isHidden();
            }
        });
        mw.addComponent(new Button("Target Info", "目标HUD", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                thw.toggleHidden();
            }

            public boolean isEnabled() {
                return !thw.isHidden();
            }
        });
        mw.addComponent(new Button("Inventory", "物品栏", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                iw.toggleHidden();
            }

            public boolean isEnabled() {
                return !iw.isHidden();
            }
        });
        mw.addComponent(new Button("Global", "全局设置", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                sw.toggleHidden();
            }

            public boolean isEnabled() {
                return !sw.isHidden();
            }
        });
        mw.addComponent(new Button("Radar", "雷达", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                rw.toggleHidden();
            }

            public boolean isEnabled() {
                return !rw.isHidden();
            }
        });
//        mw.addComponent(new Button("Profile", mw) {
//            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
//                mc.thePlayer.closeScreen();
//                GuiHelper.displayGuiScreen(new ConfigGui());
//            }
//        });
        mw.addComponent(new Button("Friend", "好友管理", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                fw.toggleHidden();
            }

            public boolean isEnabled() {
                return !fw.isHidden();
            }
        });
        mw.addComponent(new Button("Scripts", "脚本管理", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                scw.toggleHidden();
            }

            public boolean isEnabled() {
                return !scw.isHidden();
            }
        });
        mw.addComponent(new Button("Filter", "搜索器", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                filter = !filter;
            }

            public boolean isEnabled() {
                return filter;
            }
        });
        mw.addComponent(new Button("ChatGUI", "聊天GUI", mw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                chat = !chat;
            }

            public boolean isEnabled() {
                return chat;
            }
        });

        fw.components.clear();
        fw.addComponent(new InputBox(FriendManager.friendName, false, fw));
        fw.addComponent(new Button("Add", "添加", fw) {
            @Override
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                super.mouseClicked(mouseX, mouseY, mouseButton);
                if (!FriendManager.isFriend(FriendManager.friendName.getString())) {
                    if (FriendManager.friendName.getString().equals(""))
                        return;
                    FriendManager.addFriend(FriendManager.friendName.getString());
                    MessageUtils.send("FriendManager", FriendManager.friendName.getString() + " has beed added!", NotificationType.SUCCESS);
                    fw.addComponent(new Label(FriendManager.friendName.getString(), FriendManager.friendName.getString(), fw));
                    FriendManager.friendName.setString("");
                }
            }
        });
        for (String f : FriendManager.friendsList) {
            fw.addComponent(new Label(f, f, fw));
        }
        sw.addComponent(new Label("Gui Setting", "GUI设置", sw));
        sw.addComponent(new Label("Theme Color", "颜色", sw));
        sw.addComponent(cp);
        sw.addComponent(new ModuleButton(new ChineseModule(), sw));
        sw.addComponent(new InputBox(Client.watermark, false, sw));
        sw.addComponent(new InputBox(Client.tag, false, sw));
        sw.addComponent(new CheckBox(Client.font));
        sw.addComponent(new CheckBox(Client.bg));
        sw.addComponent(new CheckBox(Client.blur));
        sw.addComponent(new CheckBox(Client.thud));
        sw.addComponent(new CheckBox(Client.norenderer));
        sw.addComponent(new Label("Radar Setting", "雷达设置", sw));
        sw.addComponent(new CheckBox(Client.players));
        sw.addComponent(new CheckBox(Client.animals));
        sw.addComponent(new CheckBox(Client.mobs));
        sw.addComponent(new CheckBox(Client.item));
        sw.addComponent(new CheckBox(Client.invis));
        sw.addComponent(new Label("Client Setting", "客户端设置", sw));
        sw.addComponent(new CheckBox(Client.blatant));
        sw.addComponent(new CheckBox(Client.debug));
        sw.addComponent(new Button("Reload Scripts", "重载脚本", sw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                initScriptSystem();
            }
        });
        sw.addComponent(new Button("Uninject", "取消注入", sw) {
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                Client.uninject();
            }
        });
    }

    public static void initScriptSystem() {
        JSManager.loadScripts();
        scw.components.clear();
        if (JSManager.scripts.isEmpty()) {
            scw.addComponent(new Label("You are not loaded any Scripts.", "未加载脚本", scw));
        } else {
            if (!JSManager.scripts.isEmpty()) {
                scw.addComponent(new Label("JavaScript", "JavaScript脚本", scw));
                for (ScriptObject s : JSManager.scripts)
                    scw.addComponent(new Label(EnumChatFormatting.AQUA + s.name, EnumChatFormatting.AQUA + s.name, scw));
            }
        }
    }

    public static void drawCenteredString(String edit_alt, int i, int i1, int i2) {
        ClickGui.drawCenteredStrings(edit_alt, i, i1, i2);
    }

    public void initGui() {
        if (Client.blur.getValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        searchBox = new GuiInputBox(1, FontRenderer.F16, this.width / 2 - 100, 0, 200, 20) {
        };
        searchBox.setPlaceholder("Search Modules Within GUI...");
        searchBox.setMaxStringLength(200);
        searchBox.setCanLoseFocus(true);
        searchBox.setEnabled(true);
        searchBox.setText(history);

        chatBox = new GuiInputBox(2, FontRenderer.F16, 0, height - 16, width, 16) {
        };
        chatBox.setPlaceholder("Chat or Command");
        chatBox.setMaxStringLength(256);
        chatBox.setCanLoseFocus(true);
        chatBox.setEnabled(true);


//        pw.init();
        super.initGui();
    }

    @Override
    public void updateScreen() {
        if (searchBox != null) {
            searchBox.updateCursorCounter();
        }
    }

    public void onGuiClosed() {
        mc.entityRenderer.stopUseShader();
        history = searchBox.getText();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Client.bg.getValue())
            RenderUtil.drawRect(0, 0, width, height, new Color(0, 0, 0, 140).getRGB());
        // 使用RenderUtil画矩形，不使用画背景，防止crash
        if (filter)
            searchBox.drawTextBox();
        if (chat)
            chatBox.drawTextBox();

        for (SubWindow component : components) {
            GlStateManager.pushMatrix();
            component.drawComponent(mouseX - component.getX(), mouseY - component.getY(), partialTicks);
            GlStateManager.popMatrix();
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Component component : components) {
            component.keyTyped(typedChar, keyCode);
        }
        if (searchBox != null) {
            searchBox.textboxKeyTyped(typedChar, keyCode);
        }
        assert searchBox != null;
        if (searchBox.isFocused()) {
            for (ModuleType r : ModuleType.values()) {
                r.getWindow().components.clear();
            }
            for (Module m : ModuleManager.getModules()) {
                if (m.getName().toLowerCase().contains(searchBox.getText().toLowerCase())) {
                    ModuleWindow window = m.getModuleType().getWindow();
                    window.addComponent(new ModuleButton(m, window));
                    window.components.sort((o1, o2) -> {
                        if (!(o1 instanceof ModuleButton && o2 instanceof ModuleButton)) {
                            throw new IllegalArgumentException("!!!");
                        }
                        return ((ModuleButton) o1).getModule().getName().compareTo(((ModuleButton) o2).getModule().getName());
                    });
                }
            }
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
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
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
        searchBox.mouseClicked(mouseX, mouseY, mouseButton);
        chatBox.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (Component component : components) {
            component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (components.size() > 0) {
            components.get(components.size() - 1).handleMouseWheel(Mouse.getDWheel());
        }
    }
}
