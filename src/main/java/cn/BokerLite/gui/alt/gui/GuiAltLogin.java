package cn.BokerLite.gui.alt.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.alt.gui.component.GuiMantheButton;
import cn.BokerLite.gui.alt.gui.component.GuiMantheInputBox;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;

public class GuiAltLogin extends GuiScreen {
    private final GuiScreen previousScreen;
    private GuiTextField password;
    private AltLoginThread thread;
    private GuiTextField username;
    private GuiTextField combined;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                if (this.combined.getText().isEmpty()) {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                } else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                    String u = this.combined.getText().split(":")[0];
                    String p = this.combined.getText().split(":")[1];
                    this.thread = new AltLoginThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                } else {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                }
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(26, 26, 26).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.combined.drawTextBox();
        ClickGui.drawCenteredString("Alt Login", width / 2, 20, -1);
        ClickGui.drawCenteredString(
                this.thread == null ? "\u00a7eWaiting..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            Helper.mc.fontRendererObj.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            Helper.mc.fontRendererObj.drawStringWithShadow("Password", width / 2 - 96, 106.0f, -7829368);
        }
        if (this.combined.getText().isEmpty()) {
            Helper.mc.fontRendererObj.drawStringWithShadow("Email:Password", width / 2 - 96, 146.0f, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiMantheButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiMantheButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.username = new GuiMantheInputBox(1, FontRenderer.F16, width / 2 - 100, 60, 200, 20);
        this.password = new GuiMantheInputBox(2, FontRenderer.F16, width / 2 - 100, 100, 200, 20);
        this.combined = new GuiMantheInputBox(var3, FontRenderer.F16, width / 2 - 100, 140, 200, 20);
        this.username.setFocused(true);
        this.username.setMaxStringLength(200);
        this.password.setMaxStringLength(200);
        this.combined.setMaxStringLength(200);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t'
                && (this.username.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.combined.setFocused(!this.combined.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);


        this.combined.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
        this.combined.mouseClicked(x, y, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        this.combined.updateCursorCounter();
    }
}
