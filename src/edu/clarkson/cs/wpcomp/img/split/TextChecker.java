package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.GeometryHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.textdetect.TextImageDescriptor;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;
import edu.clarkson.cs.wpcomp.svm.Classifier;
import edu.clarkson.cs.wpcomp.svm.DataSet;
import edu.clarkson.cs.wpcomp.svm.FileDataSet;
import edu.clarkson.cs.wpcomp.svm.FileModel;
import edu.clarkson.cs.wpcomp.svm.libsvm.LibSVMClassifier;

public class TextChecker implements Checker {

	private File tempDir;

	private Classifier classifier;

	private TextImageDescriptor desc;

	private int heightThreshold = 20;

	public TextChecker() {
		super();
		classifier = new LibSVMClassifier();
		desc = new TextImageDescriptor();
		tempDir = new File("workdir/temp");
	}

	@Override
	public boolean check(Rectangle range, CheckerEnv cenv) {
		List<Rectangle> source = new ArrayList<Rectangle>();
		List<Rectangle> result = new ArrayList<Rectangle>();
		List<Rectangle> output = new ArrayList<Rectangle>();
		source.add(range);

		while (!source.isEmpty()) {
			for (Rectangle r : source) {
				Rectangle current = cenv.rectSplitter.lowerBound(r);
				if (null != current) {
					LineSegment split = cenv.lineSplitter.maxMarginSplit(
							current, true);
					if (split != null) {
						Rectangle[] splitted = GeometryHelper.split(r, split);
						result.add(splitted[0]);
						result.add(splitted[1]);
					} else {
						output.add(current);
					}
				}
			}
			source = new ArrayList<Rectangle>();
			source.addAll(result);
			result = new ArrayList<Rectangle>();
		}
		// Many companies use single word as Logo
		if (output.size() == 1 && output.get(0).height > heightThreshold) {
			return true;
		}

		List<Feature> features = new ArrayList<Feature>();

		for (Rectangle r : output) {
			BufferedImage cropped = CropHelper.crop(cenv.sourceImage, r);
			BufferedImage scaled = ImageTransformer.scale(cropped,
					(int) (50 * (double) cropped.getWidth() / (double) cropped
							.getHeight()), 50);
			Feature feature = desc.describe(new ImageAccessor(scaled));
			features.add(feature);
		}

		try {
			String fileName = UUID.randomUUID().toString();
			fileName = MessageFormat.format("{0}{1}{2}",
					tempDir.getAbsolutePath(), File.separator, fileName);
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			for (Feature feature : features) {
				pw.println(MessageFormat.format("{0} {1}", 0, feature));
			}
			pw.close();

			DataSet classifyResult = classifier.classify(new FileModel(
					new File("res/svm/text/train.model")), new FileDataSet(
					new File(fileName)));

			FileDataSet fds = (FileDataSet) classifyResult;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fds.getFile())));
			String line = null;

			int correct = 0;

			while (null != (line = br.readLine())) {
				try {
					if (Integer.parseInt(line) == 1)
						correct++;
				} catch (Exception e) {

				}
			}
			br.close();
			return (correct != features.size());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File getTempDir() {
		return tempDir;
	}

	public void setTempDir(File tempDir) {
		this.tempDir = tempDir;
	}
}
