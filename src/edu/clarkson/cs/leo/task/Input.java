package edu.clarkson.cs.leo.task;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public interface Input {

	public BufferedImage getImage();

	public int getValue();

	public static class ImageInput implements Input {
		private int value;

		private BufferedImage image;

		public ImageInput(BufferedImage image, int value) {
			this.image = image;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public BufferedImage getImage() {
			return image;
		}

	}

	public static class FileInput extends ImageInput {

		public FileInput(File file, int value) throws Exception {
			super(ImageIO.read(file), value);
		}
	}

}
