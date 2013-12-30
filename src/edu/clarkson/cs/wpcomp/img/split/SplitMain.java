package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/bbc_screen.png"));

		BufferedImage gradient = GradientHelper.gradientImage(input);

		// ImageIO.write(gradient, "png", new
		// File("res/image/split/gradient.png"));
		ImageSplitter splitter = new ImageSplitter(gradient);

		Rectangle max = splitter.maxsplit(null);
		Rectangle lower = splitter.lowerbound(max);

		Rectangle center = splitter.centralsplit(new Rectangle(lower.x + 1,
				lower.y, lower.width - 1, lower.height));
		MarkHelper.redrect(center, splitter.getAccessor());
		ImageIO.write(gradient, "png", new File("res/image/split/split.png"));
	}
}
