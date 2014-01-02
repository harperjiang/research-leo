package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Point;
import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class LineSplitter extends AbstractSplitter {

	public LineSplitter(ColorAccessor accessor) {
		super(accessor);
	}

	public LineSegment centralsplit(Rectangle range) {
		return split(range, new SplitCondition() {

			@Override
			public boolean satisfy(int point) {
				if (isHorizontal()) {
					return preprocess[range.x][point].width >= range.width;
				} else {
					return preprocess[point][range.y].height >= range.height;
				}
			}

			@Override
			public int bias(int point) {
				if (isHorizontal()) {
					return Math.abs(point - (range.y + range.height / 2));
				} else {
					return Math.abs(point - (range.x + range.width / 2));
				}
			}

			@Override
			public int fastbreak(int point, int maxbias) {
				if (isHorizontal()) {
					return (point - (range.y + range.height / 2)) > maxbias ? (range.y
							+ range.height - 1)
							: point;
				} else {
					return (point - (range.x + range.width / 2)) > maxbias ? (range.x
							+ range.width - 1)
							: point;
				}
			}

			@Override
			public int postprocess(int point) {
				if (isHorizontal()) {
					int top, bottom;
					for (top = point; top > 0
							&& preprocess[range.x][top].width >= preprocess[range.x][point].width; top--)
						;
					for (bottom = point; bottom < range.y + range.height - 1
							&& preprocess[range.x][bottom].width >= preprocess[range.x][point].width; bottom++)
						;
					return (top + bottom) / 2;
				} else {
					int left, right;
					for (left = point; left > 0
							&& preprocess[left][range.y].height >= preprocess[point][range.y].height; left--)
						;
					for (right = point; right < range.x + range.width - 1
							&& preprocess[right][range.y].height >= preprocess[point][range.y].height; right++)
						;
					return (left + right) / 2;
				}
			}

		});
	}

	public LineSegment maxmarginsplit(Rectangle range) {
		return split(range, new SplitCondition() {

			@Override
			public boolean satisfy(int point) {
				if (isHorizontal()) {
					return preprocess[range.x][point].width >= range.width;
				} else {
					return preprocess[point][range.y].height >= range.height;
				}
			}

			protected int[] margin(int point) {
				if (isHorizontal()) {
					int top, bottom;
					for (top = point; top > range.y
							&& preprocess[range.x][top].width >= preprocess[range.x][point].width; top--)
						;
					for (bottom = point; bottom < range.y + range.height - 1
							&& preprocess[range.x][bottom].width >= preprocess[range.x][point].width; bottom++)
						;
					return new int[] { top, bottom };
				} else {
					int left, right;
					for (left = point; left > range.x
							&& preprocess[left][range.y].height >= preprocess[point][range.y].height; left--)
						;
					for (right = point; right < range.x + range.width - 1
							&& preprocess[right][range.y].height >= preprocess[point][range.y].height; right++)
						;
					return new int[] { left, right };
				}
			}

			@Override
			public int bias(int point) {
				int[] margin = margin(point);
				return -(margin[1] - margin[0] - 2);
			}

			@Override
			public int fastbreak(int point, int maxbias) {
				if (!satisfy(point))
					return point;
				int[] margin = margin(point);
				return margin[1] - 1;
			}

			@Override
			public int postprocess(int point) {
				int[] margin = margin(point);
				return (margin[0] + margin[1]) / 2;
			}
		});
	}

	protected LineSegment split(Rectangle range, SplitCondition condition) {
		if (null == range) {
			range = new Rectangle(0, 0, accessor.getWidth(),
					accessor.getHeight());
		}
		condition.setRange(range);
		LineSegment vs = vsplit(range, condition);
		LineSegment hs = hsplit(range, condition);
		if (vs == null || hs == null) {
			return vs == null ? hs : vs;
		}
		condition.setHorizontal(false);
		int vratio = condition.bias(vs.from.x);
		condition.setHorizontal(true);
		int hratio = condition.bias(hs.from.y);
		return vratio > hratio ? hs : vs;
	}

	protected LineSegment vsplit(Rectangle range, SplitCondition condition) {
		condition.setHorizontal(false);
		int bias = Integer.MAX_VALUE;
		int record = -1;
		for (int i = range.x + 1; i < range.x + range.width - 1; i++) {
			int fb = condition.fastbreak(i, bias);
			if (fb != i) {
				i = fb;
			}
			if (condition.satisfy(i)) {
				// Candidate
				int thisbias = condition.bias(i);
				if (thisbias < bias) {
					bias = thisbias;
					record = i;
				}
			}
		}
		if (record == -1)
			return null;
		record = condition.postprocess(record);
		return new LineSegment(new Point(record, range.y), new Point(record,
				range.y + range.height));
	}

	protected LineSegment hsplit(Rectangle range, SplitCondition condition) {
		condition.setHorizontal(true);
		int bias = Integer.MAX_VALUE;
		int record = -1;
		for (int i = range.y + 1; i < range.y + range.height - 1; i++) {
			int fb = condition.fastbreak(i, bias);
			if (fb != i) {
				i = fb;
			}
			if (condition.satisfy(i)) {
				// Candidate
				int thisbias = condition.bias(i);
				if (thisbias < bias) {
					bias = thisbias;
					record = i;
				}
			}
		}
		if (-1 == record) {
			return null;
		}
		record = condition.postprocess(record);
		return new LineSegment(new Point(range.x, record), new Point(range.x
				+ range.width, record));
	}

	protected abstract class SplitCondition {

		protected Rectangle range;

		protected boolean horizontal;

		public SplitCondition() {
		}

		public boolean isHorizontal() {
			return horizontal;
		}

		public void setHorizontal(boolean horizontal) {
			this.horizontal = horizontal;
		}

		public Rectangle getRange() {
			return range;
		}

		public void setRange(Rectangle range) {
			this.range = range;
		}

		public abstract boolean satisfy(int point);

		public abstract int bias(int point);

		public abstract int fastbreak(int point, int maxbias);

		public abstract int postprocess(int point);
	}
}
