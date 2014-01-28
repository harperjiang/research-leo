package edu.clarkson.cs.wpcomp.img.textdetect;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.clarkson.cs.wpcomp.img.CropHelper;
import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.split.RectangleSplitter;
import edu.clarkson.cs.wpcomp.img.transform.ImageTransformer;
import edu.clarkson.cs.wpcomp.text.processor.Dicts;

public class TextGenPosMain {

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

	public static void main(String[] args) throws Exception {
		generateWord();
	}

	public static void generateWord() throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/text/positive"));

		Random random = new Random(System.currentTimeMillis());
		int dictSize = Dicts.wordList.getDict().size();
		List<String> words = new ArrayList<String>();
		words.addAll(Dicts.wordList.getDict());
		for (int repeat = 0; repeat < 1000; repeat++) {
			BufferedImage image = new BufferedImage(1000, 50,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.YELLOW);
			graphic.fillRect(0, 0, 1000, 50);
			graphic.setColor(Color.BLACK);
			graphic.setFont(new Font("Arial", Font.BOLD, 14));

			StringBuilder sb = new StringBuilder();
			int length = random.nextInt(10) + 1;
			for (int i = 0; i < length; i++) {
				int index = random.nextInt(dictSize);
				sb.append(words.get(index)).append(" ");
			}

			graphic.drawString(sb.toString().trim(), 20, 30);
			graphic.dispose();
			Feature feature = generateFeature(image, "" + repeat);
			pw.println(MessageFormat.format("{0} {1}", 1, feature));
		}

		pw.close();
	}

	public static void generateRandomChar() throws Exception {
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"res/svm/text/positive"));

		for (int repeat = 0; repeat < 1000; repeat++) {
			if (repeat % 10 == 0)
				System.out.println(System.currentTimeMillis());
			BufferedImage image = new BufferedImage(1000, 50,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.YELLOW);
			graphic.fillRect(0, 0, 1000, 50);
			graphic.setColor(Color.BLACK);
			graphic.setFont(new Font("Arial", Font.BOLD, 14));

			StringBuilder sb = new StringBuilder();
			Random random = new Random(System.currentTimeMillis());
			int length = random.nextInt(100) + 1;

			for (int i = 0; i < length; i++) {
				sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
			}

			graphic.drawString(sb.toString(), 20, 30);
			graphic.dispose();
			Feature feature = generateFeature(image, "" + repeat);
			pw.println(MessageFormat.format("{0} {1}", 1, feature));
		}
		pw.close();
	}

	private static TextImageDescriptor desc = new TextImageDescriptor();

	private static Feature generateFeature(BufferedImage image, String name)
			throws IOException {
		BufferedImage gradient = GradientHelper.gradientImage(image, 20);
		ColorAccessor accessor = new ImageAccessor(gradient);
		RectangleSplitter splitter = new RectangleSplitter(accessor);
		Rectangle lower = splitter.lowerBound(null);
		BufferedImage cropped = CropHelper.crop(gradient, lower);
		BufferedImage scaled = ImageTransformer.scale(cropped,
				(int) (50 * (double) cropped.getWidth() / (double) cropped
						.getHeight()), 50);
		// ImageIO.write(scaled, "png",
		// new File(MessageFormat.format("res/svm/text/name_{0}", name)));
		return desc.describe(new ImageAccessor(scaled));
	}
}
