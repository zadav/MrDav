package com.david.framework.desktop;

import java.awt.image.BufferedImage;
import com.david.framework.Graphics.PixmapFormat;
import com.david.framework.Pixmap;

public class DesktopPixmap implements Pixmap {
    final BufferedImage image;
    private final PixmapFormat format;

    public DesktopPixmap(BufferedImage image, PixmapFormat format) {
        this.image = image;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        // GC handles it
    }
}
