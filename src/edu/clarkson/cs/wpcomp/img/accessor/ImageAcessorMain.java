package edu.clarkson.cs.wpcomp.img.accessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;

public class ImageAcessorMain {

	public static void main(String[] args) throws IOException {
		BufferedImage image = ImageIO
				.read(new File("res/image/logo/ebay_1.jpg"));
		BufferedImage gradient = GradientHelper.gradientImage(image);

		ImageAccessor accessor = new ImageAccessor(gradient);

		for (int i = 0; i < accessor.getWidth(); i++) {
			for (int j = 0; j < accessor.getHeight(); j++) {
				accessor.setValue(i, j, Color.RED);
			}
		}
		ImageIO.write(gradient, "png", new File("res/image/output.png"));
	}
}
