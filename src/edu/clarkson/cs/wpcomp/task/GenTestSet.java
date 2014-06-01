package edu.clarkson.cs.wpcomp.task;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.desc.descriptor.HogSVMDescriptor;
import edu.clarkson.cs.wpcomp.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class GenTestSet {

	public static void main(String[] args) throws Exception {

	}

	public static void generate(List<Input> inputImages, File output)
			throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(output));
		HogSVMDescriptor hog = new HogSVMDescriptor(50, 1);
		for (Input inputImage : inputImages) {
			BufferedImage image = inputImage.getImage();
			BufferedImage gradient = GradientHelper.gradientImage(image, 30);
			ColorAccessor accessor = new ImageAccessor(gradient);
			RectangleSplitter splitter = new RectangleSplitter(accessor);
			Rectangle range = splitter.lowerBound(null);

			BufferedImage cropped = CropHelper.crop(image, range);
			BufferedImage scale = ImageTransformer.scale(cropped, 500, 500);
			Feature feature = hog.describe(new ImageAccessor(scale));
			pw.println(MessageFormat.format("{0} {1}", 1, feature));

		}
		pw.close();
	}

}
