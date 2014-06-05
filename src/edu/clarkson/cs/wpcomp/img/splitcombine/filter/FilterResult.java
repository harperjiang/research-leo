package edu.clarkson.cs.wpcomp.img.splitcombine.filter;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class FilterResult {

	private List<Rectangle> matured;

	private List<Rectangle> accepted;

	public FilterResult() {
		super();
		matured = new ArrayList<Rectangle>();
		accepted = new ArrayList<Rectangle>();
	}

	public List<Rectangle> getMatured() {
		return matured;
	}

	public List<Rectangle> getAccepted() {
		return accepted;
	}

}
