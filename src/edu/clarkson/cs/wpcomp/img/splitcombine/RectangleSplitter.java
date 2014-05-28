package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Dimension;
import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class RectangleSplitter extends AbstractSplitter {

	public RectangleSplitter(ColorAccessor accessor) {
		super(new SplitCore(accessor));
	}

	public RectangleSplitter(SplitCore core) {
		super(core);
	}

	public Rectangle maxSplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, getAccessor().getWidth(), getAccessor()
					.getHeight());
		}
		long area = 0;
		Rectangle max = null;
		for (int i = range.x; i < range.x + range.width; i++) {
			for (int j = range.y; j < range.y + range.height; j++) {
				Dimension topLeft = new Dimension(core.preprocess[i][j]);

				// Take bound into consideration
				topLeft.width = Math.min(topLeft.width, range.x + range.width
						- i);
				topLeft.height = Math.min(topLeft.height, range.y
						+ range.height - j);
				long possible = topLeft.width * topLeft.height;
				for (int w = topLeft.width - 1; w >= 0; w--) {
					for (int h = topLeft.height - 1; h >= 0; h--) {
						if (w * h < area) {
							// fast break
							if (h == topLeft.height)
								w = 0;
							h = 0;
							continue;
						}
						Dimension topRight = core.preprocess[i + w][j];
						Dimension bottomLeft = core.preprocess[i][j + h];
						if (topRight.height >= topLeft.height
								&& bottomLeft.width >= topLeft.width) {
							// Rectangle
							long curarea = w * h;
							if (curarea > area) {
								area = curarea;
								max = new Rectangle(i, j, w, h);
							}
							if (curarea == possible) {
								// Fast break
								w = 0;
								h = 0;
							}
						}
					}
				}

			}
		}
		return max;
	}

	public Rectangle centralSplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, getAccessor().getWidth(), getAccessor()
					.getHeight());
		}
		long center = (range.width * range.height) / 2;
		long area = center;
		Rectangle max = new Rectangle();
		for (int i = range.x; i < range.x + range.width; i++) {
			for (int j = range.y; j < range.y + range.height; j++) {
				Dimension topLeft = new Dimension(core.preprocess[i][j]);
				// Take bound into consideration
				topLeft.width = Math.min(topLeft.width, range.x + range.width
						- i);
				topLeft.height = Math.min(topLeft.height, range.y
						+ range.height - j);
				long possible = topLeft.width * topLeft.height;
				for (int w = topLeft.width; w > 0; w--) {
					for (int h = topLeft.height; h > 0; h--) {
						if (center - w * h >= area) {
							// fast break
							h = 0;
							if (h == topLeft.height) {
								w = 0;
							}
							continue;
						}
						Dimension topRight = core.preprocess[i + w][j];
						Dimension bottomLeft = core.preprocess[i][j + h];
						if (topRight.height >= topLeft.height
								&& bottomLeft.width >= topLeft.width) {
							// Rectangle
							long curarea = w * h;
							if (Math.abs(center - curarea) < area) {
								area = Math.abs(center - curarea);
								max.x = i;
								max.y = j;
								max.width = w;
								max.height = h;
							}
							if (curarea == possible) {
								// Fast break
								w = 0;
								h = 0;
							}
						}
					}
				}

			}
		}
		return max;
	}

	public Rectangle lowerBound(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, getAccessor().getWidth(), getAccessor()
					.getHeight());
		}
		Rectangle lowerbound = new Rectangle(range);
		Dimension bound = range.getSize();

		Dimension lefttop = core.preprocess[range.x][range.y];
		Dimension righttop = core.preprocess[range.x + range.width - 1][range.y];
		Dimension leftbottom = core.preprocess[range.x][range.y + range.height
				- 1];

		int i = 0;
		if (lefttop.height >= bound.height) {
			for (i = 0; i < range.width; i++) {
				if (core.preprocess[range.x + i][range.y].height < bound.height)
					break;
			}
			if (i == range.width) {
				// Blank rectangle
				return null;
			}
			lowerbound.x += i - 1;
			lowerbound.width -= i - 1;
		}
		if (righttop.height >= bound.height) {
			for (i = range.width - 1; i > 0; i--) {
				if (core.preprocess[range.x + i][range.y].height < bound.height)
					break;
			}
			lowerbound.width -= range.width - 2 - i;
		}
		if (lefttop.width >= bound.width) {
			for (i = 0; i < range.height; i++) {
				if (core.preprocess[range.x][range.y + i].width < bound.width)
					break;
			}
			if (i == range.height)
				return null;
			lowerbound.y += i - 1;
			lowerbound.height -= i - 1;
		}
		if (leftbottom.width >= bound.width) {
			for (i = 0; i < range.height; i++) {
				if (core.preprocess[range.x][range.y + range.height - 1 - i].width < bound.width) {
					break;
				}
			}
			lowerbound.height -= i - 1;
		}
		return lowerbound;
	}

	public Rectangle removeBorder(Rectangle range) {
		// Detect the existance of border
		// Here we define border to be a rectangle with most pixels on the
		// border being colored
		/*
		 * Rectangle test = new Rectangle(range.x + 1, range.y + 1, range.width
		 * - 2, range.height - 2);
		 * 
		 * int xcounter = 0; for (int i = test.x; i < test.x + test.width; i++)
		 * { // If black point exceeds the threshold, determine not a // border
		 * if (Color.BLACK.equals(accessor.getValue(i, test.y))) xcounter++; if
		 * (Color.BLACK.equals(accessor.getValue(i, test.y + test.height - 1)))
		 * xcounter++; } if (test.width * borderThreshold < xcounter) { return
		 * range; } int ycounter = 0; for (int i = test.y; i < test.y +
		 * test.height; i++) { if (Color.BLACK.equals(accessor.getValue(test.x,
		 * i))) ycounter++; if (Color.BLACK.equals(accessor .getValue(test.x +
		 * test.width - 1, i))) ycounter++; } if (test.height * borderThreshold
		 * < ycounter) { return range; }
		 */
		Rectangle maxSplit = maxSplit(range);
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

	private int borderThreshold = 6;
}
