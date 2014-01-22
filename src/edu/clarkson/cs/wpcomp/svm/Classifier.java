package edu.clarkson.cs.wpcomp.svm;

public interface Classifier {

	public DataSet classify(Model model, DataSet input);
}
