package edu.clarkson.cs.wpcomp.text.processor;

import java.text.MessageFormat;

public class FrequencyRecord implements Comparable<FrequencyRecord> {

	private String word;

	private Integer count;

	public FrequencyRecord(String word, Integer count) {
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public int compareTo(FrequencyRecord o) {
		return o.count.compareTo(this.count);
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}:{1}", word, count);
	}

}
