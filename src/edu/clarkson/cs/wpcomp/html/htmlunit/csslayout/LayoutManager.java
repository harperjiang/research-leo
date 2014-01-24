package edu.clarkson.cs.wpcomp.html.htmlunit.csslayout;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

public class LayoutManager {

	public Box layout(HtmlBody body, Dimension pageSize) {
		return layout(body, new Rectangle(new Point(0, 0), pageSize));
	}

	protected Box layout(HtmlElement element, Rectangle range) {
		Box current = new Box(element, range);

		CSSStyleDeclaration style = getStyle(element);

		Insets margin = getMargin(style);
		Insets padding = getPadding(style);

		for (DomElement child : element.getChildElements()) {
			if (child instanceof HtmlElement) {

			}
		}

		return null;
	}

	protected static CSSStyleDeclaration getStyle(HtmlElement element) {
		return ((HTMLElement) element.getScriptObject()).getCurrentStyle();
	}

	protected static Insets getMargin(CSSStyleDeclaration style) {
		String marginTop = style.getMarginTop();
		String marginLeft = style.getMarginLeft();
		String marginBottom = style.getMarginBottom();
		String marginRight = style.getMarginRight();
		return new Insets(pixel(marginTop), pixel(marginLeft),
				pixel(marginBottom), pixel(marginRight));
	}

	protected static Insets getPadding(CSSStyleDeclaration style) {
		String paddingTop = style.getPaddingTop();
		String paddingLeft = style.getPaddingLeft();
		String paddingBottom = style.getPaddingBottom();
		String paddingRight = style.getPaddingRight();
		return new Insets(pixel(paddingTop), pixel(paddingLeft),
				pixel(paddingBottom), pixel(paddingRight));
	}

	private static final Pattern UNIT_NUM = Pattern.compile("(\\d+)(\\w)+");

	protected static int pixel(String value) {
		// TODO Not finished
		return 0;
	}
}
