/*
 * Notice: we specially design some data that contains only short characters, like "Store", "iPhone"
 */

package edu.clarkson.cs.wpcomp.task.textdetect;

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

import edu.clarkson.cs.wpcomp.img.ImageHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.splitcombine.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.textdetect.TextImageDescriptor;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;
import edu.clarkson.cs.wpcomp.text.processor.Dicts;

public class GenPositiveSetForText {

	static final char[] SYMBOL = { ' ', ' ', ' ', ' ', ' ', ' ' };

	static StringBuilder ALPHABET;

	static {
		ALPHABET = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			ALPHABET.append((char) ('A' + i));
		}
		ALPHABET.append('a');
		ALPHABET.append('b');
		ALPHABET.append('c');
		ALPHABET.append('d');
		ALPHABET.append('e');
		ALPHABET.append('h');
		ALPHABET.append('i');
		ALPHABET.append('k');
		ALPHABET.append('l');
		ALPHABET.append('m');
		ALPHABET.append('n');
		ALPHABET.append('o');
		ALPHABET.append('r');
		ALPHABET.append('s');
		ALPHABET.append('t');
		ALPHABET.append('u');
		ALPHABET.append('v');
		ALPHABET.append('w');
		ALPHABET.append('x');
		ALPHABET.append('z');
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
		generateWord();
//		generateFromFile();
	}

	public static void generateWord() throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"workdir/textdetect/text_positive_short"));

		Random random = new Random(System.currentTimeMillis());

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
			int length = 10 + random.nextInt(20);
			for (int i = 0; i < length; i++) {
				int index = random.nextInt(ALPHABET.length());
				sb.append(ALPHABET.charAt(index));
			}

			graphic.drawString(sb.toString().trim(), 20, 30);
			graphic.dispose();
			Feature feature = generateFeature(image, "" + repeat);
			pw.println(MessageFormat.format("{0} {1}", 1, feature));
		}

		pw.close();
	}

	public static void generateFromFile() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"workdir/textdetect/text_positive"));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("res/textdetect/input")));
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
