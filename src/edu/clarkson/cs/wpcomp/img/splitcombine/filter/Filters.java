package edu.clarkson.cs.wpcomp.img.splitcombine.filter;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.splitcombine.SplitEnv;

public class Filters {

	public static List<Rectangle> filter(SplitEnv env, Filter filter,
			List<Rectangle> inputs) {
		List<Rectangle> result = new ArrayList<Rectangle>();
		for (Rectangle input : inputs)
			result.addAll(filter.filter(input, env).getAccepted());
		return result;
	}
}
