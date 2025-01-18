package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/**
 * Perform sub sampling on the image
 */
public class Subsampling implements FilterInterface {

    @Override
    public Image runFilter(BufferedImage image, Properties settings) {
        int rate = Integer.parseInt(settings.getProperty("rate"));

        //builds blocks
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                bi.setRGB(i, j, image.getRGB((i / rate) * rate, (j / rate) * rate));
            }
        }

        // creates a smaller image
        /*

        BufferedImage bi = new BufferedImage(image.getWidth()/rate, image.getHeight()/rate, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i<image.getWidth()/rate;i++){
            for (int j = 0; j<image.getHeight()/rate;j++){
                bi.setRGB(i,j,image.getRGB(i*rate,j*rate));
            }
        }
        */
        return bi;
    }

    @Override
    public String[] mandatoryProperties() {
        return new String[]{"rate:n:1-8:2"};
    }

    @Override
    public String toString() {
        return "subsampling";
    }

}
