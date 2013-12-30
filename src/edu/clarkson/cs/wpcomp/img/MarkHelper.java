package edu.clarkson.cs.wpcomp.img;

import java.awt.Color;
import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class MarkHelper {

	public static void redrect(Rectangle range, ImageAccessor accessor) {
		for (int i = range.x; i < range.x + range.width; i++) {
			accessor.setValue(i, range.y, Color.RED);
			accessor.setValue(i, range.y + range.height, Color.RED);
		}
		for (int i = range.y; i < range.y + range.height; i++) {
			accessor.setValue(range.x, i, Color.RED);
			accessor.setValue(range.x + range.width, i, Color.RED);
		}
	}

}
