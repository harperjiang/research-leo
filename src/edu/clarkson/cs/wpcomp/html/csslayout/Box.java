package edu.clarkson.cs.wpcomp.html.csslayout;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class Box {

	private Rectangle location;

	private HtmlElement element;

	private Box parent;

	private List<Box> children;

	public Box(HtmlElement element, Rectangle location) {
		super();
		this.element = element;
		this.location = location;
		this.children = new ArrayList<Box>();
	}

	public Rectangle getLocation() {
		return location;
	}

	public HtmlElement getElement() {
		return element;
	}

	public Box getParent() {
		return parent;
	}

	protected void setParent(Box parent) {
		this.parent = parent;
	}

	public void addChild(Box child) {
		Box old = child.getParent();
		child.setParent(this);
		this.children.add(child);
		if (old != null)
			old.removeChild(child);
	}

	public void removeChild(Box child) {
		child.setParent(null);
		this.children.remove(child);
	}
}
