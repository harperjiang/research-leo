package edu.clarkson.cs.leo.img.splitcombine.filter;

import java.awt.Rectangle;

import edu.clarkson.cs.leo.img.splitcombine.SplitEnv;

public class SizeFilter implements Filter {

	private int widthThreshold = 8;

	private int heightThreshold = 8;

	private int areaThreshold = 100;

	@Override
	public FilterResult filter(Rectangle r, SplitEnv cenv) {
		FilterResult result = new FilterResult();
		if (r.width > widthThreshold && r.height > heightThreshold
				&& r.width * r.height > areaThreshold)
			result.getAccepted().add(r);
		else
			result.getMatured().add(r);
		return result;
	}

}
