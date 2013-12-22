package edu.clarkson.cs.wpcomp.text.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class WordDict {

	protected static WordDict instance = new WordDict();

	public static WordDict getInstance() {
		return instance;
	}

	private Set<String> words;

	private WordDict() {
		super();
		words = new HashSet<String>();
		load();
	}

	protected void load() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							"edu/clarkson/cs/wpcomp/text/processor/words")));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!StringUtils.isEmpty(line.trim()))
					words.add(line.trim());
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isWord(String input) {
		return words.contains(input.toLowerCase());
	}
}
