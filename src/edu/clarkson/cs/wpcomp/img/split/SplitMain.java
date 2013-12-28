package edu.clarkson.cs.wpcomp.img.split;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/screen.png"));

		BufferedImage gradient = GradientHelper.gradientImage(input);

		ImageIO.write(gradient, "png", new File("res/image/split/gradient.png"));
	}
}
