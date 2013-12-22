package edu.clarkson.cs.wpcomp.text.stream;

import java.io.EOFException;
import java.io.IOException;

import edu.clarkson.cs.wpcomp.text.Token;
import edu.clarkson.cs.wpcomp.text.TokenStream;

public abstract class AbstractTokenStream implements TokenStream {

	private boolean eof = false;

	@Override
	public Token next() throws IOException, EOFException {
		if (eof)
			throw new EOFException();
		Token next = nextToken();
		if (next == Token.EOF)
			eof = true;
		return next;
	}

	protected abstract Token nextToken() throws IOException;

}
