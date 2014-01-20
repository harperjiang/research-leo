package edu.clarkson.cs.wpcomp.img;

import java.awt.Color;
import java.awt.image.BufferedImage;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class GradientHelper {

	public static BufferedImage gradientImage(BufferedImage input, int threshold) {
		BufferedImage output = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_INT_RGB);
		ColorAccessor accessor = new ImageAccessor(input);
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
				int value = unifiedGradient(accessor, i, j);
				value = value >= threshold ? value : 0;
				output.setRGB(i, j, new Color(value, value, value).getRGB());
			}
		}
		return output;
	}

	public static int[] angledGradient(ColorAccessor input, int ix, int iy) {
		int[] red = singleGradient(input, ix, iy, 0);
		int[] green = singleGradient(input, ix, iy, 1);
		int[] blue = singleGradient(input, ix, iy, 2);
		int max = Math.max(red[0], Math.max(green[0], blue[0]));
		if (max == red[0])
			return red;
		if (max == green[0])
			return green;
		if (max == blue[0])
			return blue;
		return null;
	}

	public static int unifiedGradient(ColorAccessor input, int ix, int iy) {
		int[] red = singleGradient(input, ix, iy, 0);
		int[] green = singleGradient(input, ix, iy, 1);
		int[] blue = singleGradient(input, ix, iy, 2);
		return Math.max(red[0], Math.max(green[0], blue[0]));
	}

	public static int[] singleGradient(ColorAccessor input, int ix, int iy,
			int color) {
		// int h0 = input.getValue(ix, iy, channel);
		Color hlc = input.getValue(Math.max(0, ix - 1), iy);
		Color hrc = input.getValue(Math.min(input.getWidth() - 1, ix + 1), iy);
		Color htc = input.getValue(ix, Math.max(0, iy - 1));
		Color hbc = input.getValue(ix, Math.min(input.getHeight() - 1, iy + 1));
		int hl = 0, hr = 0, ht = 0, hb = 0;
		switch (color) {
		case 0:
			hl = hlc.getRed();
			hr = hrc.getRed();
			ht = htc.getRed();
			hb = hbc.getRed();
			break;
		case 1:
			hl = hlc.getGreen();
			hr = hrc.getGreen();
			ht = htc.getGreen();
			hb = hbc.getGreen();
			break;
		case 2:
			hl = hlc.getBlue();
			hr = hrc.getBlue();
			ht = htc.getBlue();
			hb = hbc.getBlue();
			break;
		default:
			break;
		}

		// [-1, 0, 1]
		int qv = hb - ht;
		int qh = hr - hl;
		if (qv < 0) {
			// Change to [0,pi]
			qv = -qv;
			qh = -qh;
		}

		double val = Math.sqrt((Math.pow(qv, 2) + Math.pow(qh, 2)) / 2);

		double angle = Math.acos((double) qh / val);
		int dir = (int) Math.floor(9 * angle / (Math.PI));

		return new int[] { (int) val, dir };
	}
}
