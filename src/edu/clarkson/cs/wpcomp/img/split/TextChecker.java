package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;

public class TextChecker implements Checker {

	@Override
	public boolean check(Rectangle r, Object... env) {
		if (env.length < 2 || !(env[0] instanceof RectangleSplitter)
				|| !(env[1] instanceof LineSplitter)) {
			throw new IllegalArgumentException();
		}
		RectangleSplitter rect = (RectangleSplitter) env[0];
		LineSplitter line = (LineSplitter) env[1];
		
		// Split the text horizontally
//		line.hsplit(r, condition)
		
		
		return true;
	}

}
