package com.combimagnetron.imageloader;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pixel {
    private final BufferedImage image;
    private final int x;
    private final int y;
    private final Color color;
    private final int ascent;

    public Pixel(BufferedImage image, int x, int y, int ascent) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.color = new Color(image.getRGB(x, y), true);
        this.ascent = ascent;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public String getLegacyColor() {
        return String.valueOf(ChatColor.of(getColor()));
    }

    public String getMiniMessageColor() {
        return "<#" + Integer.toHexString(getColor().getRGB()).substring(2) + ">";
    }

    public boolean isClose(Pixel pixel, int threshold) {
        var external = pixel.getColor();
        var local = getColor();
        int r = external.getRed() - local.getRed(), g = external.getGreen() - local.getGreen(), b = external.getBlue()- local.getBlue();
        return (r*r + g*g + b*b) <= threshold*threshold;
    }

    public String getIcon() {
        return IconEnum.valueOf("icon" + (y + 1 + ascent)).getIcon();
    }
}
