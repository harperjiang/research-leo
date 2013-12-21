package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import java.awt.Point;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import edu.clarkson.cs.wpcomp.img.desc.accessor.ImageAccessor;
import edu.clarkson.cs.wpcomp.img.desc.accessor.RGBAccessor;

public class GradientHelper {

	public static BufferedImage gradientImage(BufferedImage input) {
		WritableRaster oraster = Raster.createWritableRaster(
				new BandedSampleModel(DataBuffer.TYPE_INT, input.getWidth(),
						input.getHeight(), 3), new Point(0, 0));
		RGBAccessor accessor = new ImageAccessor(input);
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
				int value = unifiedGradient(accessor, i, j);
				oraster.setPixel(i, j, new int[] { value, value, value });
			}
		}
		BufferedImage output = new BufferedImage(input.getWidth(),
				input.getHeight(), BufferedImage.TYPE_INT_RGB);
		output.setData(oraster);
		return output;
	}

	public static int[] angledGradient(RGBAccessor input, int ix, int iy) {
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

	public static int unifiedGradient(RGBAccessor input, int ix, int iy) {
		int[] red = singleGradient(input, ix, iy, 0);
		int[] green = singleGradient(input, ix, iy, 1);
		int[] blue = singleGradient(input, ix, iy, 2);
		return Math.max(red[0], Math.max(green[0], blue[0]));
	}

	public static int[] singleGradient(RGBAccessor input, int ix, int iy,
			int channel) {
		// int h0 = input.getValue(ix, iy, channel);
		int hl = input.getValue(Math.max(0, ix - 1), iy, channel);
		int hr = input.getValue(Math.min(input.getWidth() - 1, ix + 1), iy,
				channel);
		int ht = input.getValue(ix, Math.max(0, iy - 1), channel);
		int hb = input.getValue(ix, Math.min(input.getHeight() - 1, iy + 1),
				channel);
		// [-1, 0, 1]
		int qv = hb - ht;
		int qh = hr - hl;
		if (qv < 0) {
			// Change to [0,pi]
			qv = -qv;
			qh = -qh;
		}

		double val = Math.sqrt(Math.pow(qv, 2) + Math.pow(qh, 2));

		double angle = Math.acos((double) qh / val);
		int dir = (int) Math.floor(9 * angle / (Math.PI));

		return new int[] { (int) val, dir };
	}
}
