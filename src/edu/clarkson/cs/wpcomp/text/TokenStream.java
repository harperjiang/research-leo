package edu.clarkson.cs.wpcomp.text;

import java.io.EOFException;
import java.io.IOException;

public interface TokenStream {

	public Token next() throws IOException, EOFException;
}
