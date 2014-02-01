package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Point;
import java.text.MessageFormat;

public class LineSegment {

	public Point from;

	public Point to;

	public LineSegment() {
		from = new Point();
		to = new Point();
	}

	public LineSegment(Point from, Point to) {
		this.from = from;
		this.to = to;
	}

	public boolean isHorizontal() {
		return from.y == to.y;
	}

	public boolean isVertical() {
		return from.x == to.x;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} to {1}", from, to);
	}
}
