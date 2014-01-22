package edu.clarkson.cs.wpcomp.svm.libsvm;

import java.io.File;

import javax.xml.crypto.Data;

import org.slf4j.LoggerFactory;

import edu.clarkson.cs.wpcomp.common.proc.OutputHandler;
import edu.clarkson.cs.wpcomp.common.proc.ProcessRunner;
import edu.clarkson.cs.wpcomp.svm.FileDataSet;
import edu.clarkson.cs.wpcomp.svm.FileModel;
import edu.clarkson.cs.wpcomp.svm.Model;
import edu.clarkson.cs.wpcomp.svm.Trainer;

public class LibSVMTrainer implements Trainer {

	private static final String SVM_TRAIN = "svm-train";

	@Override
	public Model train(Data input) {
		if (!(input instanceof FileDataSet)) {
			throw new IllegalArgumentException();
		}
		FileDataSet trainset = (FileDataSet) input;
		ProcessRunner runner = new ProcessRunner(
				Configuration.ROOT_FOLDER.getAbsoluteFile() + File.separator
						+ SVM_TRAIN, trainset.getFile().getAbsolutePath(),
				trainset.getFile().getAbsolutePath() + ".model");
		runner.setHandler(new OutputHandler() {
			@Override
			public void output(String input) {
				// TODO If there's error message,throw an exception
			}
		});
		try {
			runner.runAndWait();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Exception when executing external trainer", e);
			throw new RuntimeException(e);
		}
		return new FileModel(new File(trainset.getFile().getAbsolutePath()
				+ ".model"));
	}

}
