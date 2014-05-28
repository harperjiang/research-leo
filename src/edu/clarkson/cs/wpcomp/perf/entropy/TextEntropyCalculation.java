package edu.clarkson.cs.wpcomp.perf.entropy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.wpcomp.img.FeatureHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.wpcomp.text.processor.Dicts;

public class TextEntropyCalculation {
	static Font[] FONTS = new Font[] { new Font("Geogia", Font.PLAIN, 12),
			new Font("Sans-serif", Font.PLAIN, 12),
			new Font("Arial", Font.PLAIN, 12),
			new Font("Courier", Font.PLAIN, 12) };

	public static void main(String[] args) throws Exception {
		generateWord();
		generateFromFile();
	}

	public static void generateWord() throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/text_acc/entropy_word"));

		Random random = new Random(System.currentTimeMillis());
		int dictSize = Dicts.wordList.getDict().size();
		List<String> words = new ArrayList<String>();
		words.addAll(Dicts.wordList.getDict());
		for (int repeat = 0; repeat < 10000; repeat++) {
			BufferedImage image = new BufferedImage(1000, 50,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.YELLOW);
			graphic.fillRect(0, 0, 1000, 50);
			graphic.setColor(Color.BLACK);

			Font font = FONTS[random.nextInt(FONTS.length)];
			Font newfont = new Font(font.getName(), font.getStyle(),
					12 + random.nextInt(8));
			graphic.setFont(newfont);

			StringBuilder sb = new StringBuilder();
			int length = random.nextInt(10) + 1;
			for (int i = 0; i < length; i++) {
				int index = random.nextInt(dictSize);
				sb.append(words.get(index)).append(" ");
			}

			graphic.drawString(sb.toString().trim(), 20, 30);
			graphic.dispose();
			double entropy = generateEntropy(image);
			pw.println(MessageFormat.format("{0,number,##0.0000}", entropy));
		}

		pw.close();
	}

	public static void generateFromFile() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/text_acc/entropy_text"));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/svm/perf/text_acc/source")));
		String line = null;
		int count = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (StringUtils.isEmpty(line))
				continue;
			BufferedImage image = new BufferedImage(1000, 50,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.YELLOW);
			graphic.fillRect(0, 0, 1000, 50);
			graphic.setColor(Color.BLACK);

			Font font = FONTS[random.nextInt(FONTS.length)];
			Font newfont = new Font(font.getName(), font.getStyle(),
					12 + random.nextInt(8));
			graphic.setFont(newfont);

			graphic.drawString(line.toString(), 20, 30);
			graphic.dispose();
			Double entropy = generateEntropy(image);
			if (null != entropy)
				pw.println(MessageFormat.format("{0,number,##0.0000}", entropy));
		}
		pw.close();
		br.close();
	}

	private static Double generateEntropy(BufferedImage image)
			throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(image, 20);
		ColorAccessor gaccessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(gaccessor);
		Rectangle lower = splitter.lowerBound(null);
		if (lower != null) {
			return FeatureHelper.entropy(new ImageAccessor(image), lower);
		} else {
			return null;
		}
	}
}
