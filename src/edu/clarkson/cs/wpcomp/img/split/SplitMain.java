package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import edu.clarkson.cs.wpcomp.img.MarkHelper;
import edu.clarkson.cs.wpcomp.img.accessor.ColorAccessor;
import edu.clarkson.cs.wpcomp.img.accessor.ImageAccessor;

public class SplitMain {

	public static void main(String[] args) throws IOException {
		BufferedImage input = ImageIO.read(new File(
				"res/image/split/phishing.png"));
		Split split = new Split();
		split.setLevel(6);
		List<Rectangle> ranges = split.split(input);

		ColorAccessor accessor = new ImageAccessor(input);
		for (Rectangle rect : ranges) {
			MarkHelper.redrect(rect, accessor);
		}
		ImageIO.write(input, "png", new File(
				"res/image/split/phishing_split.png"));
	}
}
