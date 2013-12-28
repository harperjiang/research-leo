package edu.clarkson.cs.wpcomp.img.accessor;

public interface RGBAccessor {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param channel
	 *            0:R, 1:G, 2:B
	 * @return
	 */
	public int getValue(int x, int y, int channel);

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
