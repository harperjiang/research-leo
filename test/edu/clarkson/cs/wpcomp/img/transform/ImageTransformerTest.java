package edu.clarkson.cs.wpcomp.img.transform;

import java.awt.geom.AffineTransform;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ImageTransformerTest {

	@Test
	public void testTransform() throws Exception {
		ImageTransformer.transform(ImageIO.read(new File("res/image/ebay_1.jpg")),
				AffineTransform.getTranslateInstance(10, 10));
		ImageTransformer.transform(ImageIO.read(new File("res/image/logo11w.png")),
				AffineTransform.getTranslateInstance(10, 10));
	}
}
