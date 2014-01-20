package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Dimension;
import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class RectangleSplitter extends AbstractSplitter {

	public RectangleSplitter(ColorAccessor accessor) {
		super(accessor);
	}

	public Rectangle maxsplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, accessor.getWidth(),
					accessor.getHeight());
		}
		long area = 0;
		Rectangle max = new Rectangle();
		for (int i = range.x; i < range.x + range.width; i++) {
			for (int j = range.y; j < range.y + range.height; j++) {
				Dimension topLeft = new Dimension(preprocess[i][j]);

				// Take bound into consideration
				topLeft.width = Math.min(topLeft.width, range.x + range.width
						- i);
				topLeft.height = Math.min(topLeft.height, range.y
						+ range.height - j);
				long possible = topLeft.width * topLeft.height;
				for (int w = topLeft.width; w > 0; w--) {
					for (int h = topLeft.height; h > 0; h--) {
						if (w * h < area) {
							// fast break
							if (h == topLeft.height)
								w = 0;
							h = 0;
							continue;
						}
						Dimension topRight = preprocess[i + w][j];
						Dimension bottomLeft = preprocess[i][j + h];
						if (topRight.height >= topLeft.height
								&& bottomLeft.width >= topLeft.width) {
							// Rectangle
							long curarea = w * h;
							if (curarea > area) {
								area = curarea;
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

	public Rectangle centralsplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, accessor.getWidth(),
					accessor.getHeight());
		}
		long center = (range.width * range.height) / 2;
		long area = center;
		Rectangle max = new Rectangle();
		for (int i = range.x; i < range.x + range.width; i++) {
			for (int j = range.y; j < range.y + range.height; j++) {
				Dimension topLeft = new Dimension(preprocess[i][j]);
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
						Dimension topRight = preprocess[i + w][j];
						Dimension bottomLeft = preprocess[i][j + h];
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

	public Rectangle lowerbound(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, accessor.getWidth(),
					accessor.getHeight());
		}
		Rectangle lowerbound = new Rectangle(range);
		Dimension bound = range.getSize();
		Dimension expansion = preprocess[range.x][range.y];
		bound.width = Math.min(bound.width, expansion.width);
		bound.height = Math.min(bound.height, expansion.height);
		int i = 0;
		for (i = 0; i < range.width; i++) {
			if (preprocess[range.x + i][range.y].height < bound.height)
				break;
		}
		lowerbound.x += i - 1;
		lowerbound.width -= i - 1;
		for (i = range.width - 1; i > 0; i--) {
			if (preprocess[range.x + i][range.y].height < bound.height)
				break;
		}
		lowerbound.width -= range.width - 1 - i;
		for (i = 0; i < range.height; i++) {
			if (preprocess[range.x][range.y + i].width < bound.width)
				break;
		}
		lowerbound.y += i - 1;
		lowerbound.height -= i - 1;
		for (i = 0; i < range.height; i++) {
			if (preprocess[range.x][range.y + range.height - 1 - i].width < bound.width) {
				break;
			}
		}
		lowerbound.height -= i - 1;

		return lowerbound;
	}
}
