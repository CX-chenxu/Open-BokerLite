package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import cn.BokerLite.Client;
import cn.BokerLite.api.enums.NotificationType;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.*;
import cn.BokerLite.utils.mod.MessageUtils;

import java.util.List;

public class ModuleButton extends Button {

    public static boolean expand = false;

    final Module module;

    final BlockQuote quote;

    private boolean expanded;

    @SuppressWarnings("unchecked")
    public ModuleButton(Module module, SubWindow parent) {
        super(Client.chinese ? module.chinese : module.getName(), parent);
        this.module = module;
        List<Value<?>> values = module.getValues();
        if (values.isEmpty()) {
            quote = null;
            return;
        }
        quote = new BlockQuote(parent);
        for (Value<?> value : values) {
            if (value instanceof Option) {
                Option<Boolean> option = (Option<Boolean>) value;
                quote.addComponent(new CheckBox(option));
            }
            if (value instanceof Mode) {
                Mode<Enum<?>> option = (Mode<Enum<?>>) value;
                quote.addComponent(new ModeSelection(option, quote));
            }
            if (value instanceof Numbers) {
                quote.addComponent(new SliderBar((Numbers) value, quote));
            }
            if (value instanceof Colors) {
                quote.addComponent(new ColorPicker((Colors) value, quote));
            }
        }
    }

    public void expand() {
        expanded = !expanded;
    }

    public boolean canExpand() {
        return quote != null;
    }

    public int getHeight() {
        int height = super.getHeight();
        if (!expanded && !expand) {
            return height;
        }
        return height + quote.getHeight();
    }

    private int getYOffset() {
        return expanded ? -quote.getHeight() : 0;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int yOff = getYOffset();
        if (mouseX > 0 && mouseX < parent.getWidth() - 10 &&
                mouseY > yOff && mouseY < getHeight() + yOff) {
            Gui.drawRect(0, 0, parent.getWidth() - 10, getHeight() + yOff, 0xAA000000);
        }
        int x = (parent.getWidth() - FontRenderer.F16.getStringWidth(Client.chinese ? module.chinese : module.getName()) - 8) / 2;
        int y = 6;
        FontRenderer.F16.drawString(((!Client.blatant.getValue()) ? EnumChatFormatting.GRAY.toString() : "") + (Client.chinese ? module.chinese : module.getName()), x, y, isEnabled() ? Client.THEME_RGB_COLOR : -1);
        if (canExpand()) {
            if (mouseX > parent.getWidth() - 10 && mouseX < parent.getWidth() &&
                    mouseY > 0 && mouseY < getHeight()) {
                Gui.drawRect(parent.getWidth() - 10, 0, parent.getWidth(), 17, 0xAA000000);
            }
            FontRenderer.F16.drawString(expanded ? "-" : "+", parent.getWidth() - 8, 6, -1);
            if (expanded) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, super.getHeight(), 0);
                quote.drawComponent(mouseX, mouseY - super.getHeight(), partialTicks);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean isEnabled() {
        return module.state;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (mouseX > 0 && mouseX < parent.getWidth() - 10 &&
                    mouseY > 0 && mouseY < super.getHeight()) {

                    module.toggle();

            }
        }  //            GuiHelper.displayGuiScreen(new BindScreen(module));


        if (canExpand()) {
            if (mouseX > parent.getWidth() - 10 && mouseX < parent.getWidth() &&
                    mouseY > 0 && mouseY < getHeight()) {
                expand();
            } else {
                quote.mouseClicked(mouseX, mouseY - super.getHeight(), mouseButton);
            }
        }
    }

    public int getWidth() {
        return canExpand() ? Math.max(super.getWidth() + 10, quote.getWidth()) : super.getWidth() + 10;
    }

	public Module getModule() {
		return module;
	}
}
