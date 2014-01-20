package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import edu.clarkson.cs.wpcomp.img.GradientHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.desc.Histogram;
import edu.clarkson.cs.wpcomp.img.desc.Feature;
import edu.clarkson.cs.wpcomp.img.desc.SVMDescriptor;

public class HogSVMDescriptor implements SVMDescriptor {

	// Cells are nxn pixels
	private int cellSize;

	// Rectangular block. Blocks are (2n+1)(2n+1) cells.
	private int normalizeSize;

	public HogSVMDescriptor() {
		this(10, 2);
	}

	public HogSVMDescriptor(int cellSize, int normalizeSize) {
		super();
		this.cellSize = cellSize;
		this.normalizeSize = normalizeSize;
	}

	@Override
	public Feature describe(ColorAccessor img) {
		int cellWidth = (int) Math.ceil((double) img.getWidth() / cellSize);
		int cellHeight = (int) Math.ceil((double) img.getHeight() / cellSize);

		Histogram[][] cells = new Histogram[cellWidth][cellHeight];
		int ix, iy;
		int cellx, celly;

		// Gradient Calculation
		// Orientation Binning
		for (ix = 0; ix < img.getWidth(); ix++) {
			for (iy = 0; iy < img.getHeight(); iy++) {
				cellx = ix / cellSize;
				celly = iy / cellSize;
				if (cells[cellx][celly] == null) {
					cells[cellx][celly] = new Histogram();
				}
				int[] grad = GradientHelper.angledGradient(img, ix, iy);
				cells[cellx][celly].add(grad[1], grad[0]);
			}
		}

		double[][] dataarray = new double[9][cellWidth * cellHeight];
		for (int i = 0; i < cellWidth; i++) {
			for (int j = 0; j < cellHeight; j++) {
				for (int rindex = 0; rindex < 9; rindex++) {
					dataarray[rindex][j * cellWidth + i] = cells[i][j]
							.get(rindex);
				}
			}
		}

		Feature data = new Feature();
		// Normalization
		for (int i = 0; i < 9; i++) {
			dataarray[i] = NormalizationHelper.normalize(dataarray[i],
					cellWidth, cellHeight, normalizeSize);
			data.join(new Feature(dataarray[i]));
		}
		return data;
	}

}
