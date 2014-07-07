package edu.clarkson.cs.leo.img.accessor;

import java.awt.Color;

public interface ColorAccessor {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param channel
	 *            0:R, 1:G, 2:B
	 * @return
	 */
	public Color getValue(int x, int y);

	/**
	 * 
	 * @param x
	 * @param y
	 * @param channel
	 * @param value
	 */
	public void setValue(int x, int y, Color color);

	/**
	 * 
	 * @return width
	 */
	public int getWidth();

	/**
	 * 
	 * @return height
	 */
	public int getHeight();
}
