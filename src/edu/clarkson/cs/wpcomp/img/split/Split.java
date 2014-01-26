package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class Split {

	private int level = 5;

	private int widthThreshold = 5;

	private int heightThreshold = 5;

	private int areaThreshold = 100;

	public List<Rectangle> split(BufferedImage input) throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(input, 0);
		ColorAccessor accessor = new ImageAccessor(gradient);

		RectangleSplitter rect = new RectangleSplitter(accessor);
		LineSplitter line = new LineSplitter(accessor);

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
				LineSegment lc = line.maxmarginsplit(fence);

				if (null == lc) {
					rect.removeBorder(fence);
					result.add(rect.removeBorder(fence));
					continue;
				}

				if (lc.isHorizontal()) {
					Rectangle top = rect.lowerBound(new Rectangle(fence.x,
							fence.y, fence.width, lc.from.y - fence.y));
					if (null != top)
						result.add(top);
					Rectangle bottom = rect.lowerBound(new Rectangle(fence.x,
							lc.from.y, fence.width, fence.y + fence.height
									- lc.from.y));
					if (null != bottom)
						result.add(bottom);
				}
				if (lc.isVertical()) {
					Rectangle left = rect.lowerBound(new Rectangle(fence.x,
							fence.y, lc.from.x - fence.x, fence.height));
					if (null != left)
						result.add(left);
					Rectangle right = rect.lowerBound(new Rectangle(lc.from.x,
							fence.y, fence.x + fence.width - lc.from.x,
							fence.height));
					if (null != right)
						result.add(right);
				}
			}
			source = result;
			result = new ArrayList<Rectangle>();
		}

		for (Rectangle r : source) {
			if (check(r))
				result.add(r);
		}
		return result;
	}

	private boolean check(Rectangle r) {
		return r.width > widthThreshold && r.height > heightThreshold
				&& r.width * r.height > areaThreshold;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
