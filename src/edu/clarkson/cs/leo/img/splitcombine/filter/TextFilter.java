package edu.clarkson.cs.leo.img.splitcombine.filter;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;

import edu.clarkson.cs.leo.img.GeometryHelper;
import edu.clarkson.cs.leo.img.ImageHelper;
import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.desc.Feature;
import edu.clarkson.cs.leo.img.splitcombine.LineSegment;
import edu.clarkson.cs.leo.img.splitcombine.LineSplitter;
import edu.clarkson.cs.leo.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.leo.img.splitcombine.SplitEnv;
import edu.clarkson.cs.leo.img.textdetect.TextImageDescriptor;
import edu.clarkson.cs.leo.img.transform.ImageTransformer;
import edu.clarkson.cs.leo.svm.Classifier;
import edu.clarkson.cs.leo.svm.DataSet;
import edu.clarkson.cs.leo.svm.FileDataSet;
import edu.clarkson.cs.leo.svm.FileModel;
import edu.clarkson.cs.leo.svm.DataSet.Row;
import edu.clarkson.cs.leo.svm.libsvm.LibSVMClassifier;

public class TextFilter implements Filter {

	private File tempDir;

	private Classifier classifier;

	private TextImageDescriptor desc;

	private int heightThreshold = 25;

	private double ratioThreshold = 1.33;

	public TextFilter() {
		super();
		classifier = new LibSVMClassifier();
		desc = new TextImageDescriptor();
		tempDir = new File("workdir/temp");
	}

	@Override
	public FilterResult filter(Rectangle range, SplitEnv cenv) {
		FilterResult fresult = new FilterResult();

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
		if (CollectionUtils.isEmpty(output)) {
			// This is an empty range which can be filtered out
			return fresult;
		}

		// Many companies use single word as Logo, thus remove text with big
		// height or single character like
		List<Rectangle> buffer = new ArrayList<Rectangle>();
		for (Rectangle rect : output) {
			if (rect.height > heightThreshold) {
				fresult.getAccepted().add(rect);
			} else if (GeometryHelper.ratio(rect) < ratioThreshold) {
				fresult.getAccepted().add(rect);
			} else {
				buffer.add(rect);
			}
		}

		output = buffer;

		if (CollectionUtils.isEmpty(output)) {
			return fresult;
		}

		List<Feature> features = new ArrayList<Feature>();

		for (Rectangle r : output) {
			BufferedImage cropped = ImageHelper.crop(cenv.sourceImage, r);
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
					new File("workdir/textdetect/text_train.model")),
					new FileDataSet(new File(fileName)));

			int counter = 0;
			for (Row row : classifyResult) {
				if (0 == Integer.parseInt(String.valueOf(row.get(0))))
					// Not a text
					fresult.getAccepted().add(output.get(counter));
				counter++;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return fresult;
	}

	public File getTempDir() {
		return tempDir;
	}

	public void setTempDir(File tempDir) {
		this.tempDir = tempDir;
	}

	public static void main(String[] args) throws IOException {
		TextFilter filter = new TextFilter();
		BufferedImage image = ImageIO.read(new File(
				"res/image/split/text_3.png"));
		SplitEnv cenv = new SplitEnv();
		cenv.sourceImage = image;
		cenv.lineSplitter = new LineSplitter(new ImageAccessor(image));
		cenv.rectSplitter = new RectangleSplitter(new ImageAccessor(image));
		filter.filter(new Rectangle(0, 0, image.getWidth(), image.getHeight()),
				cenv);
	}
}
