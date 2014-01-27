package edu.clarkson.cs.wpcomp.img.textdetect;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class TextGenerateMain {

	static final char[] SYMBOL = { ',', '.', '!', '\"', '\'', '?', '-' };

	static StringBuilder ALPHABET;

	static {
		ALPHABET = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			ALPHABET.append((char)('a' + i));
			ALPHABET.append((char)('A' + i));
		}
		for (int i = 0; i < 10; i++) {
			ALPHABET.append((char)('0' + i));
		}
		ALPHABET.append(SYMBOL);
	}

	public static void main(String[] args) throws IOException {
		BufferedImage image = new BufferedImage(1000, 200,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphic = image.createGraphics();
		graphic.setColor(Color.YELLOW);
		graphic.fillRect(0, 0, 1000, 200);
		graphic.setColor(Color.BLACK);
		graphic.setFont(new Font("Sans Serif", Font.BOLD, 14));

		StringBuilder sb = new StringBuilder();
		Random random = new Random(System.currentTimeMillis());
		int length = random.nextInt(100) + 1;

		for (int i = 0; i < length; i++) {
			sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}

		graphic.drawString(sb.toString(), 20, 30);

		ImageIO.write(image, "png", new File("text.png"));
	}
}
