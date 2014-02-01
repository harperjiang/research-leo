package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.GeometryHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class Split {

	private List<Filter> filters;

	private RectangleSplitter rect;

	private LineSplitter line;

	private SplitEnv cenv;

	public Split() {
		super();
		filters = new ArrayList<Filter>();
		filters.add(new SizeFilter());
		filters.add(new TextFilter());
	}

	public List<Rectangle> split(BufferedImage input) throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(input, 20);
		ColorAccessor accessor = new ImageAccessor(gradient);

		rect = new RectangleSplitter(accessor);
		line = new LineSplitter(accessor);

		cenv = new SplitEnv();
		cenv.lineSplitter = line;
		cenv.rectSplitter = rect;
		cenv.sourceImage = gradient;

		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		List<Rectangle> mature = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		while (!source.isEmpty()) {
			for (Rectangle r : source) {
				Rectangle fence = rect.lowerBound(r);
				if (fence == null)
					// Blank Rectangle
					continue;

				// Linear split
				LineSegment lc = line.maxMarginSplit(fence);
				if (null != lc) {
					Rectangle[] split = GeometryHelper.split(fence, lc);
					Rectangle top = rect.lowerBound(split[0]);
					Rectangle bottom = rect.lowerBound(split[1]);

					if (null != top)
						result.add(top);
					if (null != bottom)
						result.add(bottom);

					continue;
				}
				// Border Removal
				Rectangle removed = rect.removeBorder(fence);
				if (!removed.equals(fence)) {
					result.add(removed);
					continue;
				}
				// Rectangle Split, applied to big image
				List<Rectangle> maxSplitResult = new MaxSplitProcessor()
						.process(fence, cenv);
				if (maxSplitResult != null) {
					result.addAll(maxSplitResult);
					continue;
				}

				// Rectangle split, applied to images like search box
				if (fence.height >= searchBoxRange[0]
						&& fence.height <= searchBoxRange[1]
						&& (fence.getWidth() / fence.getHeight()) >= searchBoxRatio) {
					Rectangle rectsplit = rect.maxSplit(fence);
					if (null != rectsplit) {
						if (rectsplit.x - fence.x <= searchBoxBorder
								&& rectsplit.y - fence.y <= searchBoxBorder
								&& fence.y + fence.height - rectsplit.x
										- rectsplit.height <= searchBoxBorder
								&& fence.x + fence.width - rectsplit.x
										- rectsplit.width < fence.width
										* searchBoxButtonRatio) {
							result.add(rectsplit);
							continue;
						}
					}
				}

				// All methods tried, this area is thought to be non-splittable
				mature.add(fence);
			}
			source = new ArrayList<Rectangle>();
			for (Rectangle r : result) {
				if (filter(r)) {
					source.add(r);
				}
			}
			result = new ArrayList<Rectangle>();
		}

		source.addAll(mature);
		return source;
	}

	private boolean filter(Rectangle r) {
		for (Filter filter : filters) {
			if (!filter.filter(r, cenv))
				return false;
		}
		return true;
	}

	private int[] searchBoxRange = { 15, 40 };

	private double searchBoxRatio = 5;

	private int searchBoxBorder = 5;

	private double searchBoxButtonRatio = 0.3d;
}
