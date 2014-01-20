package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import com.telmomenezes.jfastemd.Feature;
import com.telmomenezes.jfastemd.Feature2D;
import com.telmomenezes.jfastemd.Signature;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.desc.EMDDescriptor;

public class GradientEMDDescriptor implements EMDDescriptor {

	private int cellSize;

	// Area for local normalization
	private int normalizeSize;

	public GradientEMDDescriptor(int cellSize, int normalizeSize) {
		super();
		this.cellSize = cellSize;
		this.normalizeSize = normalizeSize;
	}

	public GradientEMDDescriptor() {
		this(6, 20);
	}

	@Override
	public Signature describe(ColorAccessor accessor) {
		int width = accessor.getWidth(), height = accessor.getHeight();

		int cellWidth = (int) Math.ceil(((double) accessor.getWidth())
				/ cellSize);
		int cellHeight = (int) Math.ceil(((double) accessor.getHeight())
				/ cellSize);
		Feature[] features = new Feature[cellWidth * cellHeight];
		double[] weight = new double[features.length];
		for (int i = 0; i < cellWidth; i++) {
			for (int j = 0; j < cellHeight; j++) {

				Feature2D feature = new Feature2D(i, j);
				double value = 0;

				for (int ii = 0; ii < cellSize; ii++) {
					for (int ij = 0; ij < cellSize; ij++) {
						int x = Math.min(i * cellSize + ii, width - 1);
						int y = Math.min(j * cellSize + ij, height - 1);
						value += GradientHelper.unifiedGradient(accessor, x, y);
					}
				}
				features[j * cellWidth + i] = feature;
				weight[j * cellWidth + i] = value;
			}
		}

		double[] normalized = NormalizationHelper.normalize(weight, cellWidth,
				cellHeight, normalizeSize);

		Signature sig = new Signature();
		sig.setFeatures(features);
		sig.setNumberOfFeatures(features.length);
		sig.setWeights(NormalizationHelper.normalize(weight));
		return sig;
	}
}
