package edu.clarkson.cs.wpcomp.text.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.wpcomp.common.sort.HeapSorter;
import edu.clarkson.cs.wpcomp.text.model.Token;
import edu.clarkson.cs.wpcomp.text.model.WordGroup;

public class KeywordAnalysis implements Analysis {

	private double percentage = 0.3;

	public KeywordAnalysis() {
		super();
	}

	private Map<String, BigDecimal> scores;

	private List<String> keywords;

	@Override
	public void analyze(List<WordGroup> wgs) {
		scores = new HashMap<String, BigDecimal>();

		Map<String, Integer> counter = new HashMap<String, Integer>();
		Map<String, Averager> groupLength = new HashMap<String, Averager>();
		Set<WordGroup> sentences = new HashSet<WordGroup>();

		// Look for frequency and degree
		for (WordGroup group : wgs) {
			for (Token token : group.getTokens()) {
				sentences.add(token.getOrigin());
				String word = token.getWord();
				if (counter.containsKey(word)) {
					counter.put(word, counter.get(word) + 1);
				} else {
					counter.put(word, 1);
				}
				if (!groupLength.containsKey(word)) {
					groupLength.put(word,
							new Averager(BigDecimal.valueOf(group.getSize())));
				} else {
					groupLength.get(word).inc(
							BigDecimal.valueOf(group.getSize()));
				}
			}
		}
		// Calculate the credit for single words
		for (String word : counter.keySet()) {
			BigDecimal count = BigDecimal.valueOf(counter.get(word));
			BigDecimal degree = groupLength.get(word).getAverage();
			BigDecimal dof = degree.divide(count, 2, BigDecimal.ROUND_HALF_UP);
			scores.put(word, count.add(degree).add(dof));
		}
		// Look for word combinations. This only applies to words that appears
		// more than once.
		Set<String> moreThanOnce = new HashSet<String>();
		for (Entry<String, Integer> entry : counter.entrySet()) {
			if (entry.getValue() > 1)
				moreThanOnce.add(entry.getKey());
		}
		List<WordGroup> sentenceList = new ArrayList<WordGroup>();
		sentenceList.addAll(sentences);

		Map<WordGroup, Set<WordGroup>> phraseCounts = new HashMap<WordGroup, Set<WordGroup>>();

		for (int i = 0; i < sentenceList.size(); i++) {
			for (int j = i + 1; j < sentenceList.size(); j++) {
				List<WordGroup> sharedPhrases = sharedSubstring(
						sentenceList.get(i), sentenceList.get(j), moreThanOnce);
				for (WordGroup phrase : sharedPhrases) {
					if (!phraseCounts.containsKey(phrase)) {
						phraseCounts.put(phrase, new HashSet<WordGroup>());
					}
					phraseCounts.get(phrase).add(sentenceList.get(i));
					phraseCounts.get(phrase).add(sentenceList.get(j));
				}
			}
		}
		// Calculate score for the phrase
		for (WordGroup phrase : phraseCounts.keySet()) {
			BigDecimal sum = BigDecimal.ZERO;
			for (Token token : phrase.getTokens()) {
				String word = token.getWord();
				if (!StringUtils.isEmpty(word) && scores.containsKey(word)) {
					sum = sum.add(scores.get(word));
				}
			}
			scores.put(phrase.toString(), sum);
		}

		int threshold = (int) Math.floor(scores.size() * percentage);
		keywords = new ArrayList<String>();
		keywords.addAll(scores.keySet());
		new HeapSorter(true).sort(keywords, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return scores.get(o2).compareTo(scores.get(o1));
			}
		});
		List<String> temp = new ArrayList<String>();
		temp.addAll(keywords.subList(0, threshold));
		keywords = temp;
		return;
	}

	public Map<String, BigDecimal> getScores() {
		return scores;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	protected List<WordGroup> sharedSubstring(WordGroup sentence1,
			WordGroup sentence2, Set<String> base) {
		List<WordGroup> candidates = new ArrayList<WordGroup>();
		@SuppressWarnings("unchecked")
		Collection<String> sameWords = CollectionUtils.intersection(
				base,
				CollectionUtils.intersection(sentence1.getWords(),
						sentence2.getWords()));
		if (sameWords.isEmpty() || sameWords.size() < 2)
			return candidates;
		String s2String = sentence2.toString();

		List<Token> tokens = new ArrayList<Token>();
		for (String word : sameWords) {
			tokens.addAll(sentence1.query(word));
		}
		new HeapSorter(true).sort(tokens, new Comparator<Token>() {
			@Override
			public int compare(Token o1, Token o2) {
				return o1.getIndex().compareTo(o2.getIndex());
			}
		});
		int begin = 0, end = tokens.size() - 1;
		while (begin < tokens.size() - 1) {
			Token first = tokens.get(begin);
			Token second = tokens.get(end);

			String substring = sentence1.subString(first.getIndex(),
					second.getIndex());
			if (s2String.contains(substring)) {
				WordGroup candidate = new WordGroup();
				for (int index = first.getIndex(); index <= second.getIndex(); index++)
					candidate.addToken(sentence1.getTokens().get(index));
				if (candidate.getSize() == 1) {
					throw new RuntimeException();
				}
				candidates.add(candidate);
				begin = end + 1;
				end = tokens.size() - 1;
			} else {
				end--;
			}
			if (begin == end) {
				begin++;
				end = tokens.size() - 1;
			}
		}

		return candidates;
	}
}
