package edu.clarkson.cs.leo.svm.libsvm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;

import edu.clarkson.cs.leo.svm.FileDataSet;
import edu.clarkson.cs.leo.svm.FileModel;
import edu.clarkson.cs.leo.svm.Model;
import edu.clarkson.cs.leo.svm.libsvm.LibSVMTrainer;

public class LibSVMTrainerTest extends LibSVMTrainer {

	@AfterClass
	public static void clearData() {
		File generatedModel = new File("res/svm/img/train.model");
		generatedModel.delete();
	}
	
	@Test
	public void testTrain() {
		FileDataSet dataSet = new FileDataSet(new File("res/svm/img/train"));
		Model model = new LibSVMTrainer().train(dataSet);
		assertEquals(model.getClass(), FileModel.class);

		FileModel fModel = (FileModel) model;
		assertEquals(dataSet.getFile().getAbsolutePath() + ".model", fModel
				.getFile().getAbsolutePath());
		assertTrue(dataSet.getFile().exists());
	}
}
