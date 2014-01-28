package edu.clarkson.cs.wpcomp.img.textdetect;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.GeometryHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.split.LineSegment;
import edu.clarkson.cs.wpcomp.img.split.LineSplitter;
import edu.clarkson.cs.wpcomp.img.split.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;

public class TextDetectMain {

	public static void main(String[] args) throws IOException {
		BufferedImage buffered = ImageIO.read(new File(
				"res/image/split/text_2.png"));
		ColorAccessor image = new ImageAccessor(buffered);
		BufferedImage gradient = GradientHelper.gradientImage(buffered, 10);
		ColorAccessor accessor = new ImageAccessor(gradient);
		LineSplitter line = new LineSplitter(accessor);
		RectangleSplitter rect = new RectangleSplitter(accessor);

		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		List<Rectangle> output = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		while (!source.isEmpty()) {
			for (Rectangle r : source) {
				Rectangle current = rect.lowerBound(r);
				if (null != current) {
					LineSegment split = line.maxMarginSplit(current, true);
					if (split != null) {
						Rectangle[] splitted = GeometryHelper.split(r, split);
						result.add(splitted[0]);
						result.add(splitted[1]);
					} else {
						output.add(current);
					}
				}
			}
			source = new ArrayList<Rectangle>();
			source.addAll(result);
			result = new ArrayList<Rectangle>();
		}

		TextImageDescriptor desc = new TextImageDescriptor();
		List<Feature> features = new ArrayList<Feature>();

		for (Rectangle r : output) {
			BufferedImage cropped = CropHelper.crop(gradient, r);
			BufferedImage scaled = ImageTransformer.scale(cropped,
					(int) (50 * (double) cropped.getWidth() / (double) cropped
							.getHeight()), 50);
			Feature feature = desc.describe(new ImageAccessor(scaled));
			features.add(feature);
		}

		PrintWriter pw = new PrintWriter(new File("res/svm/text/test"));
		for (Feature feature : features) {
			pw.println(MessageFormat.format("{0} {1}", 0, feature));
		}
		pw.close();
	}
}
