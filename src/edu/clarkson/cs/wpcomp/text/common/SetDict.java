package edu.clarkson.cs.wpcomp.text.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class SetDict {

	private String file;

	private Set<String> words;

	public SetDict(String file) {
		super();
		this.file = file;
		words = new HashSet<String>();
		load();
	}

	protected void load() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Thread
					.currentThread().getContextClassLoader()
					.getResourceAsStream(file)));
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

	public Set<String> getDict() {
		return words;
	}
}
