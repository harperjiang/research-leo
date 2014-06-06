package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.GeometryHelper;

public class MaxSplitProcessor implements Processor {

	private static int widthThreshold = 200;

	private static int heightThreshold = 200;

	private static double splitThreshold = 0.1;

	private static int borderThreshold = 6;

	private Rectangle range;

	private SplitEnv env;

	private transient Rectangle maxSplit;

	public MaxSplitProcessor(Rectangle range, SplitEnv env) {
		super();
		this.range = range;
		this.env = env;
	}

	public Rectangle maxSplit() {
		if (null == maxSplit) {
			maxSplit = env.rectSplitter.maxSplit(range);
		}
		return maxSplit;
	}

	public Rectangle removeBorder() {
		Rectangle maxSplit = maxSplit();
		if (maxSplit == null) {
			return range;
		}
		if (maxSplit.x - range.x <= borderThreshold
				&& maxSplit.y - range.y <= borderThreshold
				&& (range.x + range.width - maxSplit.x - maxSplit.width) <= borderThreshold
				&& (range.y + range.height - maxSplit.y - maxSplit.height) <= borderThreshold)
			return maxSplit;
		return range;
	}

	public List<Rectangle> process() {
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
