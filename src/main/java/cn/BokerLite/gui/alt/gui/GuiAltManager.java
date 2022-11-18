package cn.BokerLite.gui.alt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.alt.gui.component.GuiMantheButton;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.utils.FileManager;
import cn.BokerLite.utils.reflect.ReflectionUtil;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class GuiAltManager extends GuiScreen {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public Alt selectedAlt = null;
    private GuiMantheButton login;
    private GuiMantheButton remove;
    private GuiMantheButton rename;
    private AltLoginThread loginThread;
    private int offset;
    private String status = "\u00a7eWaiting...";
    private String val = "";

    public GuiAltManager() {
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals("Logging in...")
                        && !this.loginThread.getStatus().equals("Do not hit back! Logging in...")) {
                    mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus("Do not hit back! Logging in...");
                break;
            }
            case 1: {
                String user = this.selectedAlt.getUsername();
                String pass = this.selectedAlt.getPassword();
                this.loginThread = new AltLoginThread(user, pass);
                this.loginThread.start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.getAlts().remove(this.selectedAlt);
                this.status = "\u00a7cRemoved.";
                this.selectedAlt = null;
                FileManager.saveAlts();
                break;
            }
            case 3: {
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                String username = "Czf_" + new Random().nextInt(9999);
                ReflectionUtil.setFieldValue(mc, new Session(username, "", "", "mojang"), "session", "field_71449_j");
                status = "\u00a7aLogon as " + username;
                break;
            }
            case 6: {
                mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                Alt lastAlt = AltManager.lastAlt;
                if (lastAlt == null) {
                    if (this.loginThread == null) {
                        this.status = "?cThere is no last used alt!";
                        break;
                    }
                    this.loginThread.setStatus("?cThere is no last used alt!");
                    break;
                }
                String user2 = lastAlt.getUsername();
                String pass2 = lastAlt.getPassword();
                this.loginThread = new AltLoginThread(user2, pass2);
                this.loginThread.start();
            }
            case 8: {

            }
            case 9: {

            }
        }
    }

    public String getStringRandom(int length) {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(26, 26, 26).getRGB());

        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        FontRenderer.F16.drawStringWithShadow(mc.getSession().getUsername(), 10, 10, -7829368);
        ClickGui.drawCenteredString("Account Manager - " + AltManager.getAlts().size() + " alts", width / 2, 10, -1);
        ClickGui.drawCenteredString(this.loginThread == null ? this.status : this.loginThread.getStatus(), width / 2, 20, -1);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, width, height - 50);
        GL11.glEnable(3089);
        int y = 38;
        for (Alt alt : AltManager.getAlts()) {
            if (!this.isAltInArea(y))
                continue;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            String pass = alt.getPassword().equals("") ? "\u00a7cCracked" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f,
                            -16777216, -2142943931);
                } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f,
                            -16777216, -2142088622);
                } else {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f,
                            -16777216, -2144259791);
                }
            } else if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f,
                        -16777216, -2146101995);
            } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f,
                        -16777216, -2145180893);
            }
            ClickGui.drawCenteredString(name, width / 2, y - this.offset, -1);
            ClickGui.drawCenteredString(pass, width / 2, y - this.offset + 10, 5592405);
            y += 26;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = false;
        } else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        } else if (Keyboard.isKeyDown(208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiMantheButton(0, width / 2 + 4 + 76, height - 24, 75, 20, "Cancel"));
        this.login = new GuiMantheButton(1, width / 2 - 154, height - 48, 70, 20, "Login");
        this.buttonList.add(this.login);
        this.remove = new GuiMantheButton(2, width / 2 - 74, height - 24, 70, 20, "Remove");
        this.buttonList.add(this.remove);
        this.buttonList.add(new GuiMantheButton(3, width / 2 + 4 + 76, height - 48, 75, 20, "Add"));
        this.buttonList.add(new GuiMantheButton(4, width / 2 - 74, height - 48, 70, 20, "Direct Login"));
        this.buttonList.add(new GuiMantheButton(5, width / 2 + 4, height - 48, 70, 20, "Random Login"));
        this.rename = new GuiMantheButton(6, width / 2 + 4, height - 24, 70, 20, "Edit");
        this.buttonList.add(this.rename);
        GuiMantheButton lastalt = new GuiMantheButton(7, width / 2 - 154, height - 24, 70, 20, "Last Alt");
        this.buttonList.add(new GuiMantheButton(8, width / 2 - 234, height - 48, 70, 20, "Microsoft"));
        this.buttonList.add(lastalt);
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }

    private boolean isAltInArea(int y) {
        return y - this.offset <= height - 50;
    }

    private boolean isMouseOverAlt(int x, int y, int y1) {
        return x >= 52 && y >= y1 - 4 && x <= width - 52 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= width
                && y <= height - 50;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset;
        for (Alt alt : AltManager.getAlts()) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareScissorBox(float x, float y, float x2, float y2) {
        int factor = new ScaledResolution(mc).getScaleFactor();
        GL11.glScissor((int) (x * (float) factor),
                (int) (((float) new ScaledResolution(mc).getScaledHeight() - y2) * (float) factor),
                (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }
}
