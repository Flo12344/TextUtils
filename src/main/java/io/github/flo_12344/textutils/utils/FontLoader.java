package io.github.flo_12344.textutils.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static Font loadFont(String fontPath) throws IOException, FontFormatException {
        File fontFile = new File(fontPath + ".ttf");
        if (!fontFile.isFile()) {
            fontFile = new File(fontPath + ".otf");
        }
        if (!fontFile.isFile()) {
            throw new IOException("Font file not found: " + fontPath);
        }
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        return font;
    }

    public static FontMetrics getFontMetrics(Font font) {
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImage.createGraphics();
        tempG2d.setFont(font);
        FontMetrics fm = tempG2d.getFontMetrics();
        tempG2d.dispose();
        return fm;
    }
}
