package edu.clarkson.cs.wpcomp.img.transform;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class ImageTransformation {

	public static void main(String[] args) throws Exception {
		// Read image
		BufferedImage image = ImageIO.read(new File("res/image/ebay_1.jpg"));

		BufferedImage[] variants = new BufferedImage[8];
		variants[0] = ImageTransformer.transform(
				image,
				ImageTransformer.centerScale(image.getWidth() / 2,
						image.getHeight() / 2, 1.25));
		variants[1] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(-10, 0));
		variants[2] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(-10, 10));
		variants[3] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(0, 10));
		variants[4] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(10, 10));
		variants[5] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(10, 0));
		variants[6] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(10, -10));
		variants[7] = ImageTransformer.transform(image,
				AffineTransform.getTranslateInstance(0, -10));

		for (int i = 0; i < variants.length; i++) {
			ImageIO.write(variants[i], "png", new File(
					"res/image/ebay_1_output_" + i + ".png"));
		}
	}
}
