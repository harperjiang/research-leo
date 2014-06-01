package edu.clarkson.cs.wpcomp.perf.img;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.clarkson.cs.wpcomp.svm.FileDataSet;
import edu.clarkson.cs.wpcomp.svm.Model;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMClassifier;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMTrainer;
import edu.clarkson.cs.wpcomp.task.imgdesc.PositiveSetGenerator;
import edu.clarkson.cs.wpcomp.task.imgdesc.PositiveSetGenerator.Input;

public class ImageRecognization {

	public static void main(String[] args) throws Exception {
		Random random = new Random(System.currentTimeMillis());
		File[] files = new File(
				"/home/harper/Research/webpage-comparison/imageset_test")
				.listFiles();
		File origin = files[random.nextInt(files.length)];

		// Clean environment

		new ProcessBuilder("cp", "res/svm/img/negative", "workdir").start()
				.waitFor();
		PositiveSetGenerator.generate(origin, new File("workdir/positive"));

		ProcessBuilder pb = new ProcessBuilder("cat", "workdir/negative",
				"workdir/positive");
		pb.redirectOutput(new File("workdir/train"));
		pb.start().waitFor();
		// Train SVM
		Model model = new LibSVMTrainer().train(new FileDataSet(new File(
				"workdir/train")));

		List<Input> toTest = new ArrayList<Input>();
		while (toTest.size() < 500) {
			File f = files[random.nextInt(files.length)];
			if (!f.equals(origin)) {
				toTest.add(new Input(f, 0));
			}
		}
		toTest.add(new Input(origin, 1));

		PositiveSetGenerator.generate(toTest, new File("workdir/test"));

		new LibSVMClassifier().classify(model, new FileDataSet(new File(
				"workdir/test")));
	}
}
