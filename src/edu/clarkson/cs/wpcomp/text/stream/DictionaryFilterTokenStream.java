package edu.clarkson.cs.wpcomp.text.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.clarkson.cs.wpcomp.text.Token;
import edu.clarkson.cs.wpcomp.text.TokenStream;

public class DictionaryFilterTokenStream extends FilterTokenStream {

	private Set<String> dictionary;

	public DictionaryFilterTokenStream(TokenStream input) {
		super(input);
		dictionary = new HashSet<String>();
		loadDict();
	}

	private void loadDict() {
		BufferedReader br = new BufferedReader(new InputStreamReader(Thread
				.currentThread().getContextClassLoader()
				.getResourceAsStream("edu/clarkson/cs/wpcomp/text/stream/dictionary")));
		try {
			String line = null;
			while ((line = br.readLine()) != null)
				dictionary.add(line);
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Token> filter(Token input) {
		StringBuilder buffer = new StringBuilder();
		List<Token> tokens = new ArrayList<Token>();
		String[] pieces = input.getContent().split(" ");
		if (pieces != null && pieces.length != 0)
			for (String piece : pieces) {
				if (dictionary.contains(piece.toLowerCase())) {
					if (0 != buffer.length()) {
						tokens.add(new Token(buffer.toString().trim()));
						buffer = new StringBuilder();
					}
				} else {
					buffer.append(piece).append(" ");
				}
			}
		if (0 != buffer.length())
			tokens.add(new Token(buffer.toString().trim()));
		return tokens;
	}

}
