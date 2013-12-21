package edu.clarkson.cs.wpcomp.img.desc.descriptor;

public class NormalizationHelper {

	/**
	 * 
	 * @param input
	 * @param width
	 * @param height
	 * @param extend
	 * @return
	 */
	public static double[] normalize(double[] input, int width, int height,
			int extend) {
		double[] output = new double[input.length];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double sum = 0;
				for (int x = i - extend; x <= i + extend; x++) {
					for (int y = j - extend; y <= j + extend; y++) {
						int x0 = Math.min(Math.max(0, x), width - 1);
						int y0 = Math.min(Math.max(0, y), height - 1);
						sum += input[x0 + y0 * width];
					}
				}
				output[j * width + i] = (sum == 0 ? 0 : Math.sqrt(input[j
						* width + i]
						/ sum));
			}
		}
		return output;
	}

	/**
	 * Normalize a vector
	 * 
	 * @param input
	 * @return
	 */
	public static double[] normalize(double[] input) {
		double[] output = new double[input.length];
		double sum = 0;
		for (int i = 0; i < input.length; i++)
			sum += input[i];
		for (int i = 0; i < input.length; i++)
			output[i] = (sum == 0) ? 0 : Math.sqrt(input[i] / sum);

		return output;
	}
}
