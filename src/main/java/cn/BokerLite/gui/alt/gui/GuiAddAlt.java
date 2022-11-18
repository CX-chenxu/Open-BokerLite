package cn.BokerLite.gui.alt.gui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.alt.gui.component.GuiMantheButton;
import cn.BokerLite.gui.alt.gui.component.GuiMantheInputBox;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.utils.FileManager;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.net.Proxy;

public class GuiAddAlt extends GuiScreen {
    private final GuiAltManager manager;
    private GuiMantheInputBox password;
    private String status = "\u00a7eWaiting...";
    private GuiMantheInputBox username;
    private GuiMantheInputBox combined;

    public GuiAddAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    static void access$0(GuiAddAlt guiAddAlt, String string) {
        guiAddAlt.status = string;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                AddAltThread login;
                if (this.combined.getText().isEmpty()) {
                    login = new AddAltThread(this.username.getText(), this.password.getText());
                } else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                    String u = this.combined.getText().split(":")[0];
                    String p = this.combined.getText().split(":")[1];
                    login = new AddAltThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                } else {
                    login = new AddAltThread(this.username.getText(), this.password.getText());
                }
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(26, 26, 26).getRGB());
        ClickGui.drawCenteredStrings("Add Alt", width / 2, 20, -1);
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.combined.drawTextBox();
        if (this.username.getText().isEmpty()) {
            FontRenderer.F16.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            FontRenderer.F16.drawStringWithShadow("Password", width / 2 - 96, 106, -7829368);
        }
        if (this.combined.getText().isEmpty()) {
            FontRenderer.F16.drawStringWithShadow("Email:Password", width / 2 - 96, 146, -7829368);
        }
        ClickGui.drawCenteredStrings(this.status, width / 2, 30, -1);
        super.drawScreen(i, j, f);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiMantheButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiMantheButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        this.username = new GuiMantheInputBox(1, FontRenderer.F16, width / 2 - 100, 60, 200, 20);
        this.password = new GuiMantheInputBox(2, FontRenderer.F16, width / 2 - 100, 100, 200, 20);
        this.combined = new GuiMantheInputBox(3, FontRenderer.F16, width / 2 - 100, 140, 200, 20);
        this.combined.setMaxStringLength(200);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        this.combined.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.combined.setFocused(!this.combined.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
        this.combined.mouseClicked(par1, par2, par3);
    }

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        public AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.access$0(GuiAddAlt.this, "\u00a77Waiting...");
        }

        private final void checkAndAddAlt(String username, String password) {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
                    .createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                AltManager.getAlts().add(new Alt(username, password));
                FileManager.saveAlts();
                GuiAddAlt.access$0(GuiAddAlt.this, "\u00a7aAlt added. (" + username + ")");
            } catch (AuthenticationException e) {
                GuiAddAlt.access$0(GuiAddAlt.this, "\u00a7cAlt failed!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.getAlts().add(new Alt(this.username, ""));
                FileManager.saveAlts();
                GuiAddAlt.access$0(GuiAddAlt.this, "\u00a7aAlt added. (" + this.username + " - offline name)");
                return;
            }
            GuiAddAlt.access$0(GuiAddAlt.this, "\u00a7eTrying alt...");
            this.checkAndAddAlt(this.username, this.password);
        }
    }

}
