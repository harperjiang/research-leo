package edu.clarkson.cs.wpcomp.html;

import java.text.MessageFormat;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

public class HtmlLoading {
	public static void main(String[] args) throws Exception {
		final WebClient webClient = new WebClient();
		final HtmlPage page = webClient.getPage("http://www.paypal.com");

		DomNodeList<DomElement> divs = page.getElementsByTagName("div");

		HTMLBodyElement body = (HTMLBodyElement) (page.getBody()
				.getScriptObject());
		System.out.println(body.getCurrentStyle().getWidth());

		for (DomElement div : divs) {
			HtmlDivision htmldiv = (HtmlDivision) div;
			ComputedCSSStyleDeclaration style = ((HTMLElement) htmldiv
					.getScriptObject()).getCurrentStyle();
			System.out.println(style.getVisibility());
			System.out.println(MessageFormat.format("{0},{1}-{2},{3}",
					style.getLeft(), style.getTop(), style.getWidth(),
					style.getHeight()));
		}

		webClient.closeAllWindows();
	}
}