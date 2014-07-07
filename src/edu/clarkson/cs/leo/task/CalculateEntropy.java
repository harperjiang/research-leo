package edu.clarkson.cs.leo.task;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.leo.img.FeatureHelper;
import edu.clarkson.cs.leo.img.accessor.ImageAccessor;

public class CalculateEntropy {

	public static void main(String[] args) throws IOException {
		BufferedImage image1 = ImageIO.read(new File(
				"res/image/split/text_3.png"));
		BufferedImage image2 = ImageIO.read(new File(
				"res/image/split/logo_color.png"));
		System.out.println(FeatureHelper.entropy(new ImageAccessor(image1),
				new Rectangle(0, 0, image1.getWidth(), image1.getHeight())));
		System.out.println(FeatureHelper.entropy(new ImageAccessor(image2),
				new Rectangle(0, 0, image2.getWidth(), image2.getHeight())));
	}
}
