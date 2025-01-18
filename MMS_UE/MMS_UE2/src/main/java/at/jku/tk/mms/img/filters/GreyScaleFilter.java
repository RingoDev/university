package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.image.*;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/** Just to show correct handles in ComboBox */
public class GreyScaleFilter implements FilterInterface{


	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		//some Vanilla Javascript
		for(int i = 0; i<image.getWidth();i++){
			for (int j = 0; j<image.getHeight();j++){
				Pixel px = new Pixel(image.getRGB(i,j));
				int color = (px.getR() +px.getB() +px.getG()) /3;
				px.setR(color);
				px.setB(color);
				px.setG(color);
				result.setRGB(i,j,px.getRawRGBA());
			}
		}
		return result;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { };
	}

	@Override
	public String toString() {
		return "Grey Filter";
	}

}
