package edu.clarkson.cs.wpcomp.img.splitcombine.filter;

import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.splitcombine.SplitEnv;

public interface Filter {

	public FilterResult filter(Rectangle r, SplitEnv env);

}