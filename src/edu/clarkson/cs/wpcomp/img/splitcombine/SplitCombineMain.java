package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.FeatureHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class SplitCombineMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/phishing.png"));
		Split split = new Split();
		Combine combine = new Combine();
		List<Rectangle> ranges = split.split(input);

		ColorAccessor accessor = new ImageAccessor(input);
		BufferedImage gradient = GradientHelper.gradientImage(input, 20);
		ImageIO.write(gradient, "png", new File(
				"res/image/split/phishing_gradient.png"));
		ColorAccessor ga = new ImageAccessor(gradient);

		List<Rectangle> combined = combine.combine(ranges);

		for (Rectangle rect : combined) {
			System.out.println(FeatureHelper.entropy(accessor, rect));
			if (FeatureHelper.entropy(accessor, rect) > 1) {
				MarkHelper.redrect(rect, accessor);
				MarkHelper.redrect(rect, ga);
			}
		}
		ImageIO.write(input, "png", new File(
				"res/image/split/phishing_split.png"));
		ImageIO.write(gradient, "png", new File(
				"res/image/split/phishing_gsplit.png"));
	}
}
