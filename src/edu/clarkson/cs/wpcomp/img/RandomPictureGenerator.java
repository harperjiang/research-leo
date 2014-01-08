package edu.clarkson.cs.wpcomp.img;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomPictureGenerator {

	public static BufferedImage generate(Dimension size) {
		BufferedImage image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_RGB);
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				image.setRGB(i, j,
						new Color(random.nextInt(256), random.nextInt(256),
								random.nextInt(256)).getRGB());
			}
		}
		return image;
	}

}
