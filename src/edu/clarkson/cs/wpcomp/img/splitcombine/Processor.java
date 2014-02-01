package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.util.List;

public interface Processor {

	public List<Rectangle> process(Rectangle range, SplitEnv env);
}
