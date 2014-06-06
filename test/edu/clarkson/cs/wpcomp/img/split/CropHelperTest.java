package edu.clarkson.cs.wpcomp.img.split;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.clarkson.cs.wpcomp.img.ImageHelper;

public class CropHelperTest {

	@Test
	public void testCrop() throws IOException {
		BufferedImage image = ImageIO.read(new File(
				"res/image/split/bbc_screen.png"));
		BufferedImage croped = ImageHelper.crop(image, new Rectangle(800, 20,
				40, 50));
		ImageIO.write(croped, "png", new File("res/image/split/bbc_croped.png"));
	}

}
