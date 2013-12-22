package edu.clarkson.cs.wpcomp.text.stream;

import java.io.IOException;

import edu.clarkson.cs.wpcomp.text.Token;

public class ParserTokenStream extends AbstractTokenStream {

	private Parser parser;

	public ParserTokenStream(Parser parser) {
		this.parser = parser;
	}

	@Override
	protected Token nextToken() throws IOException {
		Token next = null;
		while (next == null) {
			next = parser.yylex();
		}
		return next;
	}

}
