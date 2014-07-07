package edu.clarkson.cs.leo.text;

import java.io.FileInputStream;
import java.util.List;

import edu.clarkson.cs.leo.text.model.WordGroup;
import edu.clarkson.cs.leo.text.parser.DictionaryFilter;
import edu.clarkson.cs.leo.text.parser.Lexer;
import edu.clarkson.cs.leo.text.parser.Parser;
import edu.clarkson.cs.leo.text.processor.KeywordAnalysis;

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
