package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		int depth = 5;

		for (int i = 0; i < depth; i++) {
			for (Rectangle r : source) {
				Rectangle fence = rect.lowerbound(r);
				LineSegment lc = line.centralsplit(fence);
				if (lc != null) {
					if (lc.isHorizontal()) {
						result.add(rect.lowerbound(new Rectangle(fence.x,
								fence.y, fence.width, lc.from.y - fence.y)));
						result.add(rect.lowerbound(new Rectangle(fence.x,
								lc.from.y, fence.width, fence.y + fence.height
										- lc.from.y)));
					}
					if (lc.isVertical()) {
						result.add(rect.lowerbound(new Rectangle(fence.x,
								fence.y, lc.from.x - fence.x, fence.height)));
						result.add(rect.lowerbound(new Rectangle(lc.from.x,
								fence.y, fence.x + fence.width - lc.from.x,
								fence.height)));
					}
					MarkHelper.redline(lc, accessor);
				}
			}
			source = result;
			result = new ArrayList<Rectangle>();
		}

		ImageIO.write(gradient, "png", new File("res/image/split/split.png"));
	}
}
