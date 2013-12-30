package edu.clarkson.cs.wpcomp.img.accessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageAccessor implements ColorAccessor {

	private BufferedImage source;

	private Raster data;

	public ImageAccessor(BufferedImage source) {
		super();
		this.source = source;
	}

	protected Raster getData() {
		if (null == data) {
			data = source.getData();
		}
		return data;
	}

	@Override
	public Color getValue(int x, int y) {
		int rgb = source.getRGB(x, y);
		return new Color(rgb);
	}

	@Override
	public void setValue(int x, int y, Color value) {
		source.setRGB(x, y, value.getRGB());
	}

	@Override
	public int getWidth() {
		return getData().getWidth();
	}

	public int getHeight() {
		return getData().getHeight();
	}

}
