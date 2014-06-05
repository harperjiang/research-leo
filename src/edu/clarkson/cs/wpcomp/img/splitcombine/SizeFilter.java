package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;

public class SizeFilter implements Filter {

	private int widthThreshold = 8;

	private int heightThreshold = 8;

	private int areaThreshold = 100;

	@Override
	public FilterResult filter(Rectangle r, SplitEnv cenv) {
		if (r.width > widthThreshold && r.height > heightThreshold
				&& r.width * r.height > areaThreshold)
			return FilterResult.CONTINUE;
		return FilterResult.STOP;
	}

}
