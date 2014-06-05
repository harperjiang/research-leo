package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.cs.wpcomp.img.GeometryHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class Split {

	private int gradientThreshold = 30;

	private List<Filter> filters;

	private RectangleSplitter rect;

	private LineSplitter line;

	private SplitEnv cenv;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public Split() {
		super();
		filters = new ArrayList<Filter>();
		// filters.add(new SizeFilter());
		// filters.add(new EntropyFilter());
		filters.add(new TextFilter());
	}

	public List<Rectangle> split(BufferedImage input) throws IOException {
		logger.debug("Gradienting Image:" + System.currentTimeMillis());
		BufferedImage gradient = GradientHelper.gradientImage(input,
				gradientThreshold);
		ColorAccessor accessor = new ImageAccessor(gradient);
		logger.debug("Generating Split Core:" + System.currentTimeMillis());
		SplitCore core = new SplitCore(accessor);

		logger.debug("Prepare Split Environment:" + System.currentTimeMillis());
		rect = new RectangleSplitter(core);
		line = new LineSplitter(core);

		cenv = new SplitEnv();
		cenv.lineSplitter = line;
		cenv.rectSplitter = rect;
		cenv.sourceImage = gradient;

		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		List<Rectangle> mature = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		logger.debug("Start Split Loop:" + System.currentTimeMillis());
		while (!source.isEmpty()) {
			// for (int i = 0; i < 7; i++) {
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

		logger.debug("Finishing Split Loop:" + System.currentTimeMillis());
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

	public SplitEnv getCenv() {
		return cenv;
	}

	private int[] searchBoxRange = { 15, 40 };

	private double searchBoxRatio = 5;

	private int searchBoxBorder = 5;

	private double searchBoxButtonRatio = 0.3d;
}
