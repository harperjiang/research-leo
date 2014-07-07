package edu.clarkson.cs.leo.task;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.leo.img.FeatureHelper;
import edu.clarkson.cs.leo.img.GradientHelper;
import edu.clarkson.cs.leo.img.MarkHelper;
import edu.clarkson.cs.leo.img.accessor.ColorAccessor;
import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.splitcombine.Combine;
import edu.clarkson.cs.leo.img.splitcombine.Split;

public class SplitAndCombine {

	public static void main(String[] args) throws Exception {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/phishing.png"));
		SplitAndCombine.work(input);
	}

	public static void work(BufferedImage input) throws Exception {
		Split split = new Split();
		Combine combine = new Combine();

		List<Rectangle> ranges = split.split(input);

		ColorAccessor accessor = new ImageAccessor(input);
		BufferedImage gradient = GradientHelper.gradientImage(input, 20);
		
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
