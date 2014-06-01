package edu.clarkson.cs.wpcomp.task.textdetect;

import java.io.File;

import edu.clarkson.cs.wpcomp.svm.FileDataSet;
import edu.clarkson.cs.wpcomp.svm.Model;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMClassifier;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMTrainer;

public class TrainSVMForText {

	public static void main(String[] args) {
		LibSVMTrainer trainer = new LibSVMTrainer();
		LibSVMClassifier classifier = new LibSVMClassifier();
		Model model = trainer.train(new FileDataSet(new File(
				"workdir/textdetect/train")));
		classifier.classify(model, new FileDataSet(
				new File("workdir/textdetect/test")));
	}

}
