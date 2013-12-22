package edu.clarkson.cs.wpcomp.text.processor;

import java.util.HashMap;
import java.util.Map;

import edu.clarkson.cs.wpcomp.text.Token;
import edu.clarkson.cs.wpcomp.text.TokenProcessor;
import edu.clarkson.cs.wpcomp.text.TokenStream;

public class FrequencyProcessor implements TokenProcessor {

	private Map<String, Integer> counter;

	public FrequencyProcessor() {
		super();
		counter = new HashMap<String, Integer>();
	}

	@Override
	public void process(TokenStream input) {
		while (true) {
			try {
				Token next = input.next();
				if (next == Token.EOF)
					break;
				String[] pieces = next.getContent().split(" ");
				for (String piece : pieces) {
					String processed = Transformers.transform(piece);
					if (counter.containsKey(processed)) {
						counter.put(processed, counter.get(processed) + 1);
					} else {
						counter.put(processed, 1);
					}
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Map<String, Integer> getCounter() {
		return counter;
	}

}
