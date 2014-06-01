package edu.clarkson.cs.wpcomp.task;

import java.io.File;

import edu.clarkson.cs.wpcomp.svm.FileDataSet;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMTrainer;

public class TrainSVM {

	public static void main(String[] args) {
		LibSVMTrainer trainer = new LibSVMTrainer();
		trainer.train(new FileDataSet(new File("workdir/model/logo_train")));
	}

}
