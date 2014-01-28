package edu.clarkson.cs.wpcomp.img;

import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.split.LineSegment;

public class GeometryHelper {

	public static Rectangle[] split(Rectangle r, LineSegment split) {
		if (split.isVertical()) {
			return new Rectangle[] {
					new Rectangle(r.x, r.y, split.from.x - r.x, r.height),
					new Rectangle(split.from.x, r.y, r.width + r.x
							- split.from.x, r.height) };
		} else if (split.isHorizontal()) {
			return new Rectangle[] {
					new Rectangle(r.x, r.y, r.width, split.from.y - r.y),
					new Rectangle(r.x, split.from.y, r.width, r.y + r.height
							- split.from.y) };
		} else {
			throw new IllegalArgumentException();
		}
	}
}
