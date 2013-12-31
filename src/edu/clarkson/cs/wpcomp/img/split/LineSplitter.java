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
		LineSegment vc = vcsplit(range);
		LineSegment hc = hcsplit(range);
		if (vc == null || hc == null) {
			return vc == null ? hc : vc;
		}
		int vratio = Math.abs((vc.from.x - (range.x + range.width / 2))
				* range.height);
		int hratio = Math.abs((vc.from.y - (range.y + range.height / 2))
				* range.width);
		return vratio > hratio ? hc : vc;
	}

	protected LineSegment vcsplit(Rectangle range) {
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
		return new LineSegment(new Point(record, range.y), new Point(record,
				range.y + range.height));
	}

	protected LineSegment hcsplit(Rectangle range) {
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
		return new LineSegment(new Point(range.x, record), new Point(range.x
				+ range.width, record));
	}
}
