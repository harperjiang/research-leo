package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;

public class SizeChecker implements Checker {

	private int widthThreshold = 5;

	private int heightThreshold = 5;

	private int areaThreshold = 100;

	@Override
	public boolean check(Rectangle r, Object... env) {
		return r.width > widthThreshold && r.height > heightThreshold
				&& r.width * r.height > areaThreshold;
	}

}
