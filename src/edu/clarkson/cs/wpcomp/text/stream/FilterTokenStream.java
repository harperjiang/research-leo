package edu.clarkson.cs.wpcomp.text.stream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;

import edu.clarkson.cs.wpcomp.text.Token;
import edu.clarkson.cs.wpcomp.text.TokenStream;

public abstract class FilterTokenStream extends AbstractTokenStream {

	private TokenStream input;

	private Queue<Token> buffer;

	public FilterTokenStream(TokenStream input) {
		this.input = input;
		this.buffer = new LinkedList<Token>();
	}

	@Override
	protected Token nextToken() throws IOException {
		fillBuffer();
		return buffer.poll();
	}

	private void fillBuffer() throws IOException {
		while (buffer.isEmpty()) {
			Token token = input.next();
			if (token == Token.EOF) {
				buffer.add(token);
				break;
			}
			List<Token> tokens = filter(token);
			if (!CollectionUtils.isEmpty(tokens))
				buffer.addAll(tokens);
		}
	}

	public abstract List<Token> filter(Token input);
}
