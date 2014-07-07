package edu.clarkson.cs.leo.text.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.leo.text.model.Token;
import edu.clarkson.cs.leo.text.model.WordGroup;

public class Parser {

	private Lexer lexer;

	private Filter filter;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public List<WordGroup> parse() throws IOException {
		List<WordGroup> result = new ArrayList<WordGroup>();

		WordGroup current = new WordGroup();
		boolean eof = false;
		while (!eof) {
			Token next = lexer.yylex();
			if (null == next)
				continue;
			switch (next.getType()) {
			case WORD:
				current.addToken(next);
				break;
			case EOF:
				eof = true;
				if (!current.isEmpty()) {
					if (null == filter)
						result.add(current);
					else
						result.addAll(filter.filter(current));
				}
				current = new WordGroup();
				break;
			case STOP:
				if (!StringUtils.isEmpty(next.getContent())) {
					current.addToken(next);
				}
				if (!current.isEmpty()) {
					if (null == filter)
						result.add(current);
					else
						result.addAll(filter.filter(current));
				}
				current = new WordGroup();
				break;
			}
		}
		return result;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

}
