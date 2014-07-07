package edu.clarkson.cs.leo.task;

import java.io.File;

import edu.clarkson.cs.leo.svm.FileDataSet;
import edu.clarkson.cs.leo.svm.libsvm.LibSVMTrainer;

public class TrainSVM {

	public static void main(String[] args) {
		LibSVMTrainer trainer = new LibSVMTrainer();
		trainer.train(new FileDataSet(new File("workdir/model/logo_train")));
	}

}
