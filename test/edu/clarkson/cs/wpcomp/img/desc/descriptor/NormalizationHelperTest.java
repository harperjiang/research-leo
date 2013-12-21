package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NormalizationHelperTest {

	@Test
	public void testNormalize() {
		double[] data = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16 };
		double[] output = NormalizationHelper.normalize(data, 4, 4, 1);
		assertEquals(output[0], Math.sqrt(((double) 1) / 24), 0.0001);
		assertEquals(output[5], Math.sqrt((double) 6 / 54), 0.0001);
	}

	@Test
	public void testNormalize2() {
		double[] input = new double[] { 1, 2, 3, 4, 5, 6 };
		double[] output = NormalizationHelper.normalize(input);

		assertEquals(output[0], Math.sqrt(((double) 1) / 21), 0.0001);
	}
}
