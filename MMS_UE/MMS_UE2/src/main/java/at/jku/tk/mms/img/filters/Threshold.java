package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/**
 * Filter that implements image thresholding
 */
public class Threshold implements FilterInterface {


    @Override
    public Image runFilter(BufferedImage image, Properties settings) {
        int threshold = Integer.parseInt(settings.getProperty("threshold"));

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        //some Vanilla Javascript
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Pixel px = new Pixel(image.getRGB(i, j));
                int color = (px.getR() + px.getB() + px.getG()) / 3;
                px.setR(color > threshold ? 255 : 0);
                px.setB(color > threshold ? 255 : 0);
                px.setG(color > threshold ? 255 : 0);
                result.setRGB(i, j, px.getRawRGBA());
            }
        }
        return result;
    }

    @Override
    public String[] mandatoryProperties() {
        return new String[]{"threshold:n:0-255:128"};
    }

    @Override
    public String toString() {
        return "Threshold Filter";
    }

}
