package edu.clarkson.cs.wpcomp.img.textdetect;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.split.LineSegment;
import edu.clarkson.cs.wpcomp.img.split.LineSplitter;

public class TextDetectMain {

	public static void main(String[] args) throws IOException {
		BufferedImage buffered = ImageIO.read(new File(
				"res/image/split/text_2.png"));
		ColorAccessor image = new ImageAccessor(buffered);
		BufferedImage gradient = GradientHelper.gradientImage(buffered, 10);
		ColorAccessor accessor = new ImageAccessor(gradient);
		LineSplitter line = new LineSplitter(accessor);

		Rectangle start = null;
		while (true) {
			LineSegment split = line.maxMarginSplit(start, true);
			MarkHelper.redline(split, image);

			break;
		}
		ImageIO.write(buffered, "png", new File(
				"res/image/split/text_split.png"));
	}

}
