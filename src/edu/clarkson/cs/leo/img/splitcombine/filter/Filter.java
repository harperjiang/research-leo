package edu.clarkson.cs.leo.img.splitcombine.filter;

import java.awt.Rectangle;

import edu.clarkson.cs.leo.img.splitcombine.SplitEnv;

public interface Filter {

	public FilterResult filter(Rectangle r, SplitEnv env);

}