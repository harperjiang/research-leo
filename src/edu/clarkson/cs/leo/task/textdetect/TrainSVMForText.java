package edu.clarkson.cs.leo.task.textdetect;

import java.io.File;

import edu.clarkson.cs.leo.svm.FileDataSet;
import edu.clarkson.cs.leo.svm.Model;
import edu.clarkson.cs.leo.svm.libsvm.LibSVMClassifier;
import edu.clarkson.cs.leo.svm.libsvm.LibSVMTrainer;

public class TrainSVMForText {

	public static void main(String[] args) {
		LibSVMTrainer trainer = new LibSVMTrainer();
		LibSVMClassifier classifier = new LibSVMClassifier();
		Model model = trainer.train(new FileDataSet(new File(
				"workdir/textdetect/text_train")));
//		classifier.classify(model, new FileDataSet(
//				new File("workdir/textdetect/test")));
	}

}
