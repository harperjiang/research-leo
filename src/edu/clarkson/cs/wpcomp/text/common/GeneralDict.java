package edu.clarkson.cs.wpcomp.text.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralDict {

	private String dictFile;

	private int columns;

	private int[][] keyValues;

	private List<Map<String, String>> dicts;

	public GeneralDict(String dict, int col, int[][] keyvalues) {
		this.dictFile = dict;
		this.columns = col;
		this.keyValues = keyvalues;
		this.dicts = new ArrayList<Map<String, String>>();

		for (int count = 0; count < keyvalues.length; count++) {
			dicts.add(new HashMap<String, String>());
		}
		load();
	}

	protected void load() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Thread
					.currentThread().getContextClassLoader()
					.getResourceAsStream(dictFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.trim().split("\\s");
				if (parts.length != columns) {
					throw new IllegalArgumentException(line + ":"
							+ parts.length);
				}
				for (int count = 0; count < keyValues.length; count++) {
					int[] pair = keyValues[count];
					String[] multi = parts[pair[0]].split("/");
					for (String key : multi)
						dicts.get(count).put(key, parts[pair[1]]);
				}
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String query(String input, int index) {
		if (index >= dicts.size() || index < 0)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		return dicts.get(index).get(input);
	}
}
