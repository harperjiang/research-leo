package edu.clarkson.cs.wpcomp.text.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordGroup {

	private List<Token> tokens;

	private Set<String> words;

	public WordGroup() {
		super();
		tokens = new ArrayList<Token>();
		words = null;
	}

	public boolean isEmpty() {
		return tokens.isEmpty();
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void addToken(Token token) {
		if (null == token.getOrigin()) {
			token.setOrigin(this);
			token.setIndex(tokens.size());
		}
		tokens.add(token);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Token token : tokens) {
			builder.append(token.getContent()).append(" ");
		}
		return builder.toString().trim();
	}

	public int getSize() {
		return getTokens().size();
	}

	public Set<String> getWords() {
		if (words == null) {
			words = new HashSet<String>();
			for (Token token : getTokens()) {
				words.add(token.getContent());
			}
		}
		return words;
	}

	public String subString(int i, int j) {
		StringBuilder sb = new StringBuilder();
		for (int index = i; index <= j; index++) {
			sb.append(getTokens().get(index)).append(" ");
		}
		return sb.toString().trim();
	}

	private Map<String, List<Token>> dictionary;

	public List<Token> query(String queryWord) {
		if (null == dictionary) {
			dictionary = new HashMap<String, List<Token>>();
			for (Token token : getTokens()) {
				String word = token.getWord();
				if (!dictionary.containsKey(word)) {
					dictionary.put(word, new ArrayList<Token>());
				}
				dictionary.get(word).add(token);
			}
		}
		return dictionary.get(queryWord);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WordGroup) {
			return toString().equals(obj.toString());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
