package cn.BokerLite.gui.keybindgui;

import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class KeySelectUI extends PopUI {
    private final KeyInfo info;
    private String str = "";
    private ArrayList<Module> modules = ModuleManager.getModules();
    private final float singleHeight = 4F + FontRenderer.F35.getFontHeight();
    private float stroll = 0;
    private float maxStroll = modules.size() * singleHeight;
    private final float height = 8F + FontRenderer.F40.getFontHeight() + FontRenderer.F35.getFontHeight() + 0.5F;

    public KeySelectUI(KeyInfo info) {
        super("ui.keybind.select");
        this.info = info;
    }

    @Override
    public void render() {
        float yOffset = height - stroll + 5F;
        for (Module module : modules) {
            if (yOffset > (height - singleHeight) && (yOffset - singleHeight) < 190) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0F,yOffset,0F);
                String name = module.getName();
                FontRenderer.F35.drawString(str.isEmpty()
                        ? "§0"+name :
                        "§0"+name.substring(0,str.length())+"§7"+name.substring(str.length())
                        ,8F,singleHeight * 0.5F, Color.BLACK.getRGB(),false);
                GL11.glPopMatrix();
            }
            yOffset += singleHeight;
        }
        RenderUtil.drawRect(0F,8F+FontRenderer.F40.getFontHeight(),getBaseHeight(),height + 5F,Color.WHITE.getRGB());
        RenderUtil.drawRect(0.0F, getBaseHeight() - singleHeight, getBaseWidth(), getBaseHeight(), Color.WHITE.getRGB());
        FontRenderer.F35.drawString(str.isEmpty() ? "ui.keybind.search" : str,8F,8F + FontRenderer.F40.getFontHeight() + 4F,Color.LIGHT_GRAY.getRGB(),false);
        RenderUtil.drawRect(8F,height + 2F,getBaseHeight() - 8F,height +3F,Color.LIGHT_GRAY.getRGB());
    }

    @Override
    public void key(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_BACK) {
            if (!str.isEmpty()) {
                str = str.substring(0,str.length() - 1);
                update();
            }
            return;
        } else if (keyCode == Keyboard.KEY_RETURN) {
            if (!modules.isEmpty()) {
                apply(modules.get(0));
            }
            return;
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            str += typedChar;
            update();
        }
    }

    @Override
    public void stroll(float mouseX, float mouseY, int wheel) {
        float afterStroll = stroll - (wheel / 10F);
        if (afterStroll > 0 && afterStroll < (maxStroll - 100))
            stroll = afterStroll;
    }

    @Override
    public void click(float mouseX, float mouseY) {
        if (mouseX < 8 || mouseX > (getBaseWidth() - 8) || mouseY < height || mouseY > (getBaseHeight() - singleHeight))
            return;
        float yOffset = height - stroll + 2F;
        for (Module module : modules) {
            if (mouseY > yOffset && mouseY < (yOffset + singleHeight)) {
                apply(module);
                break;
            }
            yOffset += singleHeight;
        }
    }

    private void apply(Module module) {
        module.setKey(info.key);
        Client.keyBindManager.updateAllKeys();
        close();
    }

    private void update() {
        if (str.isEmpty()) {
            modules = ModuleManager.getModules();
        } else {
            for (Module module : ModuleManager.getModules()) {
                if (module.getName().toLowerCase().startsWith(str.toLowerCase()))
                    modules.add(module);
            }
        }
        maxStroll = modules.size() * singleHeight;
        stroll = 0;
    }
}
