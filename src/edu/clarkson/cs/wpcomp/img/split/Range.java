package edu.clarkson.cs.wpcomp.img.split;

import java.text.MessageFormat;

public class Range implements Cloneable {

	public int left;
	public int top;
	public int right;
	public int bottom;

	public Range(int l, int t, int r, int b) {
		this.left = l;
		this.top = t;
		this.right = r;
		this.bottom = b;
	}

	public Range clone() {
		try {
			return (Range) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return MessageFormat.format("Left:{0},Top:{1},Right:{2},Bottom:{3}",
				left, top, right, bottom);
	}
}
