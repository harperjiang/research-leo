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
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.Filter;
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.FilterResult;
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.SizeFilter;
import edu.clarkson.cs.wpcomp.img.splitcombine.filter.TextFilter;

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
		filters.add(new SizeFilter());
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
		List<Rectangle> premature = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		logger.debug("Start Split Loop:" + System.currentTimeMillis());
		while (!source.isEmpty()) {
			// for (int i = 0; i < 2; i++) {
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

				MaxSplitProcessor msp = new MaxSplitProcessor(fence, cenv);

				// Border Removal
				Rectangle removed = msp.removeBorder();
				if (!removed.equals(fence)) {
					result.add(removed);
					continue;
				}
				// Rectangle Split, applied to big image
				List<Rectangle> maxSplitResult = msp.process();
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
				premature.add(r);
			}

			source.clear();

			// Move rectangles from result to source;
			FilterResult filteredResult = filter(result);
			// Move rectangles from premature to mature
			FilterResult filteredMature = filter(premature);

			source.addAll(filteredResult.getAccepted());
			mature.addAll(filteredResult.getMatured());
			mature.addAll(filteredMature.getAccepted());
			mature.addAll(filteredMature.getMatured());

			premature.clear();
			result.clear();
		}

		logger.debug("Finishing Split Loop:" + System.currentTimeMillis());
		source.addAll(mature);
		return source;
	}

	public SplitEnv getCenv() {
		return cenv;
	}

	private FilterResult filter(List<Rectangle> input) {
		FilterResult result = new FilterResult();
		result.getAccepted().addAll(input);
		for (Filter filter : filters) {
			List<Rectangle> output = new ArrayList<Rectangle>();
			for (Rectangle r : result.getAccepted()) {
				FilterResult fr = filter.filter(r, getCenv());
				output.addAll(fr.getAccepted());
				result.getMatured().addAll(fr.getMatured());
			}
			result.getAccepted().clear();
			result.getAccepted().addAll(output);
		}
		return result;
	}

	private int[] searchBoxRange = { 15, 40 };

	private double searchBoxRatio = 5;

	private int searchBoxBorder = 5;

	private double searchBoxButtonRatio = 0.3d;
}
