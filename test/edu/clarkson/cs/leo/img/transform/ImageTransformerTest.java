package edu.clarkson.cs.leo.img.transform;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.clarkson.cs.leo.img.transform.ImageTransformer;

public class ImageTransformerTest {

	@Test
	public void testTransform() throws Exception {
		ImageTransformer.transform(
				ImageIO.read(new File("res/image/ebay_1.jpg")),
				AffineTransform.getTranslateInstance(10, 10));
		ImageTransformer.transform(
				ImageIO.read(new File("res/image/logo11w.png")),
				AffineTransform.getTranslateInstance(10, 10));
	}

	@Test
	public void testScale() throws Exception {
		BufferedImage output = ImageTransformer
				.scale(ImageIO
						.read(new File(
								"/home/harper/ResearchData/webpage-comparison/imageset_test/12233998843_6433e47de9_b.jpg")),
						500, 500);
	}
}
