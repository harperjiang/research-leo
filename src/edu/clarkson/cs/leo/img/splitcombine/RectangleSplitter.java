package edu.clarkson.cs.leo.img.splitcombine;

import java.awt.Dimension;
import java.awt.Rectangle;

import edu.clarkson.cs.leo.img.accessor.ColorAccessor;

public class RectangleSplitter extends AbstractSplitter {

	public RectangleSplitter(ColorAccessor accessor) {
		super(new SplitCore(accessor));
	}

	public RectangleSplitter(SplitCore core) {
		super(core);
	}

	private static double countThreshold = 0.7;

	public Rectangle maxSplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, getAccessor().getWidth(), getAccessor()
					.getHeight());
		}
		long area = 0;
		Rectangle max = null;

		int wall = -1;
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
					Dimension topRight = core.preprocess[i + w][j];
					int lastValue = -1;
					int lastValueCount = 0;
					for (int h = topLeft.height - 1; h >= 0; h--) {
						if (w * h < area) {
							// fast break
							if (h == topLeft.height)
								w = 0;
							h = 0;
							continue;
						}
						if (lastValueCount >= countThreshold * range.height
								&& w > lastValue) {
							// A vertical wall is thought to be found, decrease
							// width to be behind the wall
							wall = lastValue;
							w = lastValue + 1;
							h = 0;
							continue;
						}
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
						} else {
							if (bottomLeft.width == lastValue) {
								lastValueCount++;
							} else {
								lastValue = bottomLeft.width;
								lastValueCount = 1;
							}

							if (topRight.height < topLeft.height
									&& topRight.height < h) {
								h = topRight.height + 1;
							}

						}
					}
				}

			}
			if (wall != -1) {
				i += wall;
				wall = -1;
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

}
