package cn.BokerLite.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import cn.BokerLite.Client;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {
    private static final int[] colorCode = new int[32];
    public static final FontRenderer F11;
    public static final FontRenderer F12;
    public static final FontRenderer F13;
    public static final FontRenderer F14;
    public static final FontRenderer F16;
    public static final FontRenderer F18;
    public static final FontRenderer F24;
    public static final FontRenderer F20;
    public static final FontRenderer F28;
    public static final FontRenderer F22;
    public static final FontRenderer F33;
    public static final FontRenderer F35;
    public static final FontRenderer F40;

    static {
        Font font;
        try {
            font = Font.decode("微软雅黑");
        } catch (Exception e) {
            font = Font.decode("Arial");
        }
        F11 = new FontRenderer(font.deriveFont(11F));
        F12 = new FontRenderer(font.deriveFont(12F));
        F13 = new FontRenderer(font.deriveFont(13F));
        F14 = new FontRenderer(font.deriveFont(14F));
        F16 = new FontRenderer(font.deriveFont(16F));
        F24 = new FontRenderer(font.deriveFont(24F));
        F18 = new FontRenderer(font.deriveFont(18F));
        F20 = new FontRenderer(font.deriveFont(20F));
        F22 = new FontRenderer(font.deriveFont(22F));
        F35 = new FontRenderer(font.deriveFont(35F));
        F40 = new FontRenderer(font.deriveFont(40F));
        F28 = new FontRenderer(font.deriveFont(28F));
        F33 = new FontRenderer(font.deriveFont(33F));
        Client.customFontSupport = true;
        for (int i = 0; i < 32; ++i) {
            int base = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + base;
            int g = (i >> 1 & 1) * 170 + base;
            int b = (i >> 0 & 1) * 170 + base;
            if (i == 6) {
                r += 85;
            }

            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }

            colorCode[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }

    private final byte[][] charwidth = new byte[256][];
    private final int[] textures = new int[256];
    private final FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
    private Font font = null;

    private float size = 0;
    private int fontWidth = 0;
    private int fontHeight = 0;
    private int textureWidth = 0;
    private int textureHeight = 0;

    public FontRenderer(Font font) {
        this.font = font;
        size = font.getSize2D();
        Arrays.fill(textures, -1);
        Rectangle2D maxBounds = font.getMaxCharBounds(context);
        this.fontWidth = (int) Math.ceil(maxBounds.getWidth());
        this.fontHeight = (int) Math.ceil(maxBounds.getHeight());
        if (fontWidth > 127 || fontHeight > 127) throw new IllegalArgumentException("Font size to large!");
        this.textureWidth = resizeToOpenGLSupportResolution(fontWidth * 16);
        this.textureHeight = resizeToOpenGLSupportResolution(fontHeight * 16);
    }

    public FontRenderer() {}

    public int getFontHeight() {
        if (!Client.font.getValue()) {
            return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        }
        return fontHeight / 2;
    }

    protected int drawChar(char chr, float x, float y) {
        int region = chr >> 8;
        int id = chr & 0xFF;
        int xTexCoord = (id & 0xF) * fontWidth,
            yTexCoord = (id >> 4) * fontHeight;
        int width = getOrGenerateCharWidthMap(region)[id];
        GlStateManager.bindTexture(getOrGenerateCharTexture(region)); // 怒我直说GlStateManager是世界上最傻逼的东西
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_QUADS);
        glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        glVertex2f(x, y);
        glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        glVertex2f(x, y + fontHeight);
        glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        glVertex2f(x + width, y + fontHeight);
        glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        glVertex2f(x + width, y);
        glEnd();
        return width;
    }

    public int drawString(String str, float x, float y, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableBlend();
        return drawString(str, x, y, color, false);
    }

    public int drawString(String str, float x, float y, int color, boolean darken) {
        GlStateManager.enableBlend();
        GlStateManager.disableBlend();
        x *= 2;
        y *= 2;
        y -= 2;
        int offset = 0;
        if (darken) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000; // 相当于给RGB每个通道除以4
        }
        float r, g, b, a;
        r = (color >> 16 & 0xFF) / 255f;
        g = (color >> 8  & 0xFF) / 255f;
        b = (color >> 0  & 0xFF) / 255f;
        a = (color >> 24 & 0xFF) / 255f;
        if (a == 0)
            a = 1;
        GlStateManager.color(r, g, b, a);
        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if (chr == '\u00A7' && i != chars.length - 1) {
                i++;
                color = "0123456789abcdef".indexOf(chars[i]);
                if (color != -1) {
                    if (darken) color |= 0x10;
                    color = colorCode[color];
                    r = (color >> 16 & 0xFF) / 255f;
                    g = (color >> 8 & 0xFF) / 255f;
                    b = (color >> 0 & 0xFF) / 255f;
                    GlStateManager.color(r, g, b, a);
                }
                continue;
            }
            offset += drawChar(chr, x + offset, y);
        }
        glPopMatrix();
        return offset;
    }

    public int getStringWidth(String str) {
        if (!Client.font.getValue()) {
            return Minecraft.getMinecraft().fontRendererObj.getStringWidth(str);
        }
        int width = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if (chr == '\u00A7' && i != chars.length - 1)
                continue;
            width += getOrGenerateCharWidthMap(chr >> 8)[chr & 0xFF];
        }
        return width / 2;
    }

    public float getSize() {
        return size;
    }

    private int generateCharTexture(int id) {
        int textureId = glGenTextures();
        int offset = id << 8;
        BufferedImage img = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setFont(font);
        g.setColor(Color.WHITE);
        FontMetrics fontMetrics = g.getFontMetrics();
        for (int y = 0; y < 16; y++)
            for (int x = 0; x < 16; x++) {
                String chr = String.valueOf((char) ((y << 4 | x) | offset));
                g.drawString(chr, x * fontWidth, y * fontHeight + fontMetrics.getAscent());
            }
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageToBuffer(img));
        return textureId;
    }

    private int getOrGenerateCharTexture(int id) {
        if (textures[id] == -1)
            return textures[id] = generateCharTexture(id);
        return textures[id];
    }

    /**
     * 由于某些显卡不能很好的支持分辨率不是2的n次方的纹理，此处用于缩放到支持的范围内
     */
    private int resizeToOpenGLSupportResolution(int size) {
        int power = 0;
        while (size > 1 << power) power++;
        return 1 << power;
    }

    private byte[] generateCharWidthMap(int id) {
        int offset = id << 8;
        byte[] widthmap = new byte[256];
        for (int i = 0; i < widthmap.length; i++) {
            widthmap[i] = (byte) Math.ceil(font.getStringBounds(String.valueOf((char) (i | offset)), context).getWidth());
        }
        return widthmap;
    }

    private byte[] getOrGenerateCharWidthMap(int id) {
        if (charwidth[id] == null)
        	return charwidth[id] = generateCharWidthMap(id);
        return charwidth[id];
    }

    private double wrapTextureCoord(int coord, int size) {
        return coord / (double) size;
    }

    private ByteBuffer imageToBuffer(BufferedImage img) {
        int[] arr = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * arr.length);

        for (int i : arr) {
            buf.putInt(i << 8 | i >> 24 & 0xFF);
        }

        buf.flip();
        return buf;
    }

    protected void finalize() throws Throwable {
        for (int textureId : textures) {
            if (textureId != -1)
                glDeleteTextures(textureId);
        }
    }

    public void drawStringWithShadow(String newstr, int i, int i1, int rgb) {
        if (!Client.font.getValue()) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(newstr, i, i1, rgb);
            return;
        }
        drawString(newstr, i + 1, i1 + 1, rgb, true);
        drawString(newstr, i, i1, rgb);
    }
}
