package edu.clarkson.cs.wpcomp.img.desc.accessor;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageAccessor implements RGBAccessor {

	private BufferedImage source;

	private Raster data;

	private int numBand;

	public ImageAccessor(BufferedImage source) {
		super();
		this.source = source;
		this.numBand = source.getSampleModel().getNumBands();
	}

	protected Raster getData() {
		if (null == data) {
			data = source.getData();
		}
		return data;
	}

	@Override
	public int getValue(int x, int y, int channel) {
		int[] pixel = getData().getPixel(x, y, new int[this.numBand]);
		return pixel[channel];
	}

	@Override
	public int getWidth() {
		return getData().getWidth();
	}

	public int getHeight() {
		return getData().getHeight();
	}
}
