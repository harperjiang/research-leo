package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/bbc_screen.png"));

		BufferedImage gradient = GradientHelper.gradientImage(input);

		ColorAccessor accessor = new ImageAccessor(gradient);

		RectangleSplitter rect = new RectangleSplitter(accessor);
		LineSplitter line = new LineSplitter(accessor);

		Rectangle fence = rect.lowerbound(null);

		LineSegment lc = line.centralsplit(fence);
		if (null != lc)
			MarkHelper.redline(lc, accessor);

		Rectangle fence2 = null;
		if (lc.isHorizontal()) {
			fence2 = rect.lowerbound(new Rectangle(fence.x, fence.y,
					fence.width, lc.from.y - fence.y));
		}
		if (lc.isVertical()) {
			fence2 = rect.lowerbound(new Rectangle(fence.x, fence.y, lc.from.x
					- fence.x, fence.height));
		}
		LineSegment second = line.centralsplit(fence2);
		if (null != second) {
			MarkHelper.redline(second, accessor);
		}

		ImageIO.write(gradient, "png", new File("res/image/split/split.png"));
	}
}
