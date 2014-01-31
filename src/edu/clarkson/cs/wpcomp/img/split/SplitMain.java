package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/phishing.png"));
		Split split = new Split();
		split.setLevel(9);
		List<Rectangle> ranges = split.split(input);

		ColorAccessor accessor = new ImageAccessor(input);
		BufferedImage gradient = GradientHelper.gradientImage(input, 0);
		ColorAccessor ga = new ImageAccessor(gradient);
		for (Rectangle rect : ranges) {
			System.out.println(rect);
			MarkHelper.redrect(rect, accessor);
			MarkHelper.redrect(rect, ga);
		}
		ImageIO.write(input, "png", new File(
				"res/image/split/phishing_split.png"));
		ImageIO.write(gradient, "png", new File(
				"res/image/split/phishing_gsplit.png"));
	}
}
