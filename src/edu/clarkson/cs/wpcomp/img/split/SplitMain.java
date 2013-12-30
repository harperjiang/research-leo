package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/screen.png"));

		BufferedImage gradient = GradientHelper.gradientImage(input);

		// ImageIO.write(gradient, "png", new
		// File("res/image/split/gradient.png"));

		/*
		 * Split the image
		 */
		/*
		 * Ideas: Too small block will be combined to affiliate block
		 */

		ImageAccessor accessor = new ImageAccessor(gradient);

		/*
		 * for (int i = 0; i < accessor.getWidth(); i++) { for (int j = 0; j <
		 * accessor.getHeight(); j++) { if (accessor.getValue(i,
		 * j).equals(Color.BLACK)) { accessor.setValue(i, j, Color.RED); } } }
		 */
		System.out.println(System.currentTimeMillis());
		Dimension[][] pp = preprocess(accessor);
		System.out.println(System.currentTimeMillis());

		long area = 0;
		Rectangle max = new Rectangle();

		for (int i = 0; i < accessor.getWidth(); i++) {
			for (int j = 0; j < accessor.getHeight(); j++) {
				Dimension topLeft = pp[i][j];
				long possible = topLeft.width * topLeft.height;
				for (int w = topLeft.width; w > 0; w--) {
					for (int h = topLeft.height; h > 0; h--) {
						if (w * h < area) {
							// fast break
							w = 0;
							h = 0;
							continue;
						}
						Dimension topRight = pp[i + w][j];
						Dimension bottomLeft = pp[i][j + h];
						if (topRight.height >= topLeft.height
								&& bottomLeft.width >= topLeft.width) {
							// Rectangle
							long curarea = w * h;
							if (curarea > area) {
								area = curarea;
								max.x = i;
								max.y = j;
								max.width = w;
								max.height = h;
							}
							if (curarea == possible) {
								// Fast break
								w = 0;
								h = 0;
							}
						}
					}
				}

			}
		}
		System.out.println(System.currentTimeMillis());
		System.out.println(max);

		for (int i = max.x; i < max.x + max.width; i++) {
			accessor.setValue(i, max.y, Color.RED);
			accessor.setValue(i, max.y + max.height, Color.RED);
		}
		for (int i = max.y; i < max.y + max.height; i++) {
			accessor.setValue(max.x, i, Color.RED);
			accessor.setValue(max.x + max.width, i, Color.RED);
		}

		ImageIO.write(gradient, "png", new File("res/image/split/split.png"));
	}

	protected static Dimension[][] preprocess(ImageAccessor accessor) {
		Dimension[][] data = new Dimension[accessor.getWidth()][accessor
				.getHeight()];
		// Vertical data
		for (int i = 0; i < accessor.getWidth(); i++) {
			int index = 0;
			while (index < accessor.getHeight()) {
				if (accessor.getValue(i, index).getRed() == 0) {
					int start = index;
					while (index < accessor.getHeight()
							&& accessor.getValue(i, index).getRed() == 0) {
						index++;
					}
					for (int j = index - 1; j >= start; j--) {
						data[i][j] = new Dimension(-1, index - 1 - j);
					}
				} else {
					while (index < accessor.getHeight()
							&& accessor.getValue(i, index).getRed() != 0) {
						data[i][index++] = new Dimension(-1, 0);
					}
				}
			}
		}
		// Horizontal Data
		for (int i = 0; i < accessor.getHeight(); i++) {
			int index = 0;
			while (index < accessor.getWidth()) {
				if (accessor.getValue(index, i).getRed() == 0) {
					int start = index;
					while (index < accessor.getWidth()
							&& accessor.getValue(index, i).getRed() == 0) {
						index++;
					}
					for (int j = index - 1; j >= start; j--) {
						data[j][i].width = index - 1 - j;
					}
				} else {
					while (index < accessor.getWidth()
							&& accessor.getValue(index, i).getRed() != 0) {
						data[index++][i].width = 0;
					}
				}
			}
		}
		return data;
	}
}
