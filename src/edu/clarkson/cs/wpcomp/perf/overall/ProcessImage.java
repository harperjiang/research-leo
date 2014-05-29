package edu.clarkson.cs.wpcomp.perf.overall;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.FeatureHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.splitcombine.Combine;
import edu.clarkson.cs.wpcomp.img.splitcombine.Split;

public class ProcessImage {

	public static void main(String[] args) throws Exception {
		DecimalFormat df = new DecimalFormat("00");
		long sum = 0;
		long max = 0;
		long min = Long.MAX_VALUE;
		for (int i = 0; i < 26; i++) {
			String subdir = df.format(i);
			long start = System.currentTimeMillis();
			process(new File("workdir/" + subdir));
			long val = System.currentTimeMillis() - start;
			sum += val;
			if (val > max)
				max = val;
			if (val < min)
				min = val;
		}
		System.out.println(((double) sum) / 26);
		System.out.println(max);
		System.out.println(min);
	}

	public static void process(File folder) throws Exception {
		System.out.println("Begin:" + System.currentTimeMillis());
		BufferedImage input = ImageIO.read(new File(folder.getAbsolutePath()
				+ "/screenshot.png"));
		System.out.println("Image Read:" + System.currentTimeMillis());
		Split split = new Split();
		Combine combine = new Combine();
		List<Rectangle> ranges = split.split(input);

		System.out.println("Splitted:" + System.currentTimeMillis());
		ColorAccessor accessor = new ImageAccessor(input);
		//BufferedImage gradient = GradientHelper.gradientImage(input, 20);
		//ImageIO.write(gradient, "png", new File(folder.getAbsolutePath()
		//		+ "/gradient.png"));
		//ColorAccessor ga = new ImageAccessor(gradient);

		List<Rectangle> combined = combine.combine(ranges);

		System.out.println("Combined:" + System.currentTimeMillis());
		for (Rectangle rect : combined) {
			if (FeatureHelper.entropy(accessor, rect) > 0.72) {
				MarkHelper.redrect(rect, accessor);
				//MarkHelper.redrect(rect, ga);
			}
		}

		System.out.println("Marked:" + System.currentTimeMillis());
		//ImageIO.write(input, "png", new File(folder.getAbsolutePath()
		//		+ "/split.png"));
		//ImageIO.write(gradient, "png", new File(folder.getAbsolutePath()
		//		+ "/gsplit.png"));

		System.out.println("Done:" + System.currentTimeMillis());

	}
}
