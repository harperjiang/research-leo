package edu.clarkson.cs.wpcomp.task.imgdesc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.wpcomp.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class TestSetGenerator {

	public static void main(String[] args) throws Exception {
		// Get a picture and transform it by scaling it
		BufferedImage goodImage = ImageIO.read(new File(
				"res/image/svm/test/good.jpg"));
		BufferedImage badImage = ImageIO.read(new File(
				"res/image/svm/test/bad.png"));

		HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);

		BufferedImage croped = prepare(goodImage);

		PrintWriter pw = new PrintWriter(new FileOutputStream("test_good"));
		for (int size = 1000; size > 100; size -= 10) {
			BufferedImage scale = ImageTransformer.scale(croped, size, size);
			scale = ImageTransformer.scale(scale, 500, 500);
			Feature feature = hog.describe(new ImageAccessor(scale));
			pw.println(MessageFormat.format("{0} {1}", 2, feature));
		}
		pw.close();

		croped = prepare(badImage);
		pw = new PrintWriter(new FileOutputStream("test_bad"));
		for (int size = 1000; size > 100; size -= 10) {
			BufferedImage scale = ImageTransformer.scale(croped, size, size);
			scale = ImageTransformer.scale(scale, 500, 500);
			Feature feature = hog.describe(new ImageAccessor(scale));
			pw.println(MessageFormat.format("{0} {1}", 2, feature));
		}
		pw.close();
	}

	static BufferedImage prepare(BufferedImage image) {
		BufferedImage gradient = GradientHelper.gradientImage(image, 30);
		ColorAccessor accessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(accessor);
		Rectangle range = splitter.lowerBound(null);
		BufferedImage croped = CropHelper.crop(image, range);
		return croped;
	}
}
