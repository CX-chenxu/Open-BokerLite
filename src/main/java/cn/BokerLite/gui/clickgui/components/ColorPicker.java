package cn.BokerLite.gui.clickgui.components;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.modules.value.Colors;
import cn.BokerLite.modules.value.Option;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends Component {
    float[] hsb = new float[]{0F, 360F, 360F};
    int position;
    int color;

    Colors c;

    Option<Boolean> rainbow = new Option<>("Rainbow mode", "彩虹模式", "rainbow", false);

    private final CheckBox rainbowMode = new CheckBox(rainbow);
	private final Component parent;
    
    private static final int HEIGHT = 14;
    
    public static final ColorChanger timer = new ColorChanger();
    
    public ColorPicker(Colors colors, Component parent) {
        this.color = colors.getColor().getRGB();
        this.c = colors;
        this.position = -1111;
        this.parent = parent;
        timer.add(this);
    }


    public int getHeight() {
        return HEIGHT + 2 + rainbowMode.getHeight();
    }

    public int getColor() {
        return color;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
    	GL11.glPushMatrix();
    	GL11.glPushMatrix();
    	int offset = (parent.getWidth() - getWidth()) / 2;
    	GL11.glTranslated(offset, 0, 0);
    	mouseX -= offset;
    	synchronized(this) {
    		float[] huee = hsb.clone();
            int x = 0;
            int i = x;
            int y = 3;
            while (i < x + 90) {
                int color = Color.getHSBColor(huee[0] / 360.0f, huee[1] / 360.0f, huee[2] / 360.0f).getRGB();
                if (mouseX > i - 1 && mouseX < i + 1 && mouseY > y - 6 && mouseY < y + 12 && Mouse.isButtonDown(0)) {
                    this.color = color;
                    this.position = i;
                }
                Gui.drawRect(i - 1, y - 2, i, y - 2 + 12, color);
                huee[0] = huee[0] + 4.0f;
                if (huee[0] > 360.0f) {
                    huee[0] = huee[0] - 360.0f;
                }
                ++i;
            }
            Gui.drawRect(this.position-1, y - 2, this.position, y - 2 + 12, -1);
            if (this.hsb[0] > 360.0f) {
                this.hsb[0] = this.hsb[0] - 360.0f;
            }
            c.setColor(new Color(getColor()));
    	}
    	GL11.glPopMatrix();
        GL11.glTranslatef(0, HEIGHT, 0);
        rainbowMode.drawComponent(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	if(mouseY < HEIGHT) return;
    	rainbowMode.mouseClicked(mouseX, mouseY - HEIGHT, mouseButton);
    }

    public int getWidth() {
        return 90;
    }


	public static void update() {
		timer.update();
	}
    
}

class ColorChanger {
	private final List<WeakReference<ColorPicker>> pickers = new ArrayList<>();
	
	public void update() {
		synchronized(pickers) {
			pickers.removeIf(e -> e.get() == null);
			for(WeakReference<ColorPicker> picker : pickers) {
				changeColor(picker.get());
			}
		}
	}
	
	synchronized void add(ColorPicker colorPicker) {
		pickers.add(new WeakReference<>(colorPicker));
	}

	private void changeColor(ColorPicker picker) {
		if(!picker.rainbow.getValue()) return;
		synchronized (picker) {
			if(picker.position < 0) picker.position = 0;
			picker.position++;
			picker.position %= 89;
			float[] hsb = picker.hsb.clone();
			hsb[0] = picker.position * 4;
			if (hsb[0] > 360.0f) {
	            hsb[0] = hsb[0] - 360.0f;
	        }
			picker.color = Color.getHSBColor(hsb[0] / 360.0f, hsb[1] / 360.0f, hsb[2] / 360.0f).getRGB();
			picker.c.setColor(new Color(picker.getColor()));
		}
	}
}
