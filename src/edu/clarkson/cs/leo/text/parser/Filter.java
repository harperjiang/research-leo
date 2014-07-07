package edu.clarkson.cs.leo.text.parser;

import java.util.List;

import edu.clarkson.cs.leo.text.model.WordGroup;

public interface Filter {

	public List<WordGroup> filter(WordGroup input);
}
