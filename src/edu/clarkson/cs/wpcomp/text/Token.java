package edu.clarkson.cs.wpcomp.text;

import org.apache.commons.lang3.StringUtils;

public class Token {

	public static final Token EOF = new Token();

	private String content;

	public Token(String content) {
		this.content = content;
		if (StringUtils.isEmpty(content))
			throw new IllegalArgumentException();
	}

	public Token() {

	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return getContent();
	}
}
