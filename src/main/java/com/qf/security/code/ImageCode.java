package com.qf.security.code;

import java.awt.image.BufferedImage;

/**
 * @author 81958
 */
public class ImageCode extends ValidateCode {

    private BufferedImage image;

    public ImageCode(String code, int seconds, BufferedImage image) {
        super(code, seconds);
        this.image = image;
    }

    public ImageCode() {}

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
