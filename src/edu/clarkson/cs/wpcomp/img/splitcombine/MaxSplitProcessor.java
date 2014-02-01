package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.GeometryHelper;

public class MaxSplitProcessor implements Processor {

	private int widthThreshold = 500;

	private int heightThreshold = 500;

	private double splitThreshold = 0.1;

	@Override
	public List<Rectangle> process(Rectangle range, SplitEnv env) {
		List<Rectangle> result = new ArrayList<Rectangle>();
		if (range.getWidth() > widthThreshold
				&& range.getHeight() > heightThreshold) {
			// Only apply rectangular split to big image
			Rectangle rectSplit = env.rectSplitter.maxSplit(range);
			double areaThreshold = GeometryHelper.area(range) * splitThreshold;
			if (null != rectSplit
					&& GeometryHelper.area(rectSplit) > areaThreshold) {
				result.add(rectSplit);

				Rectangle[] border = new Rectangle[4];
				border[0] = env.rectSplitter.maxSplit(new Rectangle(range.x,
						range.y, range.width, rectSplit.y - range.y));
				border[1] = env.rectSplitter
						.maxSplit(new Rectangle(range.x, rectSplit.y
								+ rectSplit.height, range.width, range.y
								+ range.height - rectSplit.y - rectSplit.height));
				border[2] = env.rectSplitter.maxSplit(new Rectangle(range.x,
						range.y, rectSplit.x - range.x, range.height));
				border[3] = env.rectSplitter.maxSplit(new Rectangle(rectSplit.x
						+ rectSplit.width, range.y, range.x + range.width
						- rectSplit.x - rectSplit.width, range.height));
				// I think overlap doesn't matter in this case.
				for (Rectangle r : border) {
					if (GeometryHelper.area(r) > areaThreshold) {
						result.add(r);
					}
				}
			}
			return result;
		} else {
			return null;
		}
	}
}
