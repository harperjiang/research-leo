package edu.clarkson.cs.leo.text.processor;

import java.util.List;

import edu.clarkson.cs.leo.text.model.WordGroup;

public interface Analysis {

	public void analyze(List<WordGroup> records);
}
