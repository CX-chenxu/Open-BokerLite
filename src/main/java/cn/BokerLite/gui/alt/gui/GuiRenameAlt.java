package cn.BokerLite.gui.alt.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.alt.gui.component.GuiMantheButton;
import cn.BokerLite.gui.alt.gui.component.GuiMantheInputBox;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;

public class GuiRenameAlt extends GuiScreen {
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private String status = "\u00a7eWaiting...";
    private GuiTextField pwField;

    public GuiRenameAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                if (!this.pwField.getText().isEmpty()) {
                    this.manager.selectedAlt.setPassword(this.pwField.getText());
                }
                this.status = "\u00a7aEdited!";
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(26, 26, 26).getRGB());
        ClickGui.drawCenteredString("Edit Alt", width / 2, 10, -1);
        ClickGui.drawCenteredString(this.status, width / 2, 20, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            FontRenderer.F16.drawStringWithShadow("New E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            FontRenderer.F16.drawStringWithShadow("New Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiMantheButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiMantheButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiMantheInputBox(3, FontRenderer.F16, width / 2 - 100, 60, 200, 20);
        this.pwField = new GuiMantheInputBox(4, FontRenderer.F16, width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
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
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}
