package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Dimension;

import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;

public class AbstractSplitter {

	protected ColorAccessor accessor;

	protected Dimension[][] preprocess;

	public AbstractSplitter(ColorAccessor accessor) {
		super();
		this.accessor = accessor;
		preprocess();
	}

	public ColorAccessor getAccessor() {
		return accessor;
	}

	protected void preprocess() {
		preprocess = new Dimension[accessor.getWidth()][accessor.getHeight()];
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
						preprocess[i][j] = new Dimension(-1, index - j);
					}
				} else {
					while (index < accessor.getHeight()
							&& accessor.getValue(i, index).getRed() != 0) {
						preprocess[i][index++] = new Dimension(-1, 0);
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
						preprocess[j][i].width = index - j;
					}
				} else {
					while (index < accessor.getWidth()
							&& accessor.getValue(index, i).getRed() != 0) {
						preprocess[index++][i].width = 0;
					}
				}
			}
		}
	}
}
