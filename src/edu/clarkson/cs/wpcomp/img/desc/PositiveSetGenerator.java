package edu.clarkson.cs.wpcomp.img.desc;

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
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.wpcomp.img.split.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class PositiveSetGenerator {

	public static void main(String[] args) throws Exception {
		// Get a picture and transform it by scaling it

		BufferedImage image = ImageIO.read(new File(
				"res/image/positive/logo.jpg"));
		BufferedImage gradient = GradientHelper.gradientImage(image, 30);
		ColorAccessor accessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(accessor);
		Rectangle range = splitter.lowerbound(null);
		BufferedImage croped = CropHelper.crop(image, range);

		PrintWriter pw = new PrintWriter(new FileOutputStream("positive"));

		HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);

		for (int size = 1000; size > 100; size -= 50) {
			BufferedImage scale = ImageTransformer.scale(croped, size, size);
			scale = ImageTransformer.scale(scale, 500, 500);
			Feature feature = hog.describe(new ImageAccessor(scale));
			pw.println(MessageFormat.format("{0} {1}", 1, feature));
		}
		pw.close();

	}
}
