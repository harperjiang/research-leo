package edu.clarkson.cs.wpcomp.text.parser;

import java.util.List;

import edu.clarkson.cs.wpcomp.text.model.WordGroup;

public interface Filter {

	public List<WordGroup> filter(WordGroup input);
}
