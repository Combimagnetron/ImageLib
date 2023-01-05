package com.combimagnetron.imageloader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public final class Image implements IImage {
    private final Path path;
    private final String url;
    private final ColorType colorType;
    private final int ascent;
    private String result;
    private BufferedImage image;

    public enum ColorType {
        LEGACY, MINIMESSAGE;
    }
    private Image(Path path, ColorType colorType, int ascent) {
        this.path = path;
        this.url = null;
        this.colorType = colorType;
        this.ascent = ascent;
    }

    private Image(String url, ColorType colorType, int ascent) {
        this.path = null;
        this.url = url;
        this.colorType = colorType;
        this.ascent = ascent;
    }


    @Override
    public void color(Color color) {
        image.getGraphics().setColor(color);
    }

    @Override
    public void color(int[] from, int[] to, Color color) {
        //TODO
    }

    @Override
    public void color(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public static Builder builder() {
        return new Builder();
    }

    public String generate() {
        if (url != null) {
            URL url;
            HttpURLConnection connection;
            try {
            url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                image = ImageIO.read(connection.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            generateString();
            return result;
        } else if (path != null) {
            try {
                image = ImageIO.read(path.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            generateString();
            return result;
        }
        return "null";
    }

    private void generateString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x <= (image.getWidth() - 1); x++) {
            Pixel lastPixel = null;
            for (int y = 0; y <= (image.getHeight() - 1); y++) {
                Pixel pixel = new Pixel(image, x, y, ascent);
                String icon = pixel.getIcon();
                if (!(pixel.getColor().getAlpha() < 255)) {
                    if (colorType == Image.ColorType.MINIMESSAGE) {
                        if (lastPixel != null && !lastPixel.isClose(pixel, 0))
                            stringBuilder.append(pixel.getMiniMessageColor());
                        else if (lastPixel == null) {
                            stringBuilder.append(pixel.getMiniMessageColor());
                        }
                    } else if (colorType == Image.ColorType.LEGACY) {
                        if (lastPixel != null && !lastPixel.isClose(pixel, 0))
                            stringBuilder.append(pixel.getLegacyColor());
                        else if (lastPixel == null) {
                            stringBuilder.append(pixel.getLegacyColor());
                        }
                    }
                } else {
                    icon = "\uF8F4";
                }
                stringBuilder.append(icon);
                lastPixel = pixel;
                if (y != (image.getHeight() - 1)) stringBuilder.append("\uE3E3");
            }
            stringBuilder.append("\uE3E2");
        }
        this.result = stringBuilder.toString().trim();
    }

    public static class Builder {
        private static Path path;
        private static String url;
        private static ColorType colorType;
        private static int ascent = 0;

        public Builder image(Path path) {
            Builder.path = path;
            return this;
        }

        public Builder image(String url) {
            Builder.url = url;
            return this;
        }

        public Builder colorType(ColorType colorType) {
            Builder.colorType = colorType;
            return this;
        }

        public Builder ascent(int ascent) {
            Builder.ascent = ascent;
            return this;
        }

        public Image build() {
            if (colorType == null) colorType = ColorType.LEGACY;
            if (path != null)
                return new Image(path, colorType, ascent);
            else if (url != null)
                return new Image(url, colorType, ascent);
            return null;
        }
    }







}
