package edu.clarkson.cs.leo.perf.svm;

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
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.leo.img.GradientHelper;
import edu.clarkson.cs.leo.img.ImageHelper;
import edu.clarkson.cs.leo.img.accessor.ColorAccessor;
import edu.clarkson.cs.leo.img.accessor.ImageAccessor;
import edu.clarkson.cs.leo.img.desc.Feature;
import edu.clarkson.cs.leo.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.leo.img.textdetect.TextImageDescriptor;
import edu.clarkson.cs.leo.img.transform.ImageTransformer;

public class TextAccuracyDataGen {

	static final char[] SYMBOL = { ',', '.', '!', '\"', '\'', '?', '-' };

	static StringBuilder ALPHABET;

	static {
		ALPHABET = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			ALPHABET.append((char) ('a' + i));
			ALPHABET.append((char) ('A' + i));
		}
		for (int i = 0; i < 10; i++) {
			ALPHABET.append((char) ('0' + i));
		}
		ALPHABET.append(SYMBOL);
	}

	static Font[] FONTS = new Font[] { new Font("Geogia", Font.PLAIN, 12),
			new Font("Sans-serif", Font.PLAIN, 12),
			new Font("Arial", Font.PLAIN, 12),
			new Font("Courier", Font.PLAIN, 12) };

	public static void main(String[] args) throws Exception {
		preprocess();
		generateFromFile();
	}

	public static void preprocess() throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/text_acc/input"));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/svm/perf/text_acc/source")));

		StringBuilder sb = new StringBuilder();
		int rowlength = 80;
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line.trim());
		}
		br.close();
		for (int i = 0; i < sb.length(); i += rowlength) {
			pw.println(sb.subSequence(i, Math.min(sb.length(), i + rowlength)));
		}
		pw.close();
	}

	public static void generateFromFile() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/perf/text_acc/test_pos"));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/svm/perf/text_acc/input")));
		String line = null;
		int count = 0;
		while ((line = br.readLine()) != null) {
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
			Feature feature = generateFeature(image, "" + count++);
			pw.println(MessageFormat.format("{0} {1}", 1, feature));
		}
		pw.close();
		br.close();
	}

	private static TextImageDescriptor desc = new TextImageDescriptor();

	private static Feature generateFeature(BufferedImage image, String name)
			throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(image, 20);
		ColorAccessor accessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(accessor);
		Rectangle lower = splitter.lowerBound(null);
		BufferedImage cropped = ImageHelper.crop(gradient, lower);
		BufferedImage scaled = ImageTransformer.scale(cropped,
				(int) (50 * (double) cropped.getWidth() / (double) cropped
						.getHeight()), 50);
		// ImageIO.write(
		// scaled,
		// "png",
		// new File(MessageFormat.format(
		// "res/svm/perf/text_acc/name_{0}.png", name)));
		return desc.describe(new ImageAccessor(scaled));
	}
}
