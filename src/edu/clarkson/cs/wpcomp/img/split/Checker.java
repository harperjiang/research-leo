package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;

public interface Checker {
	public boolean check(Rectangle r, Object... env);
}