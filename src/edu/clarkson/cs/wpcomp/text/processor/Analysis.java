package edu.clarkson.cs.wpcomp.text.processor;

import java.util.List;

import edu.clarkson.cs.wpcomp.text.model.WordGroup;

public interface Analysis {

	public void analyze(List<WordGroup> records);
}
