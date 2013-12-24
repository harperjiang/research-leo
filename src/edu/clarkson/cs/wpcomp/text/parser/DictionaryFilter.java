package edu.clarkson.cs.wpcomp.text.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.clarkson.cs.wpcomp.text.model.Token;
import edu.clarkson.cs.wpcomp.text.model.WordGroup;

public class DictionaryFilter implements Filter {

	private Set<String> dictionary;

	public DictionaryFilter() {
		super();
		dictionary = new HashSet<String>();
		loadDict();
	}

	private void loadDict() {
		BufferedReader br = new BufferedReader(new InputStreamReader(Thread
				.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(
						"edu/clarkson/cs/wpcomp/text/parser/dictionary")));
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
	public List<WordGroup> filter(WordGroup input) {
		List<WordGroup> result = new ArrayList<WordGroup>();
		WordGroup current = new WordGroup();
		for (Token token : input.getTokens()) {
			token.setWord(Transformers.transform(token.getContent()));
			if (dictionary.contains(token.getWord())) {
				if (!current.isEmpty()) {
					result.add(current);
					current = new WordGroup();
				}
			} else {
				current.addToken(token);
			}
		}
		if (!current.isEmpty())
			result.add(current);
		return result;
	}

}
