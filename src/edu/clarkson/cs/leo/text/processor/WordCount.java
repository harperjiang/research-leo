package edu.clarkson.cs.leo.text.processor;

public class WordCount implements Comparable<WordCount> {

	private String word;

	private Integer count;

	public WordCount(String word, Integer count) {
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public Integer getCount() {
		return count;
	}

	public int compareTo(WordCount o) {
		return o.count.compareTo(count);
	}
}
