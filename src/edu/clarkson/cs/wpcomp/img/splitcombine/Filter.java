package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;

public interface Filter {

	public static enum FilterResult {
		CONTINUE, STOP, DISCARD
	}

	public FilterResult filter(Rectangle r, SplitEnv env);

}