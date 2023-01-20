package com.combimagnetron.imageloader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static String generateStringFromImage(BufferedImage image, Image.ColorType colorType, int ascent) {
        final StringBuilder stringBuilder = new StringBuilder();
        Pixel lastPixel = null;
        for (int x = 0; x <= (image.getWidth() - 1); x++) {
            for (int y = 0; y <= (image.getHeight() - 1); y++) {
                Pixel pixel = new Pixel(image, x, y, -ascent);
                String icon = pixel.getIcon();
                if (lastPixel != null && !lastPixel.isClose(pixel, 0)) {
                    stringBuilder.append(pixel.getColor(colorType));
                }
                if (pixel.getColor().getAlpha() < 255) icon = "\uF8F4";
                stringBuilder.append(icon);
                lastPixel = pixel;
                if (y != (image.getHeight() - 1)) stringBuilder.append("\uE3E3");
            }
            stringBuilder.append("\uE3E2");
        }
        return stringBuilder.toString().trim();
    }

    public static BufferedImage merge(BufferedImage original, BufferedImage overlay) {
        BufferedImage master = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = master.getGraphics();
        graphics.drawImage(original, 0, 0, null);
        graphics.drawImage(overlay, 0, 0, null);
        graphics.dispose();
        return master;
    }

}
