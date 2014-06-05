package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;

import edu.clarkson.cs.wpcomp.img.FeatureHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class EntropyFilter implements Filter {

	private double entropyThreshold = 0.5;

	@Override
	public boolean filter(Rectangle r, SplitEnv env) {
		double entropy = FeatureHelper.entropy(new ImageAccessor(
				env.sourceImage), r);
		return entropy > entropyThreshold;
	}

}
