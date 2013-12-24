package edu.clarkson.cs.wpcomp.text;

import java.io.FileInputStream;
import java.util.List;

import edu.clarkson.cs.wpcomp.text.model.WordGroup;
import edu.clarkson.cs.wpcomp.text.parser.DictionaryFilter;
import edu.clarkson.cs.wpcomp.text.parser.Lexer;
import edu.clarkson.cs.wpcomp.text.parser.Parser;
import edu.clarkson.cs.wpcomp.text.processor.KeywordAnalysis;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("res/text/test2");

		Parser parser = new Parser(new Lexer(fis));
		parser.setFilter(new DictionaryFilter());

		List<WordGroup> sentences = parser.parse();

		KeywordAnalysis analysis = new KeywordAnalysis();
		analysis.analyze(sentences);

		System.out.println(analysis.getKeywords());
	}
}
