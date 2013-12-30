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
		Dimension[][] pp = preprocess(accessor);
		Rectangle max = maxsplit(new Rectangle(0, 0, accessor.getWidth(),
				accessor.getHeight()), pp, accessor);
		// redrect(max, accessor);
		Rectangle lower = lowerbound(max, pp, accessor);
		redrect(lower, accessor);
		// System.out.println(lower);

		Rectangle max2 = maxsplit(new Rectangle(lower.x + 1, lower.y + 1,
				lower.width - 2, lower.height - 2), pp, accessor);
		redrect(max2, accessor);
		System.out.println(max2);

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

	protected static Rectangle maxsplit(Rectangle range, Dimension[][] pp,
			ImageAccessor accessor) {
		long area = 0;
		Rectangle max = new Rectangle();
		// TODO Add constraint of range to the calculation
		for (int i = range.x; i < range.x + range.width; i++) {
			for (int j = range.y; j < range.y + range.height; j++) {
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
		return max;
	}

	protected static Rectangle lowerbound(Rectangle range, Dimension[][] pp,
			ImageAccessor accessor) {
		Rectangle lowerbound = new Rectangle(range);
		int i = 0;
		for (i = 0; i < range.width; i++) {
			if (pp[range.x + i][range.y].height < pp[range.x][range.y].height)
				break;
		}
		lowerbound.x += i - 1;
		lowerbound.width -= i - 1;
		for (i = range.width - 1; i > 0; i--) {
			if (pp[range.x + i][range.y].height < pp[range.x][range.y].height)
				break;
		}
		lowerbound.width -= range.width - 1 - i;
		for (i = 0; i < range.height; i++) {
			if (pp[range.x][range.y + i].width < pp[range.x][range.y].width)
				break;
		}
		lowerbound.y += i - 1;
		lowerbound.height -= i - 1;
		for (i = 0; i < range.height; i++) {
			if (pp[range.x][range.y + range.height - 1 - i].width < pp[range.x][range.y].width) {
				break;
			}
		}
		lowerbound.height -= i - 1;

		return lowerbound;
	}

	protected static void redrect(Rectangle range, ImageAccessor accessor) {
		for (int i = range.x; i < range.x + range.width; i++) {
			accessor.setValue(i, range.y, Color.RED);
			accessor.setValue(i, range.y + range.height, Color.RED);
		}
		for (int i = range.y; i < range.y + range.height; i++) {
			accessor.setValue(range.x, i, Color.RED);
			accessor.setValue(range.x + range.width, i, Color.RED);
		}
	}
}
