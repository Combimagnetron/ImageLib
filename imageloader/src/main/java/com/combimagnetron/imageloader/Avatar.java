package com.combimagnetron.imageloader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Avatar {
    private final BufferedImage image;
    private final boolean isSlim;
    private final BufferedImage skinFront;
    private final BufferedImage skinSideLeft;
    private final BufferedImage skinSideRight;
    private Image.ColorType colorType = Image.ColorType.LEGACY;

    protected Avatar(String playerName, int scale, boolean slim, Image.ColorType colorType) {
        try {
            image = new BufferedImage(64, 64, 2);
            Graphics graphics = image.getGraphics();
            BufferedImage temp = ImageIO.read(new URL("https://mineskin.eu/skin/" + playerName));
            graphics.drawImage(temp, 0, 0, null);
            graphics.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.isSlim = slim;
        this.skinFront = generateBody();
        this.skinSideLeft = generateLeft(0);
        this.skinSideRight = generateRight(0);
        this.colorType = colorType;
    }

    protected Avatar(BufferedImage image, int scale, boolean slim) {
        this.image = image;
        this.isSlim = slim;
        this.skinFront = generateBody();
        this.skinSideLeft = generateLeft(0);
        this.skinSideRight = generateRight(0);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSmallSkin(int ascent, int scale) {
        return ImageUtils.generateStringFromImage(getSmallSkinBufferedImage(scale), colorType, ascent);
    }

    public String getFullBody(int scale, int ascent) {
        return ImageUtils.generateStringFromImage(scale(skinFront, scale), colorType, ascent);
    }

    public String getHead(int scale, int ascent) {
        return ImageUtils.generateStringFromImage(scale(skinFront.getSubimage(4, 0, 8, 8), scale), colorType, ascent);
    }

    public BufferedImage getSkinSideRight(int scale) {
        return scale(skinSideRight, scale);
    }

    public BufferedImage getSkinSideLeft(int scale) {
        return scale(skinSideLeft, scale);
    }

    public BufferedImage getSkinFront(int scale) {
        return scale(skinFront, scale);
    }

    private void setOverlay(BufferedImage image, int alpha) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(0, 0, 0, alpha));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
    }

    private BufferedImage scale(BufferedImage image, int scale) {
        int width = image.getWidth() * scale;
        int height = image.getHeight() * scale;
        final BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        finalImage.getGraphics().drawImage(scaledImage, 0, 0, null);
        finalImage.getGraphics().dispose();
        return finalImage;
    }

    private BufferedImage getPart(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    private void drawOverlay(BufferedImage image, BufferedImage overlayImage) {
        Graphics graphics = image.getGraphics();
        graphics.drawImage(overlayImage, 0, 0, null);
        graphics.dispose();
    }

    private BufferedImage generateBody() {
        final BufferedImage avatarImage = new BufferedImage(16, 32,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = avatarImage.getGraphics();
        final int offset = isSlim ? 1 : 0;
        BufferedImage head = getPart(8 , 8, 8, 8);
        BufferedImage torso = getPart(20 , 20, 8, 12);
        BufferedImage leftArm = getPart(36 , 52, 4 - offset, 12);
        BufferedImage rightArm = getPart(44 , 20, 4 - offset, 12);
        BufferedImage leftLeg = getPart(20 , 52, 4, 12);
        BufferedImage rightLeg = getPart(4 , 20, 4, 12);
        drawOverlay(head, image.getSubimage(40, 8, 8, 8));
        drawOverlay(torso, image.getSubimage(20, 36, 8, 12));
        drawOverlay(leftArm, image.getSubimage(52, 52, 4 - offset, 12));
        drawOverlay(rightArm, image.getSubimage(44, 36, 4 - offset, 12));
        drawOverlay(leftLeg, image.getSubimage(4, 52, 4, 12));
        drawOverlay(rightLeg, image.getSubimage(4, 36, 4, 12));
        graphics.drawImage(head, 4, 0, null);
        graphics.drawImage(torso, 4, 8, null);
        graphics.drawImage(leftArm, 12, 8, null);
        graphics.drawImage(rightArm, offset, 8, null);
        graphics.drawImage(leftLeg, 8, 20, null);
        graphics.drawImage(rightLeg, 4, 20, null);
        graphics.dispose();
        return avatarImage;
    }

    private BufferedImage generateLeft(int overlay) {
        final BufferedImage avatarImage = new BufferedImage(8, 32,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = avatarImage.getGraphics();
        final int offset = isSlim ? 1 : 0;
        final int alpha = ((overlay / 100) * 255);
        BufferedImage leftHead = getPart(16, 8, 8, 8);
        BufferedImage leftArm = getPart(40 - offset, 52, 4, 12);
        BufferedImage leftLeg = getPart(24, 52, 4, 12);
        setOverlay(leftHead, alpha);
        setOverlay(leftArm, alpha);
        setOverlay(leftLeg, alpha);
        drawOverlay(leftHead, image.getSubimage(48, 8, 8, 8));
        drawOverlay(leftArm, image.getSubimage(56 - offset, 52, 4, 12));
        drawOverlay(leftLeg, image.getSubimage(8, 52, 4, 12));
        graphics.drawImage(leftHead, 0,0, null);
        graphics.drawImage(leftArm, 2, 8, null);
        graphics.drawImage(leftLeg, 2, 20, null);
        graphics.dispose();
        return avatarImage;
    }

    private BufferedImage generateRight(int overlay) {
        final BufferedImage avatarImage = new BufferedImage(8, 32,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = avatarImage.getGraphics();
        final int offset = isSlim ? 1 : 0;
        final int alpha = ((overlay / 100) * 255);
        BufferedImage rightHead = getPart(0, 8, 8, 8);
        BufferedImage rightArm = getPart(40, 20, 4, 12);
        BufferedImage rightLeg = getPart(0, 20, 4, 12);
        drawOverlay(rightHead, image.getSubimage(32, 8, 8, 8));
        drawOverlay(rightArm, image.getSubimage(40, 36, 4, 12));
        drawOverlay(rightLeg, image.getSubimage(0, 36, 4, 12));
        setOverlay(rightHead, alpha);
        setOverlay(rightArm, alpha);
        setOverlay(rightLeg, alpha);
        graphics.drawImage(rightHead, 0,0, null);
        graphics.drawImage(rightArm, 2, 8, null);
        graphics.drawImage(rightLeg, 2, 20, null);
        graphics.dispose();
        return avatarImage;
    }

    private BufferedImage getSmallSkinBufferedImage(int scale) {
        final BufferedImage avatarImage = new BufferedImage(19, 18,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = avatarImage.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(1, 0, 16, 9);
        graphics.fillRect(0, 8, 19, 5);
        graphics.fillRect(3, 13, 13, 5);
        final BufferedImage right = generateRight(61);
        final Graphics temp = right.getGraphics();
        temp.setColor(new Color(0, 0, 0, 156));
        temp.fillRect(0, 0, 8, 32);
        temp.dispose();
        graphics.drawImage(right.getSubimage(0, 0, 3, 8), 2,1, null);
        graphics.drawImage(right.getSubimage(5, 0, 3, 8), 5,1, null);
        graphics.drawImage(right.getSubimage(3, 17, 3, 3), 1,9, null);
        graphics.drawImage(right.getSubimage(3, 27, 3, 5), 4,12, null);
        graphics.drawImage(skinFront.getSubimage(4, 0, 8, 8), 8, 1, null);
        graphics.drawImage(skinFront.getSubimage(4, 8, 8, 4), 7, 9, null);
        graphics.drawImage(skinFront.getSubimage(2, 17, 3, 3), 4, 9, null);
        graphics.drawImage(skinFront.getSubimage(12, 17, 3, 3), 15, 9, null);
        graphics.drawImage(skinFront.getSubimage(4, 28, 4, 4), 7, 13, null);
        graphics.drawImage(skinFront.getSubimage(8, 28, 4, 4), 11, 13, null);
        graphics.setColor(new Color(0, 0, 0, 123));
        graphics.fillRect(7, 9, 8, 1);
        graphics.dispose();
        return scale(avatarImage, scale);
    }


    public static class Builder {
        private String playerName = null;
        private BufferedImage image = null;
        private Image.ColorType colorType = Image.ColorType.LEGACY;
        private int ascent = 0;
        private int scale = 1;
        private boolean isSlim = false;

        public Builder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = image;
            return this;
        }

        public Builder colorType(Image.ColorType colorType) {
            this.colorType = colorType;
            return this;
        }

        public Builder ascent(int ascent) {
            this.ascent = ascent;
            return this;
        }

        public Builder isSlim(boolean isSlim) {
            this.isSlim = isSlim;
            return this;
        }

        public Builder scale(int scale) {
            this.scale = scale;
            return this;
        }


        public Avatar build() {
            if (colorType == null) colorType = Image.ColorType.LEGACY;
            if (image != null)
                return new Avatar(image, scale, isSlim);
            else if (playerName != null)
                return new Avatar(playerName, scale, isSlim, colorType);
            return null;
        }
    }

}
