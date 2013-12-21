package edu.clarkson.cs.wpcomp.img.desc.descriptor;

import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.clarkson.cs.wpcomp.img.desc.accessor.ImageAccessor;

public class GradientDescriptorTest extends GradientDescriptor {

	@Test
	public void testDescribe() throws Exception {
		new GradientDescriptor().describe(new ImageAccessor(ImageIO
				.read(new File("res/image/ebay_1.jpg"))));
	}

}
