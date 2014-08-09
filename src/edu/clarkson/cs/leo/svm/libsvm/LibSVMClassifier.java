package edu.clarkson.cs.leo.svm.libsvm;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.cs.clientlib.lang.proc.OutputHandler;
import edu.clarkson.cs.clientlib.lang.proc.ProcessRunner;
import edu.clarkson.cs.leo.svm.Classifier;
import edu.clarkson.cs.leo.svm.DataSet;
import edu.clarkson.cs.leo.svm.FileDataSet;
import edu.clarkson.cs.leo.svm.FileModel;
import edu.clarkson.cs.leo.svm.Model;

public class LibSVMClassifier implements Classifier {

	private static final String SVM_PREDICT = "svm-predict";

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public DataSet classify(Model model, DataSet input) {
		if (!(input instanceof FileDataSet)) {
			throw new IllegalArgumentException();
		}
		FileDataSet inputFds = (FileDataSet) input;
		if (!(model instanceof FileModel)) {
			throw new IllegalArgumentException();
		}
		FileModel fModel = (FileModel) model;

		File output = new File(inputFds.getFile().getAbsolutePath() + ".output");

		ProcessRunner runner = new ProcessRunner(
				Configuration.ROOT_FOLDER.getAbsolutePath() + File.separator
						+ SVM_PREDICT, inputFds.getFile().getAbsolutePath(),
				fModel.getFile().getAbsolutePath(), output.getAbsolutePath());

		runner.setHandler(new OutputHandler() {
			@Override
			public void output(String input) {
				// TODO Handle error input
			}
		});
		try {
			runner.runAndWait();
		} catch (Exception e) {
			logger.error("Exception while executing predict", e);
			throw new RuntimeException(e);
		}

		return new FileDataSet(output);
	}
}
