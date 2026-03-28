package com.david.framework.desktop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.david.framework.Graphics;
import com.david.framework.Pixmap;

public class DesktopGraphics implements Graphics {
    private final BufferedImage frameBuffer;
    private final Graphics2D g2d;

    public DesktopGraphics(BufferedImage frameBuffer) {
        this.frameBuffer = frameBuffer;
        this.g2d = frameBuffer.createGraphics();
    }

    private static Color toColor(int color) {
        return new Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        InputStream in = DesktopGraphics.class.getResourceAsStream("/" + fileName);
        if (in == null)
            throw new RuntimeException("Couldn't load image: " + fileName);
        try {
            var image = ImageIO.read(in);
            if (image == null)
                throw new RuntimeException("Couldn't decode image: " + fileName);
            return new DesktopPixmap(image, format);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load image: " + fileName);
        } finally {
            try { in.close(); } catch (IOException e) {}
        }
    }

    @Override
    public void clear(int color) {
        g2d.setColor(toColor(color));
        g2d.fillRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        g2d.setColor(toColor(color));
        g2d.drawLine(x, y, x, y);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        g2d.setColor(toColor(color));
        g2d.drawLine(x, y, x2, y2);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        g2d.setColor(toColor(color));
        g2d.fillRect(x, y, width, height);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        var image = ((DesktopPixmap) pixmap).image;
        g2d.drawImage(image,
                x, y, x + srcWidth, y + srcHeight,
                srcX, srcY, srcX + srcWidth, srcY + srcHeight,
                null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        g2d.drawImage(((DesktopPixmap) pixmap).image, x, y, null);
    }

    @Override
    public int getWidth() { return frameBuffer.getWidth(); }

    @Override
    public int getHeight() { return frameBuffer.getHeight(); }
}
