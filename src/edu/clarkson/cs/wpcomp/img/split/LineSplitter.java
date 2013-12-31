package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class LineSplitter extends AbstractSplitter {

	public LineSplitter(ColorAccessor accessor) {
		super(accessor);
	}

	public LineSegment centralsplit(Rectangle range) {
		if (null == range) {
			range = new Rectangle(0, 0, accessor.getWidth(),
					accessor.getHeight());
		}
		LineSegment vc = vccentralsplit(range);
		LineSegment hc = hccentralsplit(range);
		if (vc == null || hc == null) {
			return vc == null ? hc : vc;
		}
		int vratio = Math.abs((vc.from.x - (range.x + range.width / 2))
				* range.height);
		int hratio = Math.abs((vc.from.y - (range.y + range.height / 2))
				* range.width);
		return vratio > hratio ? hc : vc;
	}

	protected LineSegment vccentralsplit(Rectangle range) {
		long max = range.width;
		int record = -1;
		for (int i = range.x + 1; i < range.x + range.width - 1; i++) {
			if (i - (range.x + range.width / 2) > max)
				break;
			Dimension current = preprocess[i][range.y];
			if (current.height >= range.height) {
				// Candidate
				if (Math.abs(i - (range.x + range.width / 2)) < max) {
					max = Math.abs(i - (range.x + range.width / 2));
					record = i;
				}
			}
		}
		if (record == -1)
			return null;
		// Adjust to the center of black area
		int left = record;
		while (preprocess[left][range.y].height >= range.height) {
			left--;
		}
		int right = record;
		while (preprocess[right][range.y].height >= range.height) {
			right++;
		}
		record = (left + right) / 2;

		return new LineSegment(new Point(record, range.y), new Point(record,
				range.y + range.height));
	}

	protected LineSegment hccentralsplit(Rectangle range) {
		long max = range.height;
		int record = -1;
		for (int i = range.y + 1; i < range.y + range.height - 1; i++) {
			if (i - (range.y + range.height / 2) > max) {
				break;
			}
			Dimension current = preprocess[range.x][i];
			if (current.width >= range.width) {
				// Candidate
				if (Math.abs(i - (range.y + range.height / 2)) < max) {
					max = Math.abs(i - (range.y + range.height / 2));
					record = i;
				}
			}
		}
		if (-1 == record) {
			return null;
		}
		// Adjust to the center of black area
		int top = record;
		while (preprocess[range.x][top].width >= range.width) {
			top--;
		}
		int bottom = record;
		while (preprocess[range.x][bottom].width >= range.width) {
			bottom++;
		}
		record = (top + bottom) / 2;
		return new LineSegment(new Point(range.x, record), new Point(range.x
				+ range.width, record));
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
			if (condition.fastbreak(i))
				break;
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
			if (condition.fastbreak(i)) {
				break;
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

		private Rectangle range;

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

		public abstract boolean fastbreak(int point);

		public abstract int postprocess(int point);
	}
}
