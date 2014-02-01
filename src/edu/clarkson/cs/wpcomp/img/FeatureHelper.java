package edu.clarkson.cs.wpcomp.img;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class FeatureHelper {

	public static double entropy(ColorAccessor accessor, Rectangle range) {
		Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		for (int i = 0; i < range.width; i++) {
			for (int j = 0; j < range.height; j++) {
				int value = unify(accessor.getValue(range.x + i, range.y + j));
				if (!histogram.containsKey(value)) {
					histogram.put(value, 0);
				}
				histogram.put(value, histogram.get(value) + 1);
			}
		}
		int total = range.width * range.height;

		double entropy = 0;
		for (Entry<Integer, Integer> entry : histogram.entrySet()) {
			double p = (double) entry.getValue() / (double) total;
			entropy += p * (-Math.log(p));
		}

		return entropy;
	}

	private static int step = 10;

	protected static int unify(Color color) {
		return new Color((color.getRed() / step) * step,
				(color.getGreen() / step) * step, (color.getBlue() / step)
						* step).getRGB();
	}
}
