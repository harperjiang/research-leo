package edu.clarkson.cs.leo.img;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ImageHelper {

	public static BufferedImage crop(BufferedImage image, Rectangle range) {
		BufferedImage cropedImage = new BufferedImage(range.width,
				range.height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < range.width; i++) {
			for (int j = 0; j < range.height; j++) {
				cropedImage
						.setRGB(i, j, image.getRGB(range.x + i, range.y + j));
			}
		}
		return cropedImage;
	}

}
