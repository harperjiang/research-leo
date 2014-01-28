package edu.clarkson.cs.wpcomp.img.textdetect;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.desc.SVMDescriptor;

public class TextImageDescriptor implements SVMDescriptor {

	@Override
	public Feature describe(ColorAccessor input) {
		List<Double> value = new ArrayList<Double>();
		for (int y = 0; y < input.getHeight(); y++) {
			double sum = 0;
			for (int x = 0; x < input.getWidth(); x++) {
				Color color = input.getValue(x, y);
				if (!Color.BLACK.equals(color)) {
					sum++;
				}
			}
			value.add(sum / input.getWidth());
		}
		return new Feature(value);
	}

}
