package edu.clarkson.cs.leo.img;

import java.awt.Color;
import java.awt.Rectangle;

import edu.clarkson.cs.leo.img.accessor.ColorAccessor;
import edu.clarkson.cs.leo.img.splitcombine.LineSegment;

public class MarkHelper {

	public static void redrect(Rectangle range, ColorAccessor accessor) {
		for (int i = range.x; i < range.x + range.width; i++) {
			accessor.setValue(i, range.y, Color.RED);
			accessor.setValue(i, range.y + range.height - 1, Color.RED);
		}
		for (int i = range.y; i < range.y + range.height; i++) {
			accessor.setValue(range.x, i, Color.RED);
			accessor.setValue(range.x + range.width - 1, i, Color.RED);
		}
	}

	public static void redline(LineSegment line, ColorAccessor accessor) {
		if (line.from.x == line.to.x) {
			int step = line.from.y > line.to.y ? -1 : 1;
			for (int i = line.from.y; i != line.to.y; i += step) {
				accessor.setValue(line.from.x, i, Color.RED);
			}
		} else if (line.from.y == line.to.y) {
			int step = line.from.x > line.to.x ? -1 : 1;
			for (int i = line.from.x; i != line.to.x; i += step) {
				accessor.setValue(i, line.from.y, Color.RED);
			}
		}
	}

}
