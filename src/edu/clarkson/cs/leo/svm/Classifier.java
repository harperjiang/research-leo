package edu.clarkson.cs.leo.svm;

public interface Classifier {

	public DataSet classify(Model model, DataSet input);
}
