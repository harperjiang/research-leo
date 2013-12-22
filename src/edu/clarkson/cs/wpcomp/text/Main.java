package edu.clarkson.cs.wpcomp.text;

import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import edu.clarkson.cs.wpcomp.text.processor.FrequencyProcessor;
import edu.clarkson.cs.wpcomp.text.processor.FrequencyRecord;
import edu.clarkson.cs.wpcomp.text.processor.WordDict;
import edu.clarkson.cs.wpcomp.text.stream.DictionaryFilterTokenStream;
import edu.clarkson.cs.wpcomp.text.stream.Parser;
import edu.clarkson.cs.wpcomp.text.stream.ParserTokenStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		FileInputStream fis = new FileInputStream("res/text/test");

		TokenStream ts = new DictionaryFilterTokenStream(new ParserTokenStream(
				new Parser(fis)));

		FrequencyProcessor fprocessor = new FrequencyProcessor();
		fprocessor.process(ts);

		PriorityQueue<FrequencyRecord> sort = new PriorityQueue<FrequencyRecord>();
		for (Entry<String, Integer> entry : fprocessor.getCounter().entrySet()) {
			sort.offer(new FrequencyRecord(entry.getKey(), entry.getValue()));
		}

		while (!sort.isEmpty()) {
			FrequencyRecord next = sort.poll();
			System.out.print(next);
			System.out.print(":");
			System.out
					.println(WordDict.getInstance().isWord(next.getWord()) ? "Word"
							: "NonWord");
		}
	}
}
