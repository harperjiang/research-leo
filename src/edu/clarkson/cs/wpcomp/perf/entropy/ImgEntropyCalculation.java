package edu.clarkson.cs.wpcomp.perf.entropy;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.FeatureHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.splitcombine.RectangleSplitter;

public class ImgEntropyCalculation {

	public static void main(String[] args) throws Exception {
		Random random = new Random(System.currentTimeMillis());
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/image_acc/entropy_image"));
		File[] files = new File(
				"/home/harper/Research/webpage-comparison/imageset_test")
				.listFiles();
		for (int i = 0; i < 1000; i++) {
			try {
				BufferedImage image = ImageIO.read(files[random
						.nextInt(files.length)]);
				double entropy = generateEntropy(image);
				pw.println(MessageFormat.format("{0,number,##0.0000}", entropy));
			} catch (Exception e) {
				// Ignore
			}
		}
		pw.close();
	}

	private static Double generateEntropy(BufferedImage image)
			throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(image, 20);
		ColorAccessor gaccessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(gaccessor);
		Rectangle lower = splitter.lowerBound(null);
		return FeatureHelper.entropy(new ImageAccessor(image), lower);
	}
}
