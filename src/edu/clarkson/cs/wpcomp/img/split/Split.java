package edu.clarkson.cs.wpcomp.img.split;

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

	private int level = 5;

	private List<Checker> checkers;

	private RectangleSplitter rect;

	private LineSplitter line;

	public Split() {
		super();
		checkers = new ArrayList<Checker>();
		checkers.add(new SizeChecker());
		checkers.add(new TextChecker());
	}

	public List<Rectangle> split(BufferedImage input) throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(input, 0);
		ColorAccessor accessor = new ImageAccessor(gradient);

		rect = new RectangleSplitter(accessor);
		line = new LineSplitter(accessor);

		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		source.add(new Rectangle(0, 0, accessor.getWidth(), accessor
				.getHeight()));

		int depth = level;

		for (int i = 0; i < depth; i++) {
			for (Rectangle r : source) {
				Rectangle fence = rect.lowerBound(r);
				if (fence == null)
					// Blank Rectangle
					continue;
				LineSegment lc = line.maxMarginSplit(fence);

				if (null == lc) {
					rect.removeBorder(fence);
					result.add(rect.removeBorder(fence));
					continue;
				}

				Rectangle[] split = GeometryHelper.split(fence, lc);

				Rectangle top = rect.lowerBound(split[0]);
				if (null != top)
					result.add(top);
				Rectangle bottom = rect.lowerBound(split[1]);
				if (null != bottom)
					result.add(bottom);
			}
			source = new ArrayList<Rectangle>();
			for (Rectangle r : result) {
				if (check(r))
					source.add(r);
			}
			result = new ArrayList<Rectangle>();
		}
		return source;
	}

	private boolean check(Rectangle r) {
		for (Checker checker : checkers) {
			if (!checker.check(r, rect, line))
				return false;
		}
		return true;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
