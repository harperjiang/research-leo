package edu.clarkson.cs.wpcomp.text.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.wpcomp.text.Token;
import edu.clarkson.cs.wpcomp.text.TokenStream;

public class TokenStreamForker {

	private TokenStream input;

	private List<Token> buffer;

	private Map<ForkedTokenStream, Integer> pointers;

	private int threshold = 1000;

	private boolean eof = false;

	public TokenStreamForker(TokenStream input) {
		super();
		this.input = input;
		this.buffer = new ArrayList<Token>();
		this.pointers = new HashMap<ForkedTokenStream, Integer>();
	}

	public TokenStream fork() {
		ForkedTokenStream forked = new ForkedTokenStream();
		// Not read from the very beginning
		pointers.put(forked, 0);
		return forked;
	}

	protected synchronized void fill() throws IOException {
		if (eof)
			return;
		Token next = input.next();
		if (next == Token.EOF)
			eof = true;
		buffer.add(next);
		// Clean
		if (buffer.size() >= threshold) {
			int min = Integer.MAX_VALUE;
			for (int current : pointers.values()) {
				min = Math.min(current, min);
			}
			for (int i = 0; i < min; i++)
				buffer.remove(0);
			for (Entry<ForkedTokenStream, Integer> entry : pointers.entrySet()) {
				entry.setValue(entry.getValue() - min);
			}
		}
	}

	protected class ForkedTokenStream extends AbstractTokenStream {

		@Override
		protected Token nextToken() throws IOException {
			int pointer = pointers.get(this);
			if (pointer == buffer.size()) {
				fill();
			}
			Token next = buffer.get(pointer);
			pointers.put(this, pointer + 1);
			return next;
		}
	}
}
