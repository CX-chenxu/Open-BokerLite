package cn.BokerLite.gui.keybindgui;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

import static cn.BokerLite.Client.mc;

public class KeyInfo {
    public float posX;
    public float posY;
    public float width;
    public float height;
    public int key;
    private final String keyName;
    private final String keyDisplayName;
    private final int keyColor;
    private final int shadowColor;
    private final int unusedColor;
    private final int usedColor;
    private final int baseTabHeight;
    private final int baseTabWidth;
    private final boolean direction;

    private ArrayList<Module> modules;
    private boolean hasKeyBind;
    private float stroll;
    private int maxStroll;

    public KeyInfo(float posX, float posY, float width, float height, int key, String keyName, String keyDisplayName) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.key = key;
        this.keyName = keyName;
        this.keyDisplayName = keyDisplayName;

        this.keyColor = new Color(240, 240, 240).getRGB();
        this.shadowColor = new Color(210, 210, 210).getRGB();
        this.unusedColor = new Color(200, 200, 200).getRGB();
        this.usedColor = new Color(0, 0, 0).getRGB();
        this.baseTabHeight = 150;
        this.baseTabWidth = 100;
        this.direction = posY >= 100;

        this.modules = new ArrayList<>();
        this.hasKeyBind = false;
        this.stroll = 0;
        this.maxStroll = 0;
    }

    public KeyInfo(float posX, float posY, float width, float height, int key,String keyName) {
        this(posX, posY, width, height, key, keyName, keyName);
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX,posY,0F);
        RenderUtil.drawRect(0F,0F,width,height,keyColor);
        RenderUtil.drawRect(0F, height * 0.9F, width, height, shadowColor);
        FontRenderer.F40.drawString(keyName,
                width * 0.5F - 10F,height * 0.9F * 0.5F - (FontRenderer.F35.getFontHeight() * 0.5F) + 3F
                ,hasKeyBind ? usedColor : unusedColor, false);
        GL11.glPopMatrix();
    }

    public void renderTab() {
        GL11.glPushMatrix();
        GL11.glTranslatef((posX + width * 0.5F) - baseTabWidth * 0.5F, direction ? posY - baseTabHeight : posY + height, 0F);
        RenderUtil.drawRect(0F, 0F, baseTabWidth, baseTabHeight, Color.WHITE.getRGB());
        float fontHeight = 10F - FontRenderer.F40.getFontHeight() * 0.5F;
        float yOffset = (12F + FontRenderer.F40.getFontHeight() + 10F) - stroll;
        for (Module module : modules) {
            if (yOffset> 0 && (yOffset - 20) <100) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0F, yOffset, 0F);
                FontRenderer.F40.drawString(Client.chinese ? module.chinese : module.getName(), 12F, fontHeight, Color.DARK_GRAY.getRGB(), false);
                FontRenderer.F40.drawString(
                        "-", baseTabWidth - 12F - FontRenderer.F40.getStringWidth("-"), fontHeight, Color.RED.getRed(), false
                );
                GL11.glPopMatrix();
            }
            yOffset += 20;
        }
        RenderUtil.drawRect(0F, 0F, baseTabWidth, 12F + FontRenderer.F40.getFontHeight() + 10F, Color.WHITE.getRGB());
        RenderUtil.drawRect(0F, baseTabHeight - 22F - FontRenderer.F40.getFontHeight(), baseTabWidth, baseTabHeight, Color.WHITE.getRed());
        FontRenderer.F18.drawString(keyDisplayName,12F,12F,Color.WHITE.getRGB());
        FontRenderer.F18.drawString("ui.keybind.add",baseTabWidth - 12F - FontRenderer.F40.getStringWidth("ui.keybind.add"), baseTabHeight - 12F - FontRenderer.F40.getFontHeight(), new Color(0, 191, 255).getRGB());
        GL11.glPopMatrix();
    }

    public void stroll(float mouseX, float mouseY, float wheel) {
        float scaledMouseX = mouseX - ((posX + width * 0.5F) - baseTabWidth * 0.5F);
        float scaledMouseY = mouseY - (direction ? posY - baseTabHeight : posY + height);
        if (scaledMouseX <0 || scaledMouseY <0 || scaledMouseX> baseTabWidth || scaledMouseY> baseTabHeight)
            return;

        float afterStroll = stroll - (wheel / 40);
        if (afterStroll> 0 && afterStroll <(maxStroll - 150)) {
            stroll = afterStroll;
        }
    }

    public void update() {
        modules = ModuleManager.getModules();
        hasKeyBind = modules.size() > 0;
        stroll = 0;
        maxStroll = modules.size() * 30;
    }

    public void click(float mouseX, float mouseY) {
        KeyBindManager keyBindManager = Client.keyBindManager;
        if (keyBindManager.nowDisplayKey == null) {
            keyBindManager.nowDisplayKey = this;
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.click"),1F));
        } else {
            float scaledMouseX = mouseX - (this.posX + this.width * 0.5F - (float)this.baseTabWidth * 0.5F);
            float scaledMouseY = mouseY - (this.direction?this.posY - (float)this.baseTabHeight:this.posY + this.height);
            if(scaledMouseX < 0.0F || scaledMouseY < 0.0F || scaledMouseX > (float)this.baseTabWidth || scaledMouseY > (float)this.baseTabHeight) {
                keyBindManager.nowDisplayKey = null;
                return;
            }

            if(scaledMouseY > 22.0F + FontRenderer.F40.getFontHeight() && scaledMouseX > baseTabWidth - 12.0F - FontRenderer.F40.getStringWidth("Add")) {
                if(scaledMouseY > baseTabHeight - 22.0F - FontRenderer.F40.getFontHeight()) {
                    keyBindManager.popUI = new KeySelectUI(this);
                } else {
                    float yOffset = 12.0F + (float)FontRenderer.F40.getFontHeight() + 10.0F - this.stroll;
                    for (Module module : modules) {
                        if (scaledMouseY > (yOffset + 5) && scaledMouseY < (yOffset + 15)) {
                            module.key = Keyboard.KEY_NONE;
                            update();
                            break;
                        }
                        yOffset += 20;
                    }
                }
            }
        }
    }
}
