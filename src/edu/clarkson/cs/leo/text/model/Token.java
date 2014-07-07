package edu.clarkson.cs.leo.text.model;

import org.apache.commons.lang3.StringUtils;

public class Token {

	public static enum Type {
		WORD, STOP, EOF
	}

	public static final Token STOP = new Token(Type.STOP);

	public static final Token EOF = new Token(Type.EOF);

	private Type type;

	private String content;

	private String word;

	private int index;

	private WordGroup origin;

	public Token(String content) {
		this(Type.WORD);
		this.content = content;
		if (StringUtils.isEmpty(content))
			throw new IllegalArgumentException();
	}

	public Token(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return getContent();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public WordGroup getOrigin() {
		return origin;
	}

	public void setOrigin(WordGroup origin) {
		this.origin = origin;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
