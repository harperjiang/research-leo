package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface Checker {
	
	public boolean check(Rectangle r, CheckerEnv env);

	public static final class CheckerEnv {
		public LineSplitter lineSplitter;
		public RectangleSplitter rectSplitter;
		public BufferedImage sourceImage;
	}
}