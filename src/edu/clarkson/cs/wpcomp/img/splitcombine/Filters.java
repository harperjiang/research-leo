package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.splitcombine.Filter.FilterResult;

public class Filters {

	public static List<Rectangle> filter(SplitEnv env, Filter filter,
			List<Rectangle> inputs) {
		List<Rectangle> result = new ArrayList<Rectangle>();
		for (Rectangle input : inputs)
			if (FilterResult.CONTINUE == filter.filter(input, env))
				result.add(input);
		return result;
	}
}
