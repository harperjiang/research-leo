package edu.clarkson.cs.wpcomp.svm;

import javax.xml.crypto.Data;

public interface Trainer {

	public Model train(Data input);
}
