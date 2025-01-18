package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/**
 * Just to show correct handles in ComboBox
 */
public class SepiaFilter implements FilterInterface {

    @Override
    public Image runFilter(BufferedImage image, Properties settings) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        //some Vanilla Javascript
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Pixel px = new Pixel(image.getRGB(i, j));
                int sepiaRed = (int) ((px.getR() * 0.393) + (px.getG() * 0.769) + (px.getB() * 0.189));
                int sepiaGreen = (int) ((px.getR() * 0.349) + (px.getG() * 0.686) + (px.getB() * 0.168));
                int sepiaBlue = (int) ((px.getR() * 0.272) + (px.getG() * 0.534) + (px.getB() * 0.131));
                px.setR(Math.min(sepiaRed, 255));
                px.setB(Math.min(sepiaBlue, 255));
                px.setG(Math.min(sepiaGreen, 255));
                result.setRGB(i, j, px.getRawRGBA());
            }
        }
        return result;
    }

    @Override
    public String[] mandatoryProperties() {
        return new String[]{};
    }

    @Override
    public String toString() {
        return "Sepia Filter";
    }

}
