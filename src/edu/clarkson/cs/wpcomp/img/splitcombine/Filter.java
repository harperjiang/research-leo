package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;

public interface Filter {

	public boolean filter(Rectangle r, SplitEnv env);

}